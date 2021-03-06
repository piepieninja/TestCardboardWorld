package com.calebdevelops.rajawalivrcardboard;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.calebdevelops.R;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.EyeTransform;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;


public class MyGLRenderer implements CardboardView.StereoRenderer {

    private static final String TAG = "MyGLRenderer";
    private MainActivity activity;

    private static final float CAMERA_Z = 0.01f;
    private static final float TIME_DELTA = 0.3f;

    private static final float YAW_LIMIT = 0.12f;
    private static final float PITCH_LIMIT = 0.12f;

    // We keep the light always position just above the user.
    private final float[] mLightPosInWorldSpace = new float[] {0.0f, 2.0f, 0.0f, 1.0f};
    private final float[] mLightPosInEyeSpace = new float[4];

    public static final int COORDS_PER_VERTEX = 3;

    private int mGlProgram;
    private int mPositionParam;
    private int mNormalParam;
    private int mColorParam;
    private int mViewProjectionParam;
    private int mLightPosParam;
    private int mModelViewParam;
    private int mModelParam;
    private int mIsFloorParam;

    //private float[] mModelCube;
    private float[] mCamera;
    private float[] mView;
    private float[] mHeadView;
    private float[] mViewProjection;
    private float[] mModelView;


    private ModelObject cube, floor;
    private ModelObject wall;
    private CalebObject pyramid;

    public MyGLRenderer(MainActivity activity) {
        this.activity = activity;
        mCamera = new float[16];
        mView = new float[16];
        mViewProjection = new float[16];
        mModelView = new float[16];
        mHeadView = new float[16];
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return success
     */
    public int loadGLShader(int type, int resId) {
        String code = activity.readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     * @param func the input called
     */
    public static void checkGLError(String func) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, func + ": glError " + error);
            throw new RuntimeException(func + ": glError " + error);
        }
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world. OpenGL doesn't use Java
     * arrays, but rather needs data in a format it can understand. Hence we use ByteBuffers.
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.thd_vertex);
        int gridShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);

        mGlProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mGlProgram, vertexShader);
        GLES20.glAttachShader(mGlProgram, gridShader);
        GLES20.glLinkProgram(mGlProgram);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        float mObjectDistance = 12f;
        cube = new ModelObject(this, DATA.CUBE_COORDS, DATA.CUBE_COLORS, DATA.CUBE_NORMALS, 0, 0, -mObjectDistance, 1.0f, 1.0f, 1.0f);

        float mFloorDepth = 20f;
        floor = new ModelObject(this, DATA.FLOOR_COORDS, DATA.FLOOR_COLORS, DATA.FLOOR_NORMALS, 0, -mFloorDepth, 0, 1.0f, 1.0f, 1.0f);
        floor.setFloor(1f); // this is a floor

        // test pyramid
        pyramid = new CalebObject(this, CalebDATA.PYRAMID_COORDS, CalebDATA.PYRAMID_COLORS, CalebDATA.PYRAMID_NORMALS, 5.0f, 0, 6.0f);

        wall = new ModelObject(this, DATA.CUBE_COORDS, DATA.CUBE_COLORS, DATA.CUBE_NORMALS, 7.0f, 0.0f, 0.0f, 2.0f, 6.0f, 6.0f);

        checkGLError("onSurfaceCreated");
    }

    /**
     * Prepares OpenGL ES before we draw a frame.
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        GLES20.glUseProgram(mGlProgram);

        mViewProjectionParam = GLES20.glGetUniformLocation(mGlProgram, "u_VPMatrix");
        mLightPosParam = GLES20.glGetUniformLocation(mGlProgram, "u_LightPos");
        mModelViewParam = GLES20.glGetUniformLocation(mGlProgram, "u_MVMatrix");
        mModelParam = GLES20.glGetUniformLocation(mGlProgram, "u_Model");
        mIsFloorParam = GLES20.glGetUniformLocation(mGlProgram, "u_IsFloor");

        // Build the Model part of the ModelView matrix.
        cube.rotate(TIME_DELTA);
        pyramid.rotate(TIME_DELTA);

        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(mHeadView, 0);

        checkGLError("onReadyToDraw");
    }

    /**
     * Draws a frame for an eye. The transformation for that eye (from the camera) is passed in as
     * a parameter.
     * @param transform The transformations to apply to render this eye.
     */
    @Override
    public void onDrawEye(EyeTransform transform) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mPositionParam = GLES20.glGetAttribLocation(mGlProgram, "a_Position");
        mNormalParam = GLES20.glGetAttribLocation(mGlProgram, "a_Normal");
        mColorParam = GLES20.glGetAttribLocation(mGlProgram, "a_Color");

        GLES20.glEnableVertexAttribArray(mPositionParam);
        GLES20.glEnableVertexAttribArray(mNormalParam);
        GLES20.glEnableVertexAttribArray(mColorParam);
        checkGLError("mColorParam");

        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(mView, 0, transform.getEyeView(), 0, mCamera, 0);

        // Set the position of the light
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mView, 0, mLightPosInWorldSpace, 0);
        GLES20.glUniform3f(mLightPosParam, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1],
                mLightPosInEyeSpace[2]);

        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        //Matrix.multiplyMM(mModelView, 0, mView, 0, mModelCube, 0);
        Matrix.multiplyMM(mViewProjection, 0, transform.getPerspective(), 0, mView, 0);
        cube.draw(mView, mViewProjection);

        // Set mModelView for the floor, so we draw floor in the correct location
        Matrix.multiplyMM(mViewProjection, 0, transform.getPerspective(), 0, mView, 0);
        floor.draw(mView, mViewProjection);

        // ------------Caleb's tests----------------//
        Matrix.multiplyMM(mViewProjection, 0, transform.getPerspective(), 0, mView, 0);
        pyramid.draw(mView,mViewProjection);

        Matrix.multiplyMM(mViewProjection, 0, transform.getPerspective(), 0, mView, 0);
        wall.draw(mView, mViewProjection);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    /**
     * Check if user is looking at object by calculating where the object is in eye-space.
     * @return looking at object
     */
    private boolean isLookingAtObject() {
        float[] initVec = {0, 0, 0, 1.0f};
        float[] objPositionVec = new float[4];

        // Convert object space to camera space. Use the headView from onNewFrame.
        //Matrix.multiplyMM(mModelView, 0, mHeadView, 0, mModelCube, 0);
        Matrix.multiplyMV(objPositionVec, 0, mModelView, 0, initVec, 0);

        float pitch = (float)Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float)Math.atan2(objPositionVec[0], -objPositionVec[2]);

        Log.i(TAG, "Object position: X: " + objPositionVec[0]
                + "  Y: " + objPositionVec[1] + " Z: " + objPositionVec[2]);
        Log.i(TAG, "Object Pitch: " + pitch +"  Yaw: " + yaw);

        return (Math.abs(pitch) < PITCH_LIMIT) && (Math.abs(yaw) < YAW_LIMIT);
    }
}

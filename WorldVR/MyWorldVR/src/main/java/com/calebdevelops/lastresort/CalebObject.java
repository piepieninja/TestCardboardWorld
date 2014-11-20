package com.calebdevelops.rajawalivrcardboard;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.calebdevelops.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by CXA0589 on 7/3/2014.
 */
public class CalebObject {

    private FloatBuffer mVertices;
    private FloatBuffer mColors;
    private FloatBuffer mNormals;

    public static final int COORDS_PER_VERTEX = 3;

    private int mGlProgram;

    private float[] mModel;
    private float[] mModelView;


    public CalebObject(MyGLRenderer renderer, float[] vertices, float[] colors, float[] normals, float x, float y, float z){

        mModel = new float[16];
        mModelView = new float[16];

        Matrix.setIdentityM(mModel, 0);
        Matrix.translateM(mModel, 0, x, y, z);

        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertices.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        mVertices = bbVertices.asFloatBuffer();
        mVertices.put(vertices);
        mVertices.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(colors.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        mColors = bbColors.asFloatBuffer();
        mColors.put(colors);
        mColors.position(0);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(normals.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        mNormals = bbNormals.asFloatBuffer();
        mNormals.put(normals);
        mNormals.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = renderer.loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.thd_vertex);
        int fragmentShader = renderer.loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);

        mGlProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mGlProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mGlProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mGlProgram);
    }

    public void rotate(float angle){
        Matrix.rotateM(mModel, 0, angle, 2.0f, 2.0f, 0.0f);
    }

    public void draw(float[] mView, float[] mViewProjection){
        GLES20.glUseProgram(mGlProgram);

        int mViewProjectionParam = GLES20.glGetUniformLocation(mGlProgram, "u_VPMatrix");
        int mModelViewParam = GLES20.glGetUniformLocation(mGlProgram, "u_MVMatrix");
        int mModelParam = GLES20.glGetUniformLocation(mGlProgram, "u_Model");
        int mIsFloorParam = GLES20.glGetUniformLocation(mGlProgram, "u_IsFloor");

        int mPositionParam = GLES20.glGetAttribLocation(mGlProgram, "a_Position");
        int mNormalParam = GLES20.glGetAttribLocation(mGlProgram, "a_Normal");
        int mColorParam = GLES20.glGetAttribLocation(mGlProgram, "a_Color");

        GLES20.glEnableVertexAttribArray(mPositionParam);
        GLES20.glEnableVertexAttribArray(mNormalParam);
        GLES20.glEnableVertexAttribArray(mColorParam);
        MyGLRenderer.checkGLError("mColorParam");

        // This is not the floor!
        GLES20.glUniform1f(mIsFloorParam, 0f);

        Matrix.multiplyMM(mModelView, 0, mView, 0, mModel, 0);
        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(mModelParam, 1, false, mModel, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(mModelViewParam, 1, false, mModelView, 0);

        // Set the position of the cube
        GLES20.glVertexAttribPointer(mPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mVertices);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(mViewProjectionParam, 1, false, mViewProjection, 0);

        // Set the normal positions of the cube, again for shading
        GLES20.glVertexAttribPointer(mNormalParam, 3, GLES20.GL_FLOAT, false, 0, mNormals);


        GLES20.glVertexAttribPointer(mColorParam, 4, GLES20.GL_FLOAT, false, 0, mColors);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, CalebDATA.PYRAMID_COORDS.length/3);
        MyGLRenderer.checkGLError("Drawing Caleb's Stuff");
    }

}

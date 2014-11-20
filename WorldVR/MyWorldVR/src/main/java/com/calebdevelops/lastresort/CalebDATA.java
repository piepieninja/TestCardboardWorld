package com.calebdevelops.rajawalivrcardboard;

/**
 * Contains Various Data
 */
public final class CalebDATA {

    public static final float[] PYRAMID_COORDS = new float[]{
            // back triangle
            -1.0f, -1.0f, -1.0f, // back-left
             0.0f,  1.0f,  0.0f, // top
             1.0f, -1.0f, -1.0f, // back-right

            // left triangle
            -1.0f, -1.0f, -1.0f, // back-left
            -1.0f, -1.0f,  1.0f, // front-left
             0.0f,  1.0f,  0.0f, // top

            // front triangle
             0.0f,  1.0f,  0.0f, // top
            -1.0f, -1.0f,  1.0f, // front-left
             1.0f, -1.0f,  1.0f, // front-right

            // right triangle
             0.0f,  1.0f,  0.0f, // top
             1.0f, -1.0f,  1.0f, // front-right
             1.0f, -1.0f, -1.0f, // back-right

            // bottom 1/2 triangle
            -1.0f, -1.0f, -1.0f, // back left
             1.0f, -1.0f, -1.0f, // back-right
             1.0f, -1.0f,  1.0f, // front-right

            // bottom 2/2 triangle
            -1.0f, -1.0f, -1.0f, // back left
             1.0f, -1.0f,  1.0f, // front-right
            -1.0f, -1.0f,  1.0f, // front-left
    };

    public static final float[] PYRAMID_COLORS = new float[] {
            // back, green
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,

            // left, blue
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,

            // front, green
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,

            // left, blue
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,

            // bottom, red
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,

            // bottom, red
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
    };

    public static final float[] PYRAMID_NORMALS = new float[] {
            // back triangle
             0.0f,  1.0f, -1.0f,
             0.0f,  1.0f, -1.0f,
             0.0f,  1.0f, -1.0f,

            // left triangle
            -1.0f,  1.0f,  0.0f,
            -1.0f,  1.0f,  0.0f,
            -1.0f,  1.0f,  0.0f,

            // front triangle
             0.0f,  1.0f,  1.0f,
             0.0f,  1.0f,  1.0f,
             0.0f,  1.0f,  1.0f,

            // right triangle
             1.0f,  1.0f,  0.0f,
             1.0f,  1.0f,  0.0f,
             1.0f,  1.0f,  0.0f,

            // bottom 1/2 triangle
             0.0f, -1.0f,  0.0f,
             0.0f, -1.0f,  0.0f,
             0.0f, -1.0f,  0.0f,

            // bottom 2/2 triangle
             0.0f, -1.0f,  0.0f,
             0.0f, -1.0f,  0.0f,
             0.0f, -1.0f,  0.0f,
    };
}

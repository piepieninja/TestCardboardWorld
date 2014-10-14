/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.calebdevelops;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import com.google.vrtoolkit.cardboard.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A Cardboard sample application.
 */
public class MainActivity extends CardboardActivity{

    private static final String TAG = "MainActivity";

    private Vibrator mVibrator;

    private CardboardOverlayView mOverlayView;


    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(new MyGLRenderer(this));
        setCardboardView(cardboardView);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mOverlayView = (CardboardOverlayView) findViewById(R.id.overlay);
        mOverlayView.show3DToast("Pull the magnet when you find an object.");
    }

    /**
     * Converts a raw text file into a string.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return
     */
    public String readRawTextFile(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Increment the score, hide the object, and give feedback if the user pulls the magnet while
     * looking at the object. Otherwise, remind the user what to do.
     */
    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        /*
        if (isLookingAtObject()) {
            mScore++;
            mOverlayView.show3DToast("Found it! Look around for another one.\nScore = " + mScore);
            hideObject();
        } else {
            mOverlayView.show3DToast("Look around to find the object!");
        }
        */

        // Always give user feedback
        mVibrator.vibrate(50);
    }
}

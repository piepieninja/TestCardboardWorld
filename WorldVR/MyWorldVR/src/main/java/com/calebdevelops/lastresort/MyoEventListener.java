package com.calebdevelops.rajawalivrcardboard;

/**
 * Created by clawges on 10/25/14.
 */

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import android.content.Intent;


public class MyoEventListener implements DeviceListener {

    

    @Override
    public void onPair(Myo myo, long l) {
        System.out.println("paired!");
    }

    @Override
    public void onConnect (Myo myo, long timestamp) {
        System.out.println("connected to myo!");

    }

    @Override
    public void onDisconnect(Myo myo, long l) {
        System.out.println("disconnected!");

    }

    @Override
    public void onArmRecognized(Myo myo, long l, Arm arm, XDirection xDirection) {
        System.out.println("arm recognized!");

    }

    @Override
    public void onArmLost(Myo myo, long l) {
        System.out.println("arm lost!");

    }

    @Override
    public void onPose(Myo myo, long l, Pose pose) {
        System.out.println("new pose!");
        if (pose == Pose.FINGERS_SPREAD) {
            //RajawaliVRExampleRenderer.bamObjects();
        }
    }

    @Override
    public void onOrientationData(Myo myo, long l, Quaternion quaternion) {

    }

    @Override
    public void onAccelerometerData(Myo myo, long l, Vector3 vector3) {

    }

    @Override
    public void onGyroscopeData(Myo myo, long l, Vector3 vector3) {

    }

    @Override
    public void onRssi(Myo myo, long l, int i) {

    }

}

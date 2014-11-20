package com.calebdevelops.rajawalivrcardboard;

/**
 * Created by clawges on 10/25/14.
 */

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//import com.calebdevelops.rawfinally.R;
import com.calebdevelops.R;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

public class ScanForMyoActivity extends Activity {

    private static final int CONNECTED_MYO = 11;
    private static final String TAG = "ScanForMyoActivity";
    private MyoEventListener mMyoListener = new MyoEventListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.scan_layout);

     Hub hub = Hub.getInstance();
     if (!hub.init(this, getPackageName())) {
         Log.e(TAG, "Could not initialize hub");
         finish();
         return;
     }

     hub.addListener(mMyoListener);
    }

    public void scanForDevice(View view) {
        System.out.println("Scanning for Myo");
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void enterWorld(View view) {
        System.out.println("Entering world");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}

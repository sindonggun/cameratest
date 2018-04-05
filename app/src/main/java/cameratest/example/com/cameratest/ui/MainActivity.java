package cameratest.example.com.cameratest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cameratest.example.com.cameratest.R;
import cameratest.example.com.cameratest.ui.CameraFragment;
import cameratest.example.com.cameratest.util.ActivityUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CameraFragment cameraFragment =
                (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (cameraFragment == null) {
            cameraFragment = CameraFragment.newInstance();

            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    cameraFragment,
                    R.id.fragment_container);
        }
    }
}

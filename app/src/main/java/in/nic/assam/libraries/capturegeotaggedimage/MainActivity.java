package in.nic.assam.libraries.capturegeotaggedimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CONTROL_ACTIVITY_REQUEST_CODE = 3434;
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureButton = findViewById(R.id.capture_btn);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CameraControlActivity.class);
                startActivityForResult(i,CAMERA_CONTROL_ACTIVITY_REQUEST_CODE);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (CAMERA_CONTROL_ACTIVITY_REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
            }
        }
    }
}
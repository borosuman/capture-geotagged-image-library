package in.nic.assam.libraries.capturegeotaggedimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CONTROL_ACTIVITY_REQUEST_CODE = 3434;

    private ImageView previewImageView;
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewImageView = findViewById(R.id.preview_iv);
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
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("some_key");
                    if(getIntent().hasExtra("byteArray")) {
                        Bitmap b = BitmapFactory.decodeByteArray(
                                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
                        previewImageView.setImageBitmap(b);
                    }

                    Toast.makeText(MainActivity.this, "Hey this is the returned value"+returnValue, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
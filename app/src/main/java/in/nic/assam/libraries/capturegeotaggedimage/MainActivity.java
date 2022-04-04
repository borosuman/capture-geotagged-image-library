package in.nic.assam.libraries.capturegeotaggedimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import in.nic.assam.libraries.capturegeotaggedimage.image.GeoTaggedImage;
import in.nic.assam.libraries.capturegeotaggedimage.image.ImageMetaData;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CONTROL_ACTIVITY_REQUEST_CODE = 3434;
    private static final String TAG = MainActivity.class.getSimpleName();

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

                    ImageMetaData imageMetaData;
                    imageMetaData = data.getParcelableExtra("captured_image");

                    GeoTaggedImage geoTaggedImage = new GeoTaggedImage.Builder()
                            .withContext(this)
                            .withImageMetaData(imageMetaData)
                            .withTextColor(Color.YELLOW)
                            .withTextSize(55)
                            .withGravity(0)
                            .build();

                    Bitmap processedBitmap = geoTaggedImage.processGeoTaggedImage();
                    previewImageView.setImageBitmap(processedBitmap);
                }
                break;
            }
        }
    }
}
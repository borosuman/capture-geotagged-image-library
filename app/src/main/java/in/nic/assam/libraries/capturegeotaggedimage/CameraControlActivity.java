package in.nic.assam.libraries.capturegeotaggedimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import in.nic.assam.libraries.capturegeotaggedimage.permission.PermissionUtils;

public class CameraControlActivity extends AppCompatActivity implements PermissionUtils.PermissionResultCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 3874;

    private ImageView imageView;

    private ArrayList<String> permissions = new ArrayList<>();
    private PermissionUtils permissionUtils;
    private boolean isPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_control);
        imageView = findViewById(R.id.iv);
        initPermissionUtils();
    }

    private void initPermissionUtils() {
        permissionUtils = new PermissionUtils(CameraControlActivity.this);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionUtils.check_permission(permissions, "This app requires camera and location permission", 1);
    }
    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
        isPermissionGranted = true;
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
        getLastLocation();

    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
        finish();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(!isPermissionGranted){
            permissionUtils.check_permission(permissions, "This app requires camera and location permission", 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");


                    Canvas canvas = new Canvas(selectedImage);
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setSubpixelText(false);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setARGB(255, 255, 255, 255);
                    paint.setFakeBoldText(true);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(8);
                    canvas.drawText("Coordinates is: "+latitude+", "+longitude, 10, 50, paint);
                    imageView.setImageBitmap(selectedImage);
                }
            }
        }
    }
}
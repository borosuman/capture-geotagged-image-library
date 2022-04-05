package in.nic.assam.libraries.capturegeotaggedimagelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import in.nic.assam.libraries.capturegeotaggedimagelibrary.utils.PermissionUtils;

public class CameraControlActivity extends AppCompatActivity implements PermissionUtils.PermissionResultCallback {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final String TAG = CameraControlActivity.class.getSimpleName();
    private final ArrayList<String> permissionsList = new ArrayList<>();
    private PermissionUtils permissionUtils;
    private boolean isPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_control);
        initPermissionUtils();
    }

    private void initPermissionUtils() {
        permissionUtils = new PermissionUtils(CameraControlActivity.this);
        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.CAMERA);
        permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionUtils.check_permission(permissionsList, "This app requires camera and location permission", 1);
    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
        isPermissionGranted = true;
        Toast.makeText(CameraControlActivity.this, "Permission granted ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
        Toast.makeText(CameraControlActivity.this, "Permission partially granted ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
        Toast.makeText(CameraControlActivity.this, "Permission denied ", Toast.LENGTH_SHORT).show();

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    sendDataToCallerActivity();
                    Toast.makeText(this, "Picture was taken!", Toast.LENGTH_SHORT).show();

                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendDataToCallerActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("data","test");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }



}
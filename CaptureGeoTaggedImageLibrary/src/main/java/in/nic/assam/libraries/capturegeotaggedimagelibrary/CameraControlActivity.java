package in.nic.assam.libraries.capturegeotaggedimagelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import in.nic.assam.libraries.capturegeotaggedimagelibrary.image.ImageMetaData;
import in.nic.assam.libraries.capturegeotaggedimagelibrary.utils.LocationUtils;
import in.nic.assam.libraries.capturegeotaggedimagelibrary.utils.PermissionUtils;


public class CameraControlActivity extends AppCompatActivity implements PermissionUtils.PermissionResultCallback, LocationUtils.LocationResultCallback {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final String TAG = CameraControlActivity.class.getSimpleName();
    public final String APP_TAG = "CaptureGeoTaggedImage";
    public String photoFileName = "photo.jpg";
    File photoFile;
    private final ArrayList<String> permissionsList = new ArrayList<>();
    private PermissionUtils permissionUtils;
    private boolean isPermissionGranted;
    private Location location;
    private double latitude;
    private double longitude;
    private LocationUtils locationUtils;

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
        Toast.makeText(CameraControlActivity.this, "Permisssion granted ", Toast.LENGTH_SHORT).show();

        locationUtils = new LocationUtils(CameraControlActivity.this);
        locationUtils.getLocation();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    ImageMetaData image = new ImageMetaData(photoFile.getAbsolutePath(),location);
                    sendDataToCallerActivity(image);
                    Toast.makeText(this, "Picture was taken!", Toast.LENGTH_SHORT).show();

                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendDataToCallerActivity(ImageMetaData image) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("captured_image",image);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onLocationResult(Location location) {
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(CameraControlActivity.this, "Location retrieved: " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
        if (isPermissionGranted) {
            launchCamera();
        }
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        //    Uri fileProvider = FileProvider.getUriForFile(CameraControlActivity.this, "in.nic.assam.libraries.capturegeotaggedimage.fileprovider", photoFile);
        Uri fileProvider = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                getApplicationContext().getPackageName() + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
}
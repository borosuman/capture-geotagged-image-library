# capture-geotagged-image-library
An Android library to capture geotagged image with the help of camera.

## Step 1: Add the Library in app level build.gradle file
```
implementation 'com.github.borosuman:capture-geotagged-image-library:1.0.1'
```
## Step 2: Call the CameraControlActivity.class from your CameraControlActivity
```
   Intent i = new Intent(MainActivity.this, CameraControlActivity.class);
   startActivityForResult(i,CAMERA_CONTROL_ACTIVITY_REQUEST_CODE);
```

## Step 3: Obtain the captured geotagged image in onActivityResult function
```
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
 ```
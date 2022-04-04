package in.nic.assam.libraries.capturegeotaggedimage.image;

import android.content.Context;
import android.graphics.Bitmap;

import in.nic.assam.libraries.capturegeotaggedimage.utils.ImageUtils;

public class GeoTaggedImage {

    private Context context;
    private ImageMetaData imageMetaData;
    private int textColor;
    private int textSize;
    private int gravity;

    public GeoTaggedImage(Context context, ImageMetaData metaData, int color, int size, int gravity ) {
        this.context = context;
        this.imageMetaData = metaData;
        this.textColor = color;
        this.textSize = size;
        this.gravity = gravity;
    }

    public GeoTaggedImage() {

    }

    public static class Builder {
        Context context;
        ImageMetaData imageMetaData;
        int textColor;
        int textSize;
        int gravity;

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withImageMetaData(ImageMetaData metaData) {
            this.imageMetaData = metaData;
            return this;
        }
        public Builder withTextColor(int color) {
            this.textColor = color;
            return this;
        }
        public Builder withTextSize(int size) {
            this.textSize = size;
            return this;
        }
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }
        public GeoTaggedImage build() {
            return new GeoTaggedImage(context,imageMetaData,textColor,textSize,gravity);
        }
    }

    public Bitmap processGeoTaggedImage() {
        Bitmap processedBitmap;
        processedBitmap = ImageUtils.rotateBitmapOrientation(imageMetaData.getFilePath());
        processedBitmap = ImageUtils.drawTextToBitmap(context, processedBitmap, "Coordinates: " + imageMetaData.getLocation().getLatitude() + ", " + imageMetaData.getLocation().getLongitude(),textColor,textSize);
        return processedBitmap;
    }
}

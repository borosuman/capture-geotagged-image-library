package in.nic.assam.libraries.capturegeotaggedimagelibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.IOException;

public class ImageUtils {
    public static Bitmap drawTextToBitmap(Context gContext,
                                          Bitmap bitmap,
                                          String gText, int color, int size) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(color);
        // text size in pixels
        paint.setTextSize((int) (size * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, color);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
     /*   int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;*/

        int horizontalSpacing = 24;
        int verticalSpacing = 36;
        int x = horizontalSpacing;//(bitmap.getWidth() - bounds.width()) / 2;
        int y = bitmap.getHeight() - verticalSpacing;//(bitmap.getHeight() + bounds.height()) /

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

  /*  public static Bitmap processGeoTaggedImage(Context context, CapturedImageMetaData image) {
        Bitmap processedBitmap;
        processedBitmap = ImageUtils.rotateBitmapOrientation(image.getFilePath());
        processedBitmap = ImageUtils.drawTextToBitmap(context, processedBitmap, "Coordinates: " + image.getLocation().getLatitude() + ", " + image.getLocation().getLongitude());
        return processedBitmap;
    }*/
}

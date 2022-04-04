package in.nic.assam.libraries.capturegeotaggedimagelibrary.image;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageMetaData implements Parcelable {
    public static final Creator<ImageMetaData> CREATOR = new Creator<ImageMetaData>() {
        @Override
        public ImageMetaData createFromParcel(Parcel source) {
            return new ImageMetaData(source);
        }

        @Override
        public ImageMetaData[] newArray(int size) {
            return new ImageMetaData[size];
        }
    };
    private String filePath;
    private Location location;

    public ImageMetaData() {

    }

    public ImageMetaData(String path, Location location) {
        this.filePath = path;
        this.location = location;
    }
    protected ImageMetaData(Parcel in) {
        this.filePath = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeParcelable(this.location, flags);
    }

    public void readFromParcel(Parcel source) {
        this.filePath = source.readString();
        this.location = source.readParcelable(Location.class.getClassLoader());
    }
}

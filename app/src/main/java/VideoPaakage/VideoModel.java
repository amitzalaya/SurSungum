package VideoPaakage;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VideoModel implements Parcelable {

    private String id;
    private String title;
    private String duration;
    private String thumbanail;

    public VideoModel(Uri id, String title, int duration, String thumbanail) {
        this.id = String.valueOf(id);
        this.title = title;
        this.duration = String.valueOf(duration);
        this.thumbanail = String.valueOf(thumbanail);
    }

    protected VideoModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        duration = in.readString();
        thumbanail = in.readString();
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbanail() {
        return thumbanail;
    }

    public void setThumbanail(String thumbanail) {
        this.thumbanail = thumbanail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeString(thumbanail);
    }
}

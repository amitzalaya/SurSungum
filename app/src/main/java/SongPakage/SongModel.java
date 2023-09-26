package SongPakage;

import android.net.Uri;

import java.io.Serializable;

public class SongModel implements Serializable {
    private String path;
    private String title;
    private String duration;
    private String titleimg;

    public SongModel(Uri path, String title, String duration, Uri titleimg) {
        this.path = String.valueOf((path));
        this.title = title;
        this.duration = duration;
        this.titleimg = String.valueOf(titleimg);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getTitleimg() {
        return titleimg;
    }

    public void setTitleimg(String titleimg) {
        this.titleimg =titleimg;
}


}

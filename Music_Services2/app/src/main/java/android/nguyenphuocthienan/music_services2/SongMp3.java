package android.nguyenphuocthienan.music_services2;

import java.io.Serializable;

public class SongMp3 implements Serializable {
    private String title;
    private String singer;
    private int image; // vì lấy ảnh từ drawable
    private int resource;

    public SongMp3() {
    }

    public SongMp3(String title, String singer, int image, int resource) {
        this.title = title;
        this.singer = singer;
        this.image = image;
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}

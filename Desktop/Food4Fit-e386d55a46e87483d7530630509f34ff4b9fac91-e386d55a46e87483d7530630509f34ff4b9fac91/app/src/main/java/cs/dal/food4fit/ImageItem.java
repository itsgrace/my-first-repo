package cs.dal.food4fit;

import android.graphics.Bitmap;

/**
 * Created by graceliu on 2017-11-19.
 */

public class ImageItem {
    private Bitmap image;
    private String title;
    private int imageID;

    public ImageItem(Bitmap image, String title, int imageID) {
        super();
        this.image = image;
        this.title = title;
        this.imageID = imageID;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
package model;

public class GameListCurrentModel {

    String gtitle;
    String gdev;
    String gimage;
    String gdes;

    public GameListCurrentModel() {
    }

    public GameListCurrentModel(String gtitle, String gdev, String gimage, String gdes) {
        this.gtitle = gtitle;
        this.gdev = gdev;
        this.gimage = gimage;
        this.gdes = gdes;
    }

    public String getGtitle() {
        return gtitle;
    }

    public void setGtitle(String gtitle) {
        this.gtitle = gtitle;
    }

    public String getGdev() {
        return gdev;
    }

    public void setGdev(String gdev) {
        this.gdev = gdev;
    }

    public String getGimage() {
        return gimage;
    }

    public void setGimage(String gimage) {
        this.gimage = gimage;
    }

    public String getGdes() {
        return gdes;
    }

    public void setGdes(String gdes) {
        this.gdes = gdes;
    }
}

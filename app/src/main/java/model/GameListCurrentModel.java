package model;

public class GameListCurrentModel {

    String gtitle;
    String gdev;
    String gimage;
    String gdes;

    public GameListCurrentModel() {
    }

    /**
     * This is the constructor for model game list current
     * @param gtitle
     * @param gdev
     * @param gimage
     * @param gdes
     */
    public GameListCurrentModel(String gtitle, String gdev, String gimage, String gdes) {
        this.gtitle = gtitle;
        this.gdev = gdev;
        this.gimage = gimage;
        this.gdes = gdes;
    }

    /**
     * gets gtitle
     * @return
     */
    public String getGtitle() {
        return gtitle;
    }

    /**
     * set gtitle
     * @param gtitle
     */
    public void setGtitle(String gtitle) {
        this.gtitle = gtitle;
    }

    /**
     * gets the gdev
     * @return
     */
    public String getGdev() {
        return gdev;
    }

    /**
     * sets gdev
     * @param gdev
     */
    public void setGdev(String gdev) {
        this.gdev = gdev;
    }

    /**
     * getgimage
     * @return
     */
    public String getGimage() {
        return gimage;
    }

    /**
     * gimage sets
     * @param gimage
     */
    public void setGimage(String gimage) {
        this.gimage = gimage;
    }

    /**
     * get gdes
     * @return
     */
    public String getGdes() {
        return gdes;
    }

    /**
     * setsgdes
     * @param gdes
     */
    public void setGdes(String gdes) {
        this.gdes = gdes;
    }
}

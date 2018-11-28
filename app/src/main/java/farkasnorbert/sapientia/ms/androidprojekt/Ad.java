package farkasnorbert.sapientia.ms.androidprojekt;

import android.net.Uri;

import java.util.ArrayList;

public class Ad {
    private String title;
    private String sdesc;
    private String ldesc;
    private String phone;
    private String location;
    private ArrayList<Uri> images;

    public Ad(String title, String sdesc, String ldesc, String phone, String location) {
        this.title = title;
        this.sdesc = sdesc;
        this.ldesc = ldesc;
        this.phone = phone;
        this.location = location;
        this.images = new ArrayList<Uri>();
    }

    public Ad() {
        this.images = new ArrayList<Uri>();
    }

    public String getTitle() {
        return title;
    }

    public String getSdesc() {
        return sdesc;
    }

    public String getLdesc() {
        return ldesc;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSdesc(String sdesc) {
        this.sdesc = sdesc;
    }

    public void setLdesc(String ldesc) {
        this.ldesc = ldesc;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }

    public int getImagesSize(){
        return this.images.size();
    }

    public Uri getImg(int index){
        return this.images.get(index);
    }

    public void addImg(Uri img){
        this.images.add(img);
    }

    public void deleteImg(Uri o){
        this.images.remove(o);
    }

    public void deleteAllImgs(){
        this.images.clear();
    }
}

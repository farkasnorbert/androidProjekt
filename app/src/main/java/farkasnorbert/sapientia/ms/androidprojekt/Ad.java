package farkasnorbert.sapientia.ms.androidprojekt;

import android.net.Uri;

import java.util.ArrayList;

public class Ad {
    private String title;
    private String sdesc;
    private String ldesc;
    private String phone;
    private String location;
    private ArrayList<String> images;

    public Ad(String title, String sdesc, String ldesc, String phone, String location) {
        this.title = title;
        this.sdesc = sdesc;
        this.ldesc = ldesc;
        this.phone = phone;
        this.location = location;
        this.images = new ArrayList<String>();
    }

    public Ad(String title, String sdesc, String ldesc, String phone, String location, ArrayList<String> images) {
        this.title = title;
        this.sdesc = sdesc;
        this.ldesc = ldesc;
        this.phone = phone;
        this.location = location;
        this.images = images;
    }

    public Ad() {
        this.images = new ArrayList<String>();
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

    public ArrayList<String> getImages() {
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

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setImg(String img,int index){
        this.images.set(index,img);
    }

    public int getImagesSize(){
        return this.images.size();
    }

    public String getImg(int index){
        return this.images.get(index);
    }

    public void addImg(String img){
        this.images.add(img);
    }

    public void deleteImg(Uri o){
        this.images.remove(o);
    }

    public void deleteAllImgs(){
        this.images.clear();
    }
}

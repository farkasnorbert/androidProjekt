package farkasnorbert.sapientia.ms.androidprojekt.Modell;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ad implements Parcelable {
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

    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel in) {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };

    protected Ad(Parcel in) {
        title = in.readString();
        sdesc = in.readString();
        ldesc = in.readString();
        phone = in.readString();
        location = in.readString();
        images = in.createStringArrayList();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(sdesc);
        dest.writeString(ldesc);
        dest.writeString(phone);
        dest.writeString(location);
        dest.writeStringList(images);
    }
}

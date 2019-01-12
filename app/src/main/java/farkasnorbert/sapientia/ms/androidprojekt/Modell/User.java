package farkasnorbert.sapientia.ms.androidprojekt.Modell;

public class User {
    private String fName;
    private String lName;
    private String email;
    private String pPicture;
    private String address;
    private String phone;

    public User() {
    }

    public User(String fName, String lName, String email, String pPicture, String address,String phone) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.address = address;
        this.pPicture = pPicture;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getpPicture() {
        return pPicture;
    }

    public void setpPicture(String pPicture) {
        this.pPicture = pPicture;
    }
}

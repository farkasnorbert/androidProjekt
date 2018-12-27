package farkasnorbert.sapientia.ms.androidprojekt.Modell;

public class User {
    private String fName;
    private String lName;
    private String email;
    private String pPicture;
    private String adress;

    public User() {
    }

    public User(String fName, String lName, String email, String pPicture, String adress) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.adress = adress;
        this.pPicture = pPicture;
    }

    public String getAdress() {

        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

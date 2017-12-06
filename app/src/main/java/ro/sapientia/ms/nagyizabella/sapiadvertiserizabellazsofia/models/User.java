package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models;

/**
 * Created by Izabella on 2017-11-08.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String email;
    private String FirstName;
    private String LastName;
    private String PhoneNumbers;
    private String profilImage;
    //private String Password;
    //private String ConfirmPassword;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String email, String first, String last, String phone, String image) {
        this.email = email;
        this.FirstName = first;
        this.LastName = last;
        this.PhoneNumbers = phone;
        this.profilImage = image;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumbers() {
        return PhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        PhoneNumbers = phoneNumbers;
    }

    public String getProfilImage() {
        return profilImage;
    }

    public void setProfilImage(String image) {
        profilImage = image;
    }

    //public String getPassword() {
    //    return Password;
    //}

   // public void setPassword(String password) {
    //    Password = password;
    //}

    //public String getConfirmPassword() {
    //    return ConfirmPassword;
    //}

    //public void setConfirmPassword(String confirmPassword) {
    //    ConfirmPassword = confirmPassword;
    //}
}
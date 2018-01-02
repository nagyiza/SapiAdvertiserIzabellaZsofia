package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models;

/**
 * Created by Izabella on 2017-11-08.
 * Model of user data
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    /**
     * The user's email
     */
    private String email;
    /**
     * The user's first name
     */
    private String FirstName;
    /**
     * The user's last name
     */
    private String LastName;
    /**
     * The user's phone number
     */
    private String PhoneNumbers;
    /**
     * The user's profile image
     */
    private String profilImage;
    //private String Password;
    //private String ConfirmPassword;

    /**
     * Class constructor
     * @param email The user's email
     * @param first The user's first name
     * @param last The user's last name
     * @param phone The user's phone number
     * @param image The user's profile image
     */
    public User(String email, String first, String last, String phone, String image) {
        this.email = email;
        this.FirstName = first;
        this.LastName = last;
        this.PhoneNumbers = phone;
        this.profilImage = image;
    }


    /**
     * Get the user's email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's first name
     * @return
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * Set the user's first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    /**
     * Get the user's last name
     * @return
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * Set the user's last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /**
     * Get the user's phone number
     * @return
     */
    public String getPhoneNumbers() {
        return PhoneNumbers;
    }

    /**
     * Set the user's phone number
     * @param phoneNumbers
     */
    public void setPhoneNumbers(String phoneNumbers) {
        PhoneNumbers = phoneNumbers;
    }

    /**
     * Get the user's profile image
     * @return
     */
    public String getProfilImage() {
        return profilImage;
    }

    /**
     * Set the user's profile image
     * @param image
     */
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
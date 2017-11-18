package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models;

/**
 * Created by Izabella on 2017-11-08.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String FirstName;
    public String LastName;
    public String PhoneNumbers;
    public String Password;
    public String ConfirmPassword;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
/*
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    */

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, String firstName, String lastName, String phoneNumbers, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.PhoneNumbers = phoneNumbers;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
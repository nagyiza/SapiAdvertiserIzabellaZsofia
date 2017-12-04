package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters;

/**
 * Created by Zsófika on 04.12.2017.
 */
import android.content.Context;
import android.widget.Toast;

public class Helper {
    public static final String EMAIL = "Email";

    public static final String FIRST_NAME = "First Name";

    public static final String LAST_NAME = "Last Name";

    public static final String PHONE_NUMBER = "Phone Number";

    public static final String PASSWORD = "Password";

    public static final String CONFIRM_PASSWORD = "Confirm Password";

    public static final int SELECT_PICTURE = 2000;

    public static boolean isValidEmail(String email){
        if(email.contains("@")){
            return true;
        }
        return false;
    }

    public static void displayMessageToast(Context context, String displayMessage){
        Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show();
    }
}

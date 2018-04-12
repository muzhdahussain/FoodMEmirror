package app.foodme;

/**
 * Handles customer registration activities.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText name, phoneNum, email;
    String str_name, str_phoneNum, str_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.et_name);
        phoneNum = (EditText)findViewById(R.id.et_phoneNum);
        email = (EditText)findViewById(R.id.et_email);
    }

    public void onReg(View view){

        // Retrieves registration details from the customer
        str_name = name.getText().toString();
        str_phoneNum = phoneNum.getText().toString();
        str_email = email.getText().toString();
        String type = "register";

        // checks user input
        // Regex expression used from: http://www.sitepoint.com/forums/showthread.php?204268-Regex-for-letters-numbers-and-spaces
        if (str_name.matches("[a-z|A-Z|0-9|\\s]*") && str_phoneNum.matches("[a-z|A-Z|0-9|\\s]*")) {
            // Sends registration information to BackgroundWorker for processing
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, str_phoneNum, str_name, str_email);
        } else {
            Toast toast = Toast.makeText(Register.this, "WARNING: Please remove any special characters, and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}

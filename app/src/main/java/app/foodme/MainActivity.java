package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

/**
 * Starting screen for application.
 * Allows a customer to login with their phone number,
 * or they can choose to register in the system, or login as an employee.
 */

public class MainActivity extends AppCompatActivity {

    EditText  phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNum = (EditText)findViewById(R.id.et_phoneNum);
    }

    // Retrieves details entered by the customer and sends them for processing
    public void OnLogin(View view) {

        // retrieves phone number entered by the customer
        String str_phoneNum = phoneNum.getText().toString();
        String type = "cust_login";

        // checks user input
        // Regex expression used from: http://www.sitepoint.com/forums/showthread.php?204268-Regex-for-letters-numbers-and-spaces
        if (str_phoneNum.matches("[a-z|A-Z|0-9|\\s]*")) {
            // Sends customer login information to BackgroundWorker for processing
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, str_phoneNum);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "WARNING: Please remove any special characters, and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


    }

    // Initiates employee login activity
    public void Emp_Login(View view){

        startActivity(new Intent(this,EmpLogin.class));
    }

    // Initiates the customer registration activity
    public void OpenReg(View view){
        startActivity(new Intent(this,Register.class));
    }
}
package app.foodme;

/**
 * Handles customer registration activities.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

        // Sends registration information to BackgroundWorker for processing
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_phoneNum, str_name, str_email);
    }

    // Returns to login
    public void ReturnToLogin(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
}

package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    EditText  phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // userID as entered by the user
        phoneNum = (EditText)findViewById(R.id.et_phoneNum);
    }

    public void OnLogin(View view) {
        String str_phoneNum = phoneNum.getText().toString();
        String type = "login";

        // Sends login information to BackgroundWorker for processing
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_phoneNum);
    }

    public void Emp_Login(View view){

        startActivity(new Intent(this,Emp_Login.class));


    }


    // Starts the registration activity
    public void OpenReg(View view){
        startActivity(new Intent(this,Register.class));
    }
}

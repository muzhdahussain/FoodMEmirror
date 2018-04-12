package app.foodme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Handles the employee login activity.
 */
public class EmpLogin extends AppCompatActivity {

    EditText empSinET;
    String Sin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_login);

        empSinET = (EditText)findViewById(R.id.et_empSin);

    }

    public void LoginEmp(View view) {

        Sin = empSinET.getText().toString();
        String type = "emp_login";

        // checks user input
        // Regex expression used from: http://www.sitepoint.com/forums/showthread.php?204268-Regex-for-letters-numbers-and-spaces
        if (Sin.matches("[a-z|A-Z|0-9|\\s]*")) {
            // Sends login information to BackgroundWorker for processing
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, Sin);
        } else {
            Toast toast = Toast.makeText(EmpLogin.this, "WARNING: Please remove any special characters, and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
package app.foodme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

        // Sends login information to BackgroundWorker for processing
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, Sin);
    }
}
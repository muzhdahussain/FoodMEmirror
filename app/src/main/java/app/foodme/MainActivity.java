package app.foodme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    EditText  nameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // userID as entered by the user
        nameEt = (EditText)findViewById(R.id.nameEt);
    }

    public void OnLogin(View view) {
        String userID = nameEt.getText().toString();
        String type = "login";

        // Sends login information to BackgroundWorker for processing
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, userID);
    }
}

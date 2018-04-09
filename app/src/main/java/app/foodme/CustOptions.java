package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_options);
    }



    public void openCustomerMenu(View view){

        startActivity(new Intent(this,CustomerMenu.class));


    }

    public void openCustomerHistory(View view){

        // startActivity(new Intent(this,OrderHistory.class));


    }

    // Overrides default back button action to return to the main screen
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}

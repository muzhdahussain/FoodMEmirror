package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustomerOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customeroption);
    }


    public void openCustomerMenu(View view){

        startActivity(new Intent(this,CustomerMenu.class));


    }

    public void openCustomerHistory(View view){

        // startActivity(new Intent(this,OrderHistory.class));


    }
}

package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustOptions extends AppCompatActivity {


    String phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_options);
        phone_no = getIntent().getStringExtra("custPhoneNum");
    }




    public void openCustomerMenu(View view){

        startActivity(new Intent(this,CustomerMenu.class));


    }

    public void openCustomerHistory(View view){

        startActivity(new Intent(this,CustomerHistory.class));


    }
}

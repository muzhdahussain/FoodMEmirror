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
       // phone_no= getIntent().getExtras("s_phoneNum");

        phone_no = getIntent().getStringExtra("phone_no");
    }




    public void openCustomerMenu(View view){
        Intent i = new Intent(this, CustomerMenu.class);
        i.putExtra("s_phoneNum", phone_no);
        startActivity(i);
    }

    public void openCustomerHistory(View view){

        Intent i = new Intent(this,CustomerHistory.class);
        i.putExtra("s_phoneNum", phone_no);
        startActivity(i);
    }

    // Overrides default back button action to return to the main screen
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}

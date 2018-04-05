package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Handles the employee menu activities.
 */

public class EmpMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_menu);
    }

    public void ViewOrders(View view){

       // startActivity(new Intent(this,Orders.class));
    }

    public void ViewOrderHistory(View view){

       // startActivity(new Intent(this,OrderHistory.class));
    }
}

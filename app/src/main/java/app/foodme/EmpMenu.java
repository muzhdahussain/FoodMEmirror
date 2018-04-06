package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Handles the employee menu activities.
 */

public class EmpMenu extends AppCompatActivity {

    String empSin;
    int orderViewFlag; //1 for orders 2 for order history
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_menu);

        //gets the emp sin from the login
        empSin = getIntent().getStringExtra("EMP_SIN");
    }

    public void ViewOrders(View view){

        Intent i = new Intent(this,EmpOrders.class);
        i.putExtra("EMP_SIN", empSin);
        i.putExtra("ORDER_FLAG", 1);
        startActivity(i);
    }

    public void ViewOrderHistory(View view){

        Intent i = new Intent(this,EmpOrders.class);
        i.putExtra("EMP_SIN", empSin);
        i.putExtra("ORDER_FLAG", 2);
        startActivity(i);
    }
}

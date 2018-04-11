package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class CustomerRating extends AppCompatActivity {

    String custPhone;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rating);

        orderID = getIntent().getStringExtra("orderID");
        custPhone = getIntent().getStringExtra("custPhone");
    }

    // Handles the click of the rate order button
    public void submitRating(View view){

        RatingBar rb_foodRatingBar = findViewById(R.id.rb_food);
        RatingBar rb_deliveryRatingBar = findViewById(R.id.rb_delivery);
        EditText et_ratingNotes = findViewById(R.id.et_notes2);

        // Retrieves variables entered by the customer
        String foodRating = Integer.toString(rb_foodRatingBar.getNumStars());
        String deliveryRating = Integer.toString(rb_deliveryRatingBar.getNumStars());
        String ratingNotes = et_ratingNotes.getText().toString();

        // Submits data to the database
        //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //backgroundWorker.execute("submit_rating", custPhone, deliveryRating, ratingNotes, foodRating, orderID);
    }

}

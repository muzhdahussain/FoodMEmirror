package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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
    public void submitRating(View view) {

        RatingBar rb_foodRatingBar = findViewById(R.id.rb_food);
        RatingBar rb_deliveryRatingBar = findViewById(R.id.rb_delivery);
        EditText et_ratingNotes = findViewById(R.id.et_notes2);

        // Retrieves variables entered by the customer
        String foodRating = Float.toString(rb_foodRatingBar.getRating());
        String deliveryRating = Float.toString(rb_deliveryRatingBar.getRating());
        String ratingNotes = et_ratingNotes.getText().toString();

        // Checks user input
        // Regex expression used from: http://www.sitepoint.com/forums/showthread.php?204268-Regex-for-letters-numbers-and-spaces
        if (ratingNotes.matches("[a-z|A-Z|0-9|\\s]*")) {
            // Submits data to the database
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute("submit_rating", custPhone, deliveryRating, ratingNotes, foodRating, orderID);
        } else {
            Toast toast = Toast.makeText(CustomerRating.this, "WARNING: Please remove any special characters, and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}

package app.foodme;

import android.app.MediaRouteButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class EmpViewOrder extends AppCompatActivity {

    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    String orderID;
    String orderView;
    String http_json_url;

    String campus;
    String building;
    String roomNum;
    String custPhoneNum;
    String campusID;
    String notes;
    String vendor;
    String vendorBuilding;
    String vendorRoomNum;
    String itemName;
    String itemQuantity;
    String divider;

    TextView orderLocation;
    TextView phoneNum;
    TextView notesTV;
    TextView vendorInfo;
    TextView foodInfo;

    Button btnComplete;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_view_order);
        btnComplete = findViewById(R.id.buttonComplete);
        btnStart = findViewById(R.id.buttonStart);

        divider = " - ";
        orderID = getIntent().getStringExtra("ORDER_ID");
        orderView = getIntent().getStringExtra("ORDER_VIEW");
        http_json_url = "http://70.77.241.161:8080/get_order.php?order_id=" + orderID;


        //get orderID info about order from db

        JsonDataWebCall();

        //btnStart.setVisibility(View.VISIBLE);
        // btnComplete.setVisibility(View.VISIBLE);

    }


    // Sets up the web call to the database
    public void JsonDataWebCall() {

        jsonArrayRequest = new JsonArrayRequest(http_json_url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JsonParseDataAfterWebCall(response);

                        if (campusID.equals("1")) {
                            campus = "U of C";

                        } else if (campusID.equals("2")) {

                            campus = "MRU";
                        }

                        if (orderView.equals("2")){
                           btnStart.setVisibility(View.GONE);
                           btnComplete.setVisibility(View.GONE);
                        }

                        //Set the text fields in the activity

                        TextView orderLocationTV = (TextView) findViewById(R.id.orderLocation);
                        orderLocationTV.setText(campus + divider + building + divider + roomNum);

                        TextView phoneNum = (TextView) findViewById(R.id.custPhone);
                        phoneNum.setText(custPhoneNum);

                        TextView notesTV = (TextView) findViewById(R.id.notes);
                        notesTV.setText(notes);

                        TextView vendorInfo = (TextView) findViewById(R.id.vendorInfo);
                        vendorInfo.setText(vendor + divider + vendorBuilding + divider + vendorRoomNum);

                        TextView foodInfo = (TextView) findViewById(R.id.itemInfo);
                        foodInfo.setText(itemName + " X " + itemQuantity);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }


    // Parses the data retrieved from the database
    public void JsonParseDataAfterWebCall(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {


            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                // Sets the varibles of the order to be displayed


                building = json.getString("Building");
                roomNum = json.getString("Room_Num");
                campusID = json.getString("Campus_Campus_ID");

                custPhoneNum = json.getString("Customer_Phone_Num");
                notes = json.getString("Notes");

                vendor = json.getString("Name");
                vendorBuilding = json.getString("Vendor_Building");
                vendorRoomNum = json.getString("Vendor_Room_Num");
                itemName = json.getString("Menu_Item_Item_Name");
                itemQuantity = json.getString("Item_Quantity");




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public void completeOrder(View view) {

        // Updates the db when the order is marked as completed

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("order completed", orderID);

    }


    public void startOrder(View view) {

        // Updates the db when the order is marked as Started
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("order in progress", orderID);



    }


}

package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by muzhd on 4/9/2018.
 */

public class Order_PP extends AppCompatActivity {

    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;

    // String orderView;
    String http_json_url;

    String orderID;
    String campus;
    String building;
    String roomNum;
    String payment;
    String campusID;
    String notes;
    String phone_num;
    String orderStatus;

    String vendor;
    String itemName;
    String itemQuantity;
    String[] orderString;

    TextView text_OrderNum;
    TextView text_PaymentType;
    TextView text_Building;
    TextView text_RoomNum;
    TextView text_Campus;
    ListView orderList;

    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_popup);

        orderID = getIntent().getStringExtra("ORDER_ID");
        //orderView = getIntent().getStringExtra("ORDER_VIEW");
        http_json_url = "http://70.77.241.161:8080/get_order.php?order_id=" + orderID;

        //get orderID info about order from db
        JsonDataWebCall();
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

                        //Add menuItems to itemsAdapter
                        List<String> order_list = new ArrayList<String>(Arrays.asList(orderString));
                        itemsAdapter = new ArrayAdapter<String>(Order_PP.this, android.R.layout.simple_list_item_1, order_list);

                        orderList = (ListView) findViewById(R.id.orderList);
                        orderList.setAdapter(itemsAdapter);

                        //Set the text fields in the activity
                        text_OrderNum = (TextView) findViewById(R.id.textOrderNum);
                        text_OrderNum.setText("Order# " + orderID);

                        text_Campus = (TextView) findViewById(R.id.textCampus);
                        text_Campus.setText("Campus: " + campus);


                        text_Building = (TextView) findViewById(R.id.textBuilding);
                        text_Building.setText("Building: " + building);

                        text_RoomNum = (TextView) findViewById(R.id.textRoomNum);
                        text_RoomNum.setText("Room# : " + roomNum);

                        text_PaymentType = (TextView) findViewById(R.id.textPaymentType);
                        text_PaymentType.setText("Payment Method: " + payment);
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
        orderString = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                // Sets the varibles of the order to be displayed

                campusID = json.getString("Campus_Campus_ID");
                building = json.getString("Building");
                roomNum = json.getString("Room_Num");
                payment = json.getString("Payment_Type");
                phone_num = json.getString("Customer_Phone_Num");
                notes = json.getString("Notes");
                orderStatus = json.getString("Status");
                vendor = json.getString("Name");
                itemName = json.getString("Menu_Item_Item_Name");
                itemQuantity = json.getString("Item_Quantity");
                orderString[i] = vendor + " -" + itemName + " Q: " + itemQuantity;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Handles the click of the rate order button
    public void rateOrder(View view){
        if ((orderStatus.equals("3"))){
            Intent i = new Intent(this, CustomerRating.class);
            i.putExtra("orderID", orderID);
            i.putExtra("custPhone", phone_num);
            startActivity(i);
        }
        else {
            Toast.makeText(Order_PP.this, "Selected order is not available to be rated at this time.", Toast.LENGTH_SHORT).show();
        }
    }
}
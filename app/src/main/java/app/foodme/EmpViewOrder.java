package app.foodme;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


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
    String notes;
    List<EmpOrderInfo> orderInfo;
    String divider;
    ListView allOrders;
    String[] onlyOrderInfo;
    TextView orderLocation;
    Button btnComplete;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_view_order);
        btnComplete = findViewById(R.id.buttonComplete);
        btnStart = findViewById(R.id.buttonStart);
        orderInfo = new ArrayList<>();

        divider = " - ";
        orderID = getIntent().getStringExtra("ORDER_ID");
        orderView = getIntent().getStringExtra("ORDER_VIEW");
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

                        if (orderInfo.get(0).getCampusID().equals("1")) {
                            campus = "U of C";

                        } else if (orderInfo.get(0).getCampusID().equals("2")) {

                            campus = "MRU";
                        }

                        if (orderView.equals("2")){
                           btnStart.setVisibility(View.GONE);
                           btnComplete.setVisibility(View.GONE);
                        }

                        //Take this info from just the first element of the list as they are all the same
                        custPhoneNum = orderInfo.get(0).getCustPhoneNum();

                        notes = orderInfo.get(0).getNotes();
                        building = orderInfo.get(0).getBuilding();
                        roomNum = orderInfo.get(0).getRoomNum();

                        //Set the text fields in the activity
                        setListView();

                        TextView orderLocationTV = (TextView) findViewById(R.id.orderLocation);
                        orderLocationTV.setText("Deliver To: " + campus + divider + building + divider + roomNum);

                        TextView phoneNum = (TextView) findViewById(R.id.custPhone);
                        phoneNum.setText(custPhoneNum);

                        if (notes == null) {
                            TextView notesTV = (TextView) findViewById(R.id.notes);
                            notesTV.setText(notes);
                        }
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

                EmpOrderInfo order = new EmpOrderInfo();

                order.setBuilding(json.getString("Building"));
                order.setRoomNum(json.getString("Room_Num"));
                order.setCampusID(json.getString("Campus_Campus_ID"));
                order.setCustPhoneNum("Phone Number: " + json.getString("Customer_Phone_Num"));

                order.setNotes("Notes: " + json.getString("Notes"));
                order.setVendor(json.getString("Name"));
                order.setVendorBuilding(json.getString("Vendor_Building"));
                order.setVendorRoomNum(json.getString("Vendor_Room_Num"));
                order.setItemName(json.getString("Menu_Item_Item_Name"));
                order.setItemQuantity(json.getString("Item_Quantity"));

                order.setOrderVendorInfo(order.getVendor() + divider + order.getVendorBuilding() + divider + order.getVendorRoomNum() + System.lineSeparator() +
                        order.getItemName() + " X " + order.getItemQuantity() + System.lineSeparator());

                orderInfo.add(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setListView(){

        // Set the List view of the orders
        onlyOrderInfo = new String[orderInfo.size()];

        for (int counter = 0; counter < orderInfo.size(); counter++) {
            onlyOrderInfo[counter] = orderInfo.get(counter).getOrderVendorInfo();
        }

        allOrders = (ListView) findViewById(R.id.listView1);

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(EmpViewOrder.this,android.R.layout.simple_list_item_1, onlyOrderInfo);

        allOrders.setAdapter(adapter);
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

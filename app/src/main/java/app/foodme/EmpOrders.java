package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
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
import java.util.List;

public class EmpOrders extends AppCompatActivity {

    String databaseURL = "http://70.77.241.161:8080";
    List<Item> itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;

    String empSin;
    String orderView;
    String http_json_url = databaseURL + "/emp_orders.php";

    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    View ChildView;
    int GetItemPosition;
    ArrayList<String> itemIDs;
    String orderID;
    String ORDER_URL = "?type=orders&empSin=";
    String ORDER_HISTORY_URL = "?type=orderHistory&empSin=";

    // Represents the column name we want to retrieve to display from the query
    String JSON_NAME = "Order # ";
    // Represents the column name we want to retrieve for tracking from the query
    String JSON_ID = "Order_Num";
    // Represents if the customer is currently selecting menu items or not
    boolean itemSelection = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_orders);

        //get values passes from the two previous menus
        empSin = getIntent().getStringExtra("EMP_SIN");
        orderView = getIntent().getStringExtra("ORDER_FLAG");


        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView2);
        progressBar = findViewById(R.id.progressBar2);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        itemIDs = new ArrayList<>();


        if (orderView.equals("1")) {

            http_json_url += ORDER_URL;
            http_json_url += empSin;
        } else if (orderView.equals("2")) {

            http_json_url += ORDER_HISTORY_URL;
            http_json_url += empSin;
        }


        JsonDataWebCall();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(EmpOrders.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    orderID = (itemIDs.get(GetItemPosition));
                    Toast toast = Toast.makeText(EmpOrders.this, "Order " +  orderID + " Selected", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();

                }
                return false;
            }


            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    // Sets up the web call to the database
    public void JsonDataWebCall() {

        jsonArrayRequest = new JsonArrayRequest(http_json_url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        JsonParseDataAfterWebCall(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(EmpOrders.this, "No Results Found!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    // Parses the data retrieved from the database
    public void JsonParseDataAfterWebCall(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            // An item represents a tuple retrieved from the database
            Item GetDataAdapter2 = new Item();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                // Sets the tuple's name (what is displayed in the menu)
                GetDataAdapter2.setItemName(JSON_NAME + json.getString(JSON_ID));


                // Sets the tuple's identifier (what is stored for future retrieval)
                GetDataAdapter2.setItemID(json.getString(JSON_ID));
                itemIDs.add(json.getString(JSON_ID));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemList.add(GetDataAdapter2);
        }

        // Displays all retrieved tuple "names" as buttons in the menu
        recyclerViewadapter = new RecyclerViewCardViewAdapter(itemList, this);
        recyclerView.setAdapter(recyclerViewadapter);

    }

    //Check to see if employee correctly selected an order, and send them to new activity
    public void showOrder(View view) {

        if (orderID == null) {

            Toast toast = Toast.makeText(EmpOrders.this, "Please select an order.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {

            Intent i;
            i = new Intent(this, EmpViewOrder.class);
            i.putExtra("ORDER_VIEW", orderView);
            i.putExtra("ORDER_ID", orderID);
            startActivity(i);
        }
    }
}
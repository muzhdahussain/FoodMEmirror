package app.foodme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
//http://tekeye.uk/android/examples/ui/android-popup-window using
public class CustomerHistory extends AppCompatActivity {

    List<Item> itemList;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;

    String databaseURL = "http://70.77.241.161:8080";
    String HTTP_JSON_URL = databaseURL + "/customer_history.php?cust_phone=";

    // Represents the column name we want to retrieve for tracking from the query
    String phone_no;

    String orderID;
    // Represents the column name we want to retrieve to display from the query
    String GET_JSON_Order_Num = "Order_Num";
    String GET_JSON_App_or_Den = "App_or_Den";
    String GET_JSON_Payment_type= "Payment_Type";
    String GET_JSON_Building= "Building";
    String GET_JSON_Room_Num= "Room_Num";
    String GET_JSON_Status= "Status";
    String GET_JSON_Employee_SIN= "Employee_SIN";
    String GET_JSON_Campus_Campus_ID= "Campus_Campus_ID";
    String GET_JSON_Notes= "Notes";




    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cust_hist);

        phone_no= getIntent().getStringExtra("s_phoneNum");
        HTTP_JSON_URL= HTTP_JSON_URL+ phone_no;
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView2);
        progressBar = findViewById(R.id.progressBar2);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        itemIDs = new ArrayList<>();


        JSON_DATA_WEB_CALL();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(CustomerHistory.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    //TODO Add scrollable popup to display all order information
                    //itemID[i]= order number, i corresponds to its index on screen = GetItemPosition

                   // Toast.makeText(CustomerHistory.this, SubjectNames.get(GetItemPosition), Toast.LENGTH_LONG).show();
                    orderID = (itemIDs.get(GetItemPosition));
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

    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            // An item represents a tuple retrieved from the database
            Item GetDataAdapter2 = new Item();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setItemName("Order#: "+json.getString(GET_JSON_Order_Num));

                itemIDs.add(json.getString(GET_JSON_Order_Num));


            } catch (JSONException e) {

                e.printStackTrace();
            }
            itemList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(itemList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }

    public void showOrder(View view) {

        if (orderID == null) {

            Toast.makeText(CustomerHistory.this, "Select An Order", Toast.LENGTH_LONG).show();

        } else {

            Intent i;
            i = new Intent(this, Order_PP.class);
           // i.putExtra("ORDER_VIEW", orderView);
            i.putExtra("ORDER_ID", orderID);
            startActivity(i);
        }
    }
}


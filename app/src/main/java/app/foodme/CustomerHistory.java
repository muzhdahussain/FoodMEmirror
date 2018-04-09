package app.foodme;

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

public class CustomerHistory extends AppCompatActivity {

    List<Item> itemList;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;

    String databaseURL = "http://70.77.241.161:8080";
    String HTTP_JSON_URL = databaseURL + "/customer_history.php";

    // Represents the column name we want to retrieve for tracking from the query
    String phone_no;

    // Represents the column name we want to retrieve to display from the query
    String GET_JSON_FROM_SERVER_NAME1 = "Order_Num";
    String GET_JSON_FROM_SERVER_NAME2 = "Payment_Type";
    String GET_JSON_FROM_SERVER_NAME3 = "App_or_Den";


    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cust_hist);

        phone_no= getIntent().getStringExtra("custPhoneNum");


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

                   // Toast.makeText(CustomerHistory.this, SubjectNames.get(GetItemPosition), Toast.LENGTH_LONG).show();
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

                GetDataAdapter2.setItemName(json.getString(GET_JSON_FROM_SERVER_NAME1));

                itemIDs.add(json.getString(GET_JSON_FROM_SERVER_NAME1));


            } catch (JSONException e) {

                e.printStackTrace();
            }
            itemList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(itemList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }
}

    /////////////////////
    /*
    String databaseURL = "http://70.77.241.161:8080";
    List<Item> itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    ListView c_order_ListView;

    String phone_no;
    int orderView = 0;
    String http_json_url = databaseURL + "/customer_history.php";

    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemIDs;



    // Represents the column name we want to retrieve to display from the query
    String JSON_NAME = "Building";
    // Represents the column name we want to retrieve for tracking from the query
    String JSON_ID = "Order_Num";

    boolean itemSelection = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);
        c_order_ListView= (ListView) findViewById(R.id.recyclerView3);
        progressBar= (ProgressBar) findViewById(R.id.recyclerView4);
        new GetHttpResponse (CustomerHistory.this).execute();

    }
}

*/
package app.foodme;

/**
 *  Portions of the code adapted from: https://androidjson.com/recyclerview-json-listview-example/
 *
 *  Handles the sequential menus displayed to the customer.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CustomerMenu extends AppCompatActivity {

    String databaseURL = "http://70.77.241.161:8080";
    List<Item> itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    String HTTP_JSON_URL = databaseURL + "/menu.php?type=campus";
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemIDs;
    CustomerSelection customerSelection = new CustomerSelection();
    // Represents the column name we want to retrieve to display from the query
    String JSON_NAME = "Name";
    // Represents the column name we want to retrieve for tracking from the query
    String JSON_ID = "Campus_ID";
    // Represents if the customer is currently selecting menu items or not
    boolean itemSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView1);
        progressBar = findViewById(R.id.progressBar1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        itemIDs = new ArrayList<>();

        // Retrieves the first list of items from the database (campus)
        JSON_DATA_WEB_CALL();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                                                GestureDetector gestureDetector = new GestureDetector(CustomerMenu.this, new GestureDetector.SimpleOnGestureListener() {

                                                    @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                                                        return true;
                                                    }
                                                }
                                                );


        @Override
        public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

            if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                // If campus was the current selection
                if (JSON_ID.equals("Campus_ID")){

                    // Sets campusID to chosen campus
                    customerSelection.setCampusID(itemIDs.get(GetItemPosition));

                    // Sets next menu retrieval variables
                    JSON_NAME = "Name";
                    JSON_ID = "Vendor_ID";
                    HTTP_JSON_URL = databaseURL + "/menu.php?type=vendor&campusid=" + customerSelection.getCampusID();
                }

                // If vendor was the current selection
                else if (JSON_ID.equals("Vendor_ID")){

                    // Sets vendorID to chosen vendor
                    customerSelection.setVendorID(itemIDs.get(GetItemPosition));

                    // Sets next menu retrieval variables
                    JSON_NAME = "Menu_Name";
                    JSON_ID = "Menu_Name";
                    HTTP_JSON_URL = databaseURL + "/menu.php?type=menu&campusid=" + customerSelection.getCampusID() + "&vendorid=" + customerSelection.getVendorID();
                }

                // If menu was the current selection
                else if (JSON_ID.equals("Menu_Name")){

                    // Sets menuID to chosen menu
                    customerSelection.setMenuID(itemIDs.get(GetItemPosition));

                    // Sets next menu retrieval variables
                    JSON_NAME = "Item_Name";
                    JSON_ID = "Item_Name";
                    HTTP_JSON_URL = databaseURL + "/menu.php?type=menu_item&campusid=" + customerSelection.getCampusID() + "&vendorid=" +customerSelection.getVendorID() + "&menuid=" + customerSelection.getMenuID();
                }

                    // If on menu item selection, do not clear the menu and load new menu
                    if (itemSelection == false) {
                        // Clears the menu
                        itemIDs.clear();
                        itemList.clear();
                        recyclerView.removeAllViewsInLayout();
                        int count = recyclerViewadapter.getItemCount();
                        recyclerViewadapter.notifyItemRangeRemoved(0, count);

                        // Retrieves next menu
                        JSON_DATA_WEB_CALL();
                    }

                    else {

                        // TODO: handle menu item selection here

                        // Displays to the user that the item has been added to their order when they tap it (doesn't actually yet)
                        Toast.makeText(CustomerMenu.this, "Item added to order!", Toast.LENGTH_SHORT).show();
                    }

                    if (JSON_NAME.equals("Item_Name")){
                        itemSelection = true;
                    }
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

    // Parses the data retrieved from the database
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            // An item represents a tuple retrieved from the database
            Item GetDataAdapter2 = new Item();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                    // Sets the tuple's name (what is displayed in the menu)
                    GetDataAdapter2.setItemName(json.getString(JSON_NAME));
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

    // Returns to login
    public void ReturnToLogin(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
}
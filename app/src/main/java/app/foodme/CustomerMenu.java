package app.foodme;

/**
 *  Portions of the code adapted from: https://androidjson.com/recyclerview-json-listview-example/
 *
 *  Handles the sequential menus displayed to the customer.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
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
import android.widget.TextView;
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
    String currentMenu = "campus";
    // Represents the column name we want to retrieve to display from the query
    String JSON_NAME = "Name";
    // Represents the column name we want to retrieve for tracking from the query
    String JSON_ID = "Campus_ID";
    // Represents if the customer is currently selecting menu items or not
    boolean itemSelection = false;
    // Creates empty order for customer
    Order order = new Order();
    Button btnReview;
    RecyclerView.OnItemTouchListener touchListener;
    String paymentType;
    String building;
    String roomNum;
    String notes;
    String phone_no;
    ArrayList<OrderItem> orderItems;

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
        btnReview = findViewById(R.id.btn_review);
        phone_no = getIntent().getStringExtra("s_phoneNum");

        // Retrieves the first list of items from the database (campus)
        JSON_DATA_WEB_CALL();

        touchListener = new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(CustomerMenu.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            }
            );

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    // If campus was the current selection
                    if (JSON_ID.equals("Campus_ID")) {


                        // In the case that a user backs out to campus selection, and chooses a DIFFERENT campus, the order must be cleared and a message is displayed to user
                        if (!((customerSelection.getCampusID()).equals("") || customerSelection.getCampusID().equals(itemIDs.get(GetItemPosition)))) {
                            order.clearOrder();
                            // Displays to the user that their order has been cleared
                            // Displays to the user that their order has been cleared
                            Toast toast = Toast.makeText(CustomerMenu.this, "WARNING: Order has been cleared!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        // Sets campusID to chosen campus
                        customerSelection.setCampusID(itemIDs.get(GetItemPosition));

                        // Sets next menu retrieval variables
                        currentMenu = "vendors";
                        JSON_NAME = "Name";
                        JSON_ID = "Vendor_ID";
                        HTTP_JSON_URL = databaseURL + "/menu.php?type=vendor&campusid=" + customerSelection.getCampusID();
                    }

                    // If vendor was the current selection
                    else if (JSON_ID.equals("Vendor_ID")) {

                        // Sets vendorID to chosen vendor
                        customerSelection.setVendorID(itemIDs.get(GetItemPosition));

                        // Sets next menu retrieval variables
                        currentMenu = "menus";
                        JSON_NAME = "Menu_Name";
                        JSON_ID = "Menu_Name";
                        HTTP_JSON_URL = databaseURL + "/menu.php?type=menu&campusid=" + customerSelection.getCampusID() + "&vendorid=" + customerSelection.getVendorID();
                    }

                    // If menu was the current selection
                    else if (JSON_ID.equals("Menu_Name")) {

                        // Sets menuID to chosen menu
                        customerSelection.setMenuID(itemIDs.get(GetItemPosition));

                        // Sets next menu retrieval variables
                        currentMenu = "items";
                        JSON_NAME = "Item_Name";
                        JSON_ID = "Item_Name";
                        HTTP_JSON_URL = databaseURL + "/menu.php?type=menu_item&campusid=" + customerSelection.getCampusID() + "&vendorid=" + customerSelection.getVendorID() + "&menuid=" + customerSelection.getMenuID();
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
                    } else {

                        // Adds item to the orderItem array in Order
                        order.addItem(new OrderItem(customerSelection.getVendorID(), customerSelection.getMenuID(), itemIDs.get(GetItemPosition)));

                        // Displays to the user that the item has been added to their order
                        // Displays to the user that their order has been cleared
                        Toast toast = Toast.makeText(CustomerMenu.this, "Item added to order!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }

                    if (JSON_NAME.equals("Item_Name")) {
                        itemSelection = true;
                    }

                    // If the customer has items in their order, the submit order button is displayed
                    if (order.hasItems()) {
                        btnReview.setVisibility(View.VISIBLE);
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
        };
        recyclerView.addOnItemTouchListener(touchListener);
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

    /*
    * Uses the built-in back button in Android
    * Goes back to an appropriate location
    * If in a menu other than campus, goes back one menu position
    * If in the campus menu, returns to login
    */
    @Override
    public void onBackPressed() {
        if (true) {
            if (currentMenu.equals("campus")) {

                // Displays to the user that their order has been cleared
                Toast toast = Toast.makeText(CustomerMenu.this, "WARNING: Order has been cleared!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // Returns to the customer options menu
                Intent i = new Intent(this, CustOptions.class);
                i.putExtra("phone_no", phone_no);
                this.startActivity(i);
            }

            else if (currentMenu.equals("vendors")){
                // Sets previous menu retrieval variables
                JSON_NAME = "Name";
                JSON_ID = "Campus_ID";
                HTTP_JSON_URL = databaseURL + "/menu.php?type=campus";
                currentMenu = "campus";
            }

            else if (currentMenu.equals("menus")){
                // Sets previous menu retrieval variables
                JSON_NAME = "Name";
                JSON_ID = "Vendor_ID";
                HTTP_JSON_URL = databaseURL + "/menu.php?type=vendor&campusid=" + customerSelection.getCampusID();
                currentMenu = "vendors";
            }

            else if (currentMenu.equals("items")){
                // Sets previous menu retrieval variables
                JSON_NAME = "Menu_Name";
                JSON_ID = "Menu_Name";
                HTTP_JSON_URL = databaseURL + "/menu.php?type=menu&campusid=" + customerSelection.getCampusID() + "&vendorid=" + customerSelection.getVendorID();
                currentMenu = "menus";
                itemSelection = false;
            }

            else if (currentMenu.equals("review")){
                currentMenu = "items";

                // Sets previous menu retrieval variables
                JSON_NAME = "Item_Name";
                JSON_ID = "Item_Name";
                HTTP_JSON_URL = databaseURL + "/menu.php?type=menu_item&campusid=" + customerSelection.getCampusID() + "&vendorid=" +customerSelection.getVendorID() + "&menuid=" + customerSelection.getMenuID();

                // Reinstates the RecyclerView, Touch Listener, layout features, and review order button
                setContentView(R.layout.activity_customer_menu);
                recyclerView = findViewById(R.id.recyclerView1);
                progressBar = findViewById(R.id.progressBar1);
                recyclerView.setHasFixedSize(true);
                recyclerViewlayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(recyclerViewlayoutManager);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.addOnItemTouchListener(touchListener);
                btnReview = findViewById(R.id.btn_review);
                btnReview.setVisibility(View.VISIBLE);
            }

            else if (currentMenu.equals("submit")){
                currentMenu = "review";
                setContentView(R.layout.activity_customer_review_order);

                // Displays chosen order items to the customer
                TextView txtOrderItems = findViewById(R.id.txt_orderitems);
                txtOrderItems.setText(order.retrieveItems());
                txtOrderItems.setMovementMethod(new ScrollingMovementMethod());
            }

            // Ignores menu refresh if on review or submit page
            if (!currentMenu.equals("submit")) {

                itemIDs.clear();
                itemList.clear();
                recyclerView.removeAllViewsInLayout();
                int count = recyclerViewadapter.getItemCount();
                recyclerViewadapter.notifyItemRangeRemoved(0, count);

                // Retrieves previous menu
                JSON_DATA_WEB_CALL();
            }
        } else {
            super.onBackPressed();
        }
    }

    // Response when the review order button is selected
    public void reviewOrder(View view){
        currentMenu = "review";
        setContentView(R.layout.activity_customer_review_order);

        // Displays chosen order items to the customer
        TextView txtOrderItems = findViewById(R.id.txt_orderitems);
        txtOrderItems.setText(order.retrieveItems());
        txtOrderItems.setMovementMethod(new ScrollingMovementMethod());
    }

    // Response when the submit button is selected
    public void submitOrderPage(View view){
        currentMenu = "submit";
        setContentView(R.layout.activity_customer_submit_order);
    }

    // Handles retrieving the input entered by the customer and sending them for submission to the database
    public void finalOrderSubmission(View view){

        // Variables for the edittext fields on the order submission page
        EditText et_paymentType = findViewById(R.id.et_paymentType);
        EditText et_building = findViewById(R.id.et_building);
        EditText et_roomNum = findViewById(R.id.et_roomNum);
        EditText et_notes = findViewById(R.id.et_notes);

        // Retrieves information entered by the customer
        paymentType = et_paymentType.getText().toString();
        building = et_building.getText().toString();
        roomNum = et_roomNum.getText().toString();
        notes = et_notes.getText().toString();

        if (paymentType.equals("") || building.equals("") || roomNum.equals("")){
            Toast toast = Toast.makeText(CustomerMenu.this, "Please fill out all required information! (Notes are optional)", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            // checks user input
            // Regex expression used from: http://www.sitepoint.com/forums/showthread.php?204268-Regex-for-letters-numbers-and-spaces
            if (paymentType.matches("[a-z|A-Z|0-9|\\s]*") && building.matches("[a-z|A-Z|0-9|\\s]*") && roomNum.matches("[a-z|A-Z|0-9|\\s]*") && notes.matches("[a-z|A-Z|0-9|\\s]*")) {
                // Default apprOrDen value is A (payment system is not implemented)
                // Status is set to 1, which indicates the order is awaiting a deliverer
                // Sends customer order information to BackgroundWorker for processing
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);

                // Adds the order items to an array in the background worker for processing
                orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    backgroundWorker.orderItems.add(orderItem);
                }

                backgroundWorker.execute("order_submit", paymentType, "A", building, roomNum, "1", phone_no, assignEmployee(), customerSelection.getCampusID(), notes);
            } else {
                Toast toast = Toast.makeText(CustomerMenu.this, "WARNING: Please remove any special characters, and try again.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

    }

    // Assigns an employee for the order based on the campus the order is linked to
    public String assignEmployee(){
        // TODO: External self-refreshing script would handle employee assignment in full implementation and be called here
        // For demonstration purposes, the assignment is hardcoded. As the current implementation of the application does not allow for modification of employees in the system.

        if (customerSelection.getCampusID().equals("1")){
            return "111111111";
        }

        else {
            return "333333333";
        }
    }


}
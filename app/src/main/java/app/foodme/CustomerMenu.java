package app.foodme;
//package com.androidjson.recyclerviewcardviewjson_androidjsoncom;

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

public class CustomerMenu extends AppCompatActivity {

    List<Item> itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    String HTTP_JSON_URL = "http://70.77.241.161:8080/menu.php?type=campus";
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemIDs;
    CustomerSelection customerSelection = new CustomerSelection();
    String JSON_NAME = "Name";
    String JSON_ID = "Campus_ID";

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

                // If campus is the current selection, set the campusID chosen to customerSelection
                if (JSON_ID.equals("Campus_ID")){
                    customerSelection.setCampusID(itemIDs.get(GetItemPosition));
                    // clear RecyclerView after set and retrieve new list for vendor
                }
                //Intent i = new Intent(CustomerMenu.this, VendorMenu.class);
                //startActivity(i);
                //Toast.makeText(CustomerMenu.this, "ID: " + itemIDs.get(GetItemPosition), Toast.LENGTH_LONG).show();
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

            Item GetDataAdapter2 = new Item();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                // If a campus has not yet been selected (this might not work right if they select back)
                if ((customerSelection.getCampusID()).equals("")) {
                    GetDataAdapter2.setItemName(json.getString(JSON_NAME));
                    GetDataAdapter2.setItemID(json.getString(JSON_ID));
                    itemIDs.add(json.getString(JSON_ID));
               }


            } catch (JSONException e) {

                e.printStackTrace();
            }
            itemList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(itemList, this);

        recyclerView.setAdapter(recyclerViewadapter);

    }

    // Returns to login
    public void ReturnToLogin(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
}

package app.foodme;
//package com.androidjson.recyclerviewcardviewjson_androidjsoncom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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


public class CustomerMenu extends AppCompatActivity {

    List<Item> itemList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    String HTTP_JSON_URL = "http://192.168.1.5:8080/menu.php";
    String GET_JSON_FROM_SERVER_NAME = "foodme";
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> itemNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        itemList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        itemNames = new ArrayList<>();

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

                Toast.makeText(CustomerMenu.this, itemNames.get(GetItemPosition), Toast.LENGTH_LONG).show();
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

                GetDataAdapter2.setItemName(json.getString(GET_JSON_FROM_SERVER_NAME));

                itemNames.add(json.getString(GET_JSON_FROM_SERVER_NAME));


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

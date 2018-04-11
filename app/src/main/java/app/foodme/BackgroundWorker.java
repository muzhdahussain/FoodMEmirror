package app.foodme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This file sends login/registration information to the php site for verification.
 * <p>
 * Portions of code adapted from the tutorial series found at: https://www.youtube.com/watch?v=HK515-8-Q_w
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String empSIN;
    String s_phoneNum;
    String orderID;
    ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        /**
         * Set to http://192.168.1.5:8080 for Kaylee's house
         * Set to http://70.77.241.161:8080 for elsewhere
         */
        String databaseURL = "http://70.77.241.161:8080";
        String login_url = databaseURL + "/cust_login.php";
        String register_url = databaseURL + "/register.php";
        String emp_login_url = databaseURL + "/emp_login.php";
        String cust_submit_order_url = databaseURL + "/cust_submit_order.php";
        String cust_submit_item_url = databaseURL + "/cust_submit_item.php";

        // Handles customer login requests
        if (type.equals("cust_login")) {
            try {
                // Retrieves phone number entered by the customer
                String phoneNum = params[1];
                s_phoneNum= params[1];

                // Makes HTTP connection to the php site
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends login information to the php site
                String postData = URLEncoder.encode("phoneNum", "UTF-8") + "=" + URLEncoder.encode(phoneNum, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
        // Handles customer registration requests
        } else if (type.equals("register")) {
            try {
                // Retrieves registration variables entered by the user
                String phoneNum = params[1];
                String name = params[2];
                String email = params[3];

                // Makes HTTP connection to the registration php site
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends registration information to the php site
                String postData = URLEncoder.encode("phoneNum", "UTF-8") + "=" + URLEncoder.encode(phoneNum, "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Handles employee login requests
        } else if (type.equals("emp_login")) {

            try {
                // Retrieves SIN entered by the user
                empSIN = params[1];

                // Makes HTTP connection to the php site
                URL url = new URL(emp_login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("empSIN", "UTF-8") + "=" + URLEncoder.encode(empSIN, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

        // Handles order submission queries
        } else if ((type.equals("order_submit"))){

            try {
                // Makes HTTP connection to the php site
                URL url = new URL(cust_submit_order_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends order submission details to php script
                String postData = URLEncoder.encode("payment_type", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&"
                        + URLEncoder.encode("appr_den", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&"
                        + URLEncoder.encode("building", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&"
                        + URLEncoder.encode("room_num", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&"
                        + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") + "&"
                        + URLEncoder.encode("cust_phone", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8") + "&"
                        + URLEncoder.encode("emp_sin", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8") + "&"
                        + URLEncoder.encode("campus_id", "UTF-8") + "=" + URLEncoder.encode(params[8], "UTF-8") + "&"
                        + URLEncoder.encode("notes", "UTF-8") + "=" + URLEncoder.encode(params[9], "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site, which includes the order number for the inserted order
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();

                // Sends the order items to be added to the database
                OrderItem orderItem;
                String retrievedOrderNum = result;
                Iterator<OrderItem> iterator = orderItems.iterator();

                while (iterator.hasNext()) {
                    orderItem = iterator.next();

                    // Makes HTTP connection to the php site
                    url = new URL(cust_submit_item_url);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    // Creates output streams
                    outputStream = httpURLConnection.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    // Sends order item submission details to php script
                    postData = URLEncoder.encode("order_num", "UTF-8") + "=" + URLEncoder.encode(retrievedOrderNum, "UTF-8") + "&"
                            + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(orderItem.getMenuItemID(), "UTF-8") + "&"
                            + URLEncoder.encode("menu_name", "UTF-8") + "=" + URLEncoder.encode(orderItem.getMenuID(), "UTF-8") + "&"
                            + URLEncoder.encode("vendor_id", "UTF-8") + "=" + URLEncoder.encode(orderItem.getVendorID(), "UTF-8") + "&"
                            + URLEncoder.encode("campus_id", "UTF-8") + "=" + URLEncoder.encode(params[8], "UTF-8") + "&"
                            + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(orderItem.getQuantity(), "UTF-8");

                    bufferedWriter.write(postData);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    // Creates input streams
                    inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    // Reads response from the php site
                    result = "";
                    line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                s_phoneNum = params[6];
                // String response to the user on order submission
                result = "Order No. " + retrievedOrderNum + " has been submitted successfully!";
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("order in progress")) {

            orderID = params[1];

            try {

                String updateOrderUrl = "http://70.77.241.161:8080/update_order_status.php";
                // Makes HTTP connection to the php site
                URL url = new URL(updateOrderUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Sends order submission details to php script
                String postData = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8") + "&"
                        + URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(orderID, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site, which includes the order number for the inserted order
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();


                return result;


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(type.equals("order completed")){

            orderID = params[1];

            try {
                String updateOrderUrl = "http://70.77.241.161:8080/update_order_status.php?";
                // Makes HTTP connection to the php site
                URL url = new URL(updateOrderUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Creates output streams
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("3", "UTF-8") + "&"
                        + URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(orderID, "UTF-8");
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                // Creates input streams
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                // Reads response from the php site
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // Creates dialog window for the login response to the user
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Login successful!!")) {
            Intent i = new Intent(context, CustOptions.class);
            i.putExtra("phone_no", s_phoneNum);
            context.startActivity(i);

        } else if (result.equals("Employee login successful!!")) {
            Intent i = new Intent(context, EmpMenu.class);
            //pass the empSin thought activity's
            i.putExtra("EMP_SIN", empSIN);
            context.startActivity(i);
        }
       else if (result.startsWith("Order No.")){

            // Displays to the customer that their order has been submitted
            Toast.makeText(context, result,  Toast.LENGTH_LONG).show();

            // Returns to the customer options menu
            Intent i = new Intent(context, CustOptions.class);
            i.putExtra("phone_no", s_phoneNum);
            context.startActivity(i);
        }
        else {
            // Displays response to the user
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
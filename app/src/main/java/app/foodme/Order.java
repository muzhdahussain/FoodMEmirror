package app.foodme;

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
 * Stores the variables for a customer order.
 * Allows addition/removal of order items from an order.
 * Allows for clearing of order contents.
 */

public class Order {

    private String paymentType;
    private String apprOrDen;
    private String building;
    private String roomNum;
    private String status;
    private String custPhoneNum;
    private String campusID;
    private String notes;

    // Format of order list is (vendorID, menuID, menuItemID)
    private ArrayList<OrderItem> orderItems;

    Order() {
        this.campusID = "";
        this.apprOrDen = null;
        this.building = "";
        this.roomNum = "";
        this.status = "";
        this.custPhoneNum = "";
        this.notes = "";
        this.orderItems = new ArrayList<>();
    }
    
    // Sets final order submission details
    public void setOrderDetails(String campusID, String custPhoneNum, String apprOrDen, String building, String roomNum, String status, String notes, String paymentType) {
        this.apprOrDen = apprOrDen;
        this.building = building;
        this.roomNum = roomNum;
        this.status = status;
        this.notes = notes;
        this.paymentType = paymentType;
        this.campusID = campusID;
        this.custPhoneNum = custPhoneNum;
    }

    // Retrieves a string containing a list of all items in the order
    public String retrieveItems(){
        if (orderItems.isEmpty()){
            return "";
        }
        else {
            String items = "";
            Iterator<OrderItem> iterator = orderItems.iterator();
            while (iterator.hasNext()) {
                items = items + iterator.next().getMenuItemID() + "\n";
            }
            return items;
        }
    }
    // Returns true if the order has any items added to it
    public boolean hasItems(){
        if (orderItems.isEmpty()){
            return false;
        }
        return true;
    }

    // Clears an in-progress order if user switches to another campus
    public void clearOrder() {
        this.orderItems.clear();
    }

    // Removes an item from an order
    public void removeItem(OrderItem orderItem) {

        Iterator<OrderItem> iterator = orderItems.iterator();
        while (iterator.hasNext()) {
            OrderItem next = iterator.next();
            if (next.getVendorID().equals(orderItem.getVendorID()) && next.getMenuID().equals(orderItem.getMenuID()) && next.getMenuItemID().equals(orderItem.getMenuItemID())) {
                iterator.remove();
            }
        }
    }

    // Adds an item to an order
    public void addItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    // Submits an order to the database
    public String submitOrder() {
        try {

            String submitOrder_url = "http://70.77.241.161:8080/cust_submit_order.php";
            // Makes HTTP connection to the php site
            URL url = new URL(submitOrder_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            // Creates output streams
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            // Sends order submission details to php script
            String postData = URLEncoder.encode("payment_type", "UTF-8") + "=" + URLEncoder.encode(this.paymentType, "UTF-8")
                    + URLEncoder.encode("appr_den", "UTF-8") + "=" + URLEncoder.encode(this.apprOrDen, "UTF-8")
                    + URLEncoder.encode("building", "UTF-8") + "=" + URLEncoder.encode(this.building, "UTF-8")
                    + URLEncoder.encode("room_num", "UTF-8") + "=" + URLEncoder.encode(this.roomNum, "UTF-8")
                    + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(this.status, "UTF-8")
                    + URLEncoder.encode("cust_phone", "UTF-8") + "=" + URLEncoder.encode(this.custPhoneNum, "UTF-8")
                    + URLEncoder.encode("campus_id", "UTF-8") + "=" + URLEncoder.encode(this.campusID, "UTF-8")
                    + URLEncoder.encode("notes", "UTF-8") + "=" + URLEncoder.encode(this.notes, "UTF-8");
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

            String orderNum = result;
            // TODO: Send order items to php script using the retrieved order number
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

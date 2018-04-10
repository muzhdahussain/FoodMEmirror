package app.foodme;

import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Stores the variables for a customer order.
 * Allows addition/removal of order items from an order.
 * Allows for clearing of order contents.
 */

public class Order extends AppCompatActivity{


    // Format of order list is (vendorID, menuID, menuItemID)
    private ArrayList<OrderItem> orderItems;

    Order() {
        this.orderItems = new ArrayList<>();
    }

    // Retrieves the list of order items
    public ArrayList getOrderItems(){
        return orderItems;
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
}

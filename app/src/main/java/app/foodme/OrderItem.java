package app.foodme;

/**
 * Stores the variables for an order item.
 */
public class OrderItem {

    private String vendorID;
    private String menuID;
    private String menuItemID;
    private int quantity;

    OrderItem(String vendorID, String menuID, String menuItemID){
        this.vendorID = vendorID;
        this.menuID = menuID;
        this.menuItemID = menuItemID;
        this.quantity = 1;
    }

    public String getVendorID() {
        return this.vendorID;
    }

    public String getMenuID() {
        return this.menuID;
    }

    public String getMenuItemID() {
        return this.menuItemID;
    }

    public String getQuantity() { return Integer.toString(this.quantity); }

    public void incrementQuantity() {
        this.quantity++;
    }
}

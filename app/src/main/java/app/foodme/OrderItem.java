package app.foodme;

/**
 * Stores the variables for an order item.
 */
public class OrderItem {

    String vendorID;
    String menuID;
    String menuItemID;

    OrderItem(String vendorID, String menuID, String menuItemID){
        this.vendorID = vendorID;
        this.menuID = menuID;
        this.menuItemID = menuItemID;
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
}

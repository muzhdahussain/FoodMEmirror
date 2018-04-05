package app.foodme;

/**
 * Stores the selection variables for the customer.
 */
public class CustomerSelection {

    String campusID;
    String vendorID;
    String menuID;
    boolean history;
    boolean newOrder;

    CustomerSelection(){
        this.campusID = "";
        this.vendorID = "";
        this.menuID = "";
    }

    public String getCampusID() {

        return campusID;
    }

    public void setCampusID(String TempName) {

        this.campusID = TempName;
    }

    public String getVendorID() {

        return vendorID;
    }

    public void setVendorID(String TempName) {

        this.vendorID = TempName;
    }

    public String getMenuID() {

        return menuID;
    }

    public void setMenuID(String TempName) {

        this.menuID = TempName;
    }

    public void viewHistory() {

        this.history= true;
        this.newOrder= false;
    }
    public void placeNewOrder() {

        this.history= false;
        this.newOrder= true;
    }
}
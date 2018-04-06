package app.foodme;

/**
 * Stores the selection variables for the customer for tracking.
 */
public class CustomerSelection {

    String campusID;
    String vendorID;
    String menuID;

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
}
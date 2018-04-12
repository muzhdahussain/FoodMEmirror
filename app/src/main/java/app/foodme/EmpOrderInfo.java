package app.foodme;

/*
* Holds info about a specific order to be displayed on the emp order screen
 */

public class EmpOrderInfo {

    String campus;
    String building;
    String roomNum;
    String custPhoneNum;
    String campusID;
    String notes;
    String vendor;
    String vendorBuilding;
    String vendorRoomNum;
    String itemName;
    String itemQuantity;
    String orderVendorInfo;

    public EmpOrderInfo() {
        this.campus = "";
        this.building = "";
        this.roomNum = "";
        this.custPhoneNum = "";
        this.campusID = "";
        this.notes = "";
        this.vendor = "";
        this.vendorBuilding = "";
        this.vendorRoomNum = "";
        this.itemName = "";
        this.itemQuantity = "";
        this.orderVendorInfo= "";
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getCustPhoneNum() {
        return custPhoneNum;
    }

    public void setCustPhoneNum(String custPhoneNum) {
        this.custPhoneNum = custPhoneNum;
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorBuilding() {
        return vendorBuilding;
    }

    public void setVendorBuilding(String vendorBuilding) {
        this.vendorBuilding = vendorBuilding;
    }

    public String getVendorRoomNum() {
        return vendorRoomNum;
    }

    public void setVendorRoomNum(String vendorRoomNum) {
        this.vendorRoomNum = vendorRoomNum;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getOrderVendorInfo() {
        return orderVendorInfo;
    }

    public void setOrderVendorInfo(String orderVendorInfo) {
        this.orderVendorInfo = orderVendorInfo;
    }
}

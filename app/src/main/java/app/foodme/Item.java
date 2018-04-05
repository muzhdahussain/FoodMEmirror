package app.foodme;

/**
 * Created by Kaylee on 2018-03-27.
 *
 * Stores information on a specific item displayed in the menu.
 */

public class Item {

    String itemName;
    String itemID;

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String TempName) {

        this.itemName = TempName;
    }

    public String getItemID() {

        return itemID;
    }

    public void setItemID(String TempID) {

        this.itemID = TempID;
    }
}
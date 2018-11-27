package gr.ifasman7.uomandroid.restaurantsworldwideguide;

/**
 * Created by ifasman7 on 07/11/2018.
 */

public class RestaurantMenuItem {

    private String itemName;
    private String itemDescription;
    private double itemPrice;

    public RestaurantMenuItem(String itemName, String itemDescription, double itemPrice) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    @Override
    public String toString() {
        return "Item -> "+
                "Name: " + itemName +
                ", Description: " + itemDescription +
                ", Price: " + itemPrice +
                "\n";
    }
}

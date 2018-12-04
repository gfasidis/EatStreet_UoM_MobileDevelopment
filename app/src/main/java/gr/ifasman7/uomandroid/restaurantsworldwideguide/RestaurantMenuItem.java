package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantMenuItem implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.itemDescription);
        dest.writeDouble(this.itemPrice);
    }

    protected RestaurantMenuItem(Parcel in) {
        this.itemName = in.readString();
        this.itemDescription = in.readString();
        this.itemPrice = in.readDouble();
    }

    public static final Parcelable.Creator<RestaurantMenuItem> CREATOR = new Parcelable.Creator<RestaurantMenuItem>() {
        @Override
        public RestaurantMenuItem createFromParcel(Parcel source) {
            return new RestaurantMenuItem(source);
        }

        @Override
        public RestaurantMenuItem[] newArray(int size) {
            return new RestaurantMenuItem[size];
        }
    };
}

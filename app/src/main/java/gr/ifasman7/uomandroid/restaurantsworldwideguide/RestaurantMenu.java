package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RestaurantMenu implements Parcelable {

    private String category;
    private ArrayList<RestaurantMenuItem> itemsArrayList;

    public RestaurantMenu(String category, ArrayList<RestaurantMenuItem> itemsArrayList) {
        this.category = category;
        this.itemsArrayList = itemsArrayList;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<RestaurantMenuItem> getItemsArrayList() {
        return itemsArrayList;
    }

    @Override
    public String toString() {
        return "RestaurantMenu:\n" +
                "Category: " + category + "\n" +
                ",itemsArrayList:\n" + itemsArrayList.toString() +
                "\n";
    }

    public static ArrayList<String> getCategoriesFromMenu(ArrayList<RestaurantMenu> menuArrayList){
        ArrayList<String> categories = new ArrayList<>();

        for (RestaurantMenu menu : menuArrayList){
            String menuCategory = menu.getCategory();
            categories.add(menuCategory);
        }
        return categories;
    }

    public static ArrayList<RestaurantMenuItem> getItemsFromCategory(String selectedCategory, ArrayList<RestaurantMenu> menuArrayList){
        ArrayList<RestaurantMenuItem> menuItems = new ArrayList<>();

        for (RestaurantMenu menu : menuArrayList){
            if(menu.getCategory().equals(selectedCategory)){
                for(RestaurantMenuItem item : menu.getItemsArrayList())
                    menuItems.add(item);
            }
        }
        return menuItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeList(this.itemsArrayList);
    }

    protected RestaurantMenu(Parcel in) {
        this.category = in.readString();
        this.itemsArrayList = new ArrayList<RestaurantMenuItem>();
        in.readList(this.itemsArrayList, RestaurantMenuItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<RestaurantMenu> CREATOR = new Parcelable.Creator<RestaurantMenu>() {
        @Override
        public RestaurantMenu createFromParcel(Parcel source) {
            return new RestaurantMenu(source);
        }

        @Override
        public RestaurantMenu[] newArray(int size) {
            return new RestaurantMenu[size];
        }
    };
}


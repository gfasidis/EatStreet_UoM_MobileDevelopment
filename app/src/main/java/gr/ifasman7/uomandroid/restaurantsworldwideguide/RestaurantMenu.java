package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import java.util.ArrayList;

public class RestaurantMenu {

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

    public static ArrayList<String> getCategories(ArrayList<RestaurantMenu> menuArrayList){
        ArrayList<String> categories = new ArrayList<>();

        for (RestaurantMenu menu : menuArrayList){
            String categMenu = menu.getCategory();
            categories.add(categMenu);
        }
        return categories;
    }

    public static ArrayList<RestaurantMenuItem> getItemsByCategory(String selectedCategory, ArrayList<RestaurantMenu> menuArrayList){
        ArrayList<RestaurantMenuItem> menuItems = new ArrayList<>();

        for (RestaurantMenu menu : menuArrayList){
            if(menu.getCategory().equals(selectedCategory)){
                for(RestaurantMenuItem item : menu.getItemsArrayList())
                    menuItems.add(item);
            }
        }

        return menuItems;
    }
}


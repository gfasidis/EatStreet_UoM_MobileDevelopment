package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONMenuParser {

    private static final String TAG = "iFasMan";

    private static final String ITEMS_ARRAY = "items";
    private static final String CATEGORY_NAME = "name";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_PRICE = "basePrice";

    private ArrayList<RestaurantMenu> menuArrayList;

    public JSONMenuParser() {
        this.menuArrayList = new ArrayList<RestaurantMenu>();
    }

    public ArrayList<RestaurantMenu> getMenuArrayList() {
        return menuArrayList;
    }

    public boolean parse(String jsonData){

        JSONArray menuJSON = null;

        try {

            menuJSON = new JSONArray(jsonData);

            for (int i = 0; i < menuJSON.length(); i++){
                JSONObject menuCategories = menuJSON.getJSONObject(i);

                String category = menuCategories.getString(CATEGORY_NAME);
                ArrayList<RestaurantMenuItem> menuItemsArrayList = new ArrayList<>();

                JSONArray menuItems = menuCategories.getJSONArray(ITEMS_ARRAY);
                for (int j = 0; j < menuItems.length(); j++){
                    JSONObject item = menuItems.getJSONObject(j);

                    String itemName;
                    String itemDesc;
                    Double itemPrice;

                    itemName = item.getString(ITEM_NAME);
                    if (item.has(ITEM_DESCRIPTION)) {
                        itemDesc = item.getString(ITEM_DESCRIPTION);
                    } else{
                        itemDesc = " ";
                    }
                    itemPrice = item.getDouble(ITEM_PRICE);

                    menuItemsArrayList.add(new RestaurantMenuItem(itemName,itemDesc,itemPrice));
                }
                RestaurantMenu restaurantMenu = new RestaurantMenu(category,menuItemsArrayList);
                menuArrayList.add(restaurantMenu);
            }

        } catch (JSONException e) {
            Log.e(TAG, "parse: Error parsing json data",e );
            return false;
        }
        return true;

    }
}

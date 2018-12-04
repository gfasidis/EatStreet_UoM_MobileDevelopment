package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JSONNearbyRestaurantParser {

    private static final String TAG = "iFasMan";

    private static final String RES_API_KEY = "apiKey";
    private static final String RES_NAME = "name";
    private static final String RES_URL = "url";
    private static final String RES_ADDRESS = "streetAddress";
    private static final String RES_CITY = "city";
    private static final String RES_STATE = "state";
    private static final String RES_ZIP = "zip";
    private static final String RES_PHONE = "phone";
    private static final String RES_LAT = "latitude";
    private static final String RES_LONG = "longitude";
    private static final String RES_ACCEPT_CASH = "acceptsCash";
    private static final String RES_ACCEPT_CARD = "acceptsCard";
    private static final String RES_LOGO_URL = "logoUrl";
    private static final String RES_OFFERS_DELIVERY = "offersDelivery";
    private static final String RES_OFFERS_PICKUP = "offersPickup";
    private static final String RES_MIN_WAIT_TIME = "minWaitTime";
    private static final String RES_MAX_WAIT_TIME = "maxWaitTime";

    private static final String RES_FOOD_TYPES = "foodTypes";
    private static final String RES_HOURS = "hours";


    private ArrayList<Restaurant> restaurants;

    public JSONNearbyRestaurantParser(){
        restaurants = new ArrayList<>();
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public boolean parse(String jsonData){

        JSONObject restaurantJSON = null;
        try {

            restaurantJSON = new JSONObject(jsonData);
            JSONArray nearby_restaurants = restaurantJSON.getJSONArray("restaurants");

            for (int i = 0; i < nearby_restaurants.length(); i++){

                JSONObject restaurant = nearby_restaurants.getJSONObject(i);

                String res_id = restaurant.getString(RES_API_KEY);
                String res_name = restaurant.getString(RES_NAME);
                String res_url = restaurant.getString(RES_URL);
                String res_logo_url = restaurant.getString(RES_LOGO_URL);

                String res_address = restaurant.getString(RES_ADDRESS);
                String res_city = restaurant.getString(RES_CITY);

                Double res_latitude = restaurant.getDouble(RES_LAT);
                Double res_longtitude = restaurant.getDouble(RES_LONG);

                String res_state = restaurant.getString(RES_STATE);
                String res_zip = restaurant.getString(RES_ZIP);
                String res_phone = restaurant.getString(RES_PHONE);

                Boolean res_accept_cash = restaurant.getBoolean(RES_ACCEPT_CASH);
                Boolean res_accept_card = restaurant.getBoolean(RES_ACCEPT_CARD);
                Boolean res_offers_delivery = restaurant.getBoolean(RES_OFFERS_DELIVERY);
                Boolean res_offers_pickup = restaurant.getBoolean(RES_OFFERS_PICKUP);

                String res_min_wait_time = restaurant.getString(RES_MIN_WAIT_TIME);
                String res_max_wait_time = restaurant.getString(RES_MAX_WAIT_TIME);
                String res_waiting_time = res_min_wait_time + " - " + res_max_wait_time;

                JSONObject hours = restaurant.getJSONObject(RES_HOURS);
                Map<String,String> res_hours = new HashMap<>();

                if (hours.has("Monday")){
                    res_hours.put("Monday",hours.getJSONArray("Monday").getString(0));
                }
                else {
                    res_hours.put("Monday","CLOSED");
                }
                if(hours.has("Tuesday")){
                    res_hours.put("Tuesday",hours.getJSONArray("Tuesday").getString(0));
                }
                else {
                    res_hours.put("Tuesday","CLOSED");
                }
                if (hours.has("Wednesday")){
                    res_hours.put("Wednesday",hours.getJSONArray("Wednesday").getString(0));
                }
                else {
                    res_hours.put("Wednesday","CLOSED");
                }
                if (hours.has("Thursday")){
                    res_hours.put("Thursday",hours.getJSONArray("Thursday").getString(0));
                }
                else {
                    res_hours.put("Thursday","CLOSED");
                }
                if (hours.has("Friday")){
                    res_hours.put("Friday",hours.getJSONArray("Friday").getString(0));
                }
                else {
                    res_hours.put("Friday","CLOSED");
                }
                if (hours.has("Saturday")){
                    res_hours.put("Saturday",hours.getJSONArray("Saturday").getString(0));
                }
                else {
                    res_hours.put("Saturday","CLOSED");
                }
                if (hours.has("Sunday")){
                    res_hours.put("Sunday",hours.getJSONArray("Sunday").getString(0));
                }
                else {
                    res_hours.put("Sunday","CLOSED");
                }

                JSONArray foodTypes = restaurant.getJSONArray(RES_FOOD_TYPES);
                ArrayList<String> res_food_types = new ArrayList<>();
                for (int x = 0; x < foodTypes.length(); x++){
                    res_food_types.add(foodTypes.getString(x));
                }

                Restaurant aRestaurant = new Restaurant(res_id,res_name,res_url,res_logo_url,res_address,res_city,res_latitude,res_longtitude,res_state,res_zip,res_phone,res_waiting_time,
                        res_accept_cash,res_accept_card,res_offers_delivery,res_offers_pickup,res_hours,foodTypesToString(res_food_types));
                restaurants.add(aRestaurant);
            }
        } catch (JSONException e) {
            Log.e(TAG, "parse: Error parsing json data",e );
            return false;
        }

        return true;

    }

    private String foodTypesToString(ArrayList<String> foods){

        StringBuilder foodTypes = new StringBuilder();
        for(int i = 0; i < foods.size(); i++){
            foodTypes.append(foods.get(i));
            if (i != foods.size() - 1){
                foodTypes.append(", ");
            }
        }
        return foodTypes.toString();
    }
}

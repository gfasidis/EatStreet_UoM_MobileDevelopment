package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import java.util.ArrayList;
import java.util.Map;


public class Restaurant {
    private static final String TAG = "iFasMan";

    private String res_id;
    private String res_name;
    private String res_url;
    private String res_logo_url;

    private String res_address;
    private String res_city;
    private Double res_latitude;
    private Double res_longitude;
    private String res_state;
    private String res_zip;
    private String res_phone;
    private String res_max_wait_time;
    private String res_min_wait_time;
    private String res_Waiting_Time;

    private Boolean res_accept_cash;
    private Boolean res_accept_card;
    private Boolean res_offers_delivery;
    private Boolean res_offers_pickup;

    private Map<String,String> res_hours;
    private String res_food_types;

    private ArrayList<RestaurantMenu> res_menu;

    public Restaurant(String res_id, String res_name, String res_url, String res_logo_url, String res_address, String res_city, Double res_latitude, Double res_longitude, String res_state, String res_zip, String res_phone, Boolean res_accept_cash, Boolean res_accept_card, Boolean res_offers_delivery, Boolean res_offers_pickup,String res_min_wait_time, String res_max_wait_time, Map<String, String> res_hours, String res_food_types) {
        this.res_id = res_id;
        this.res_name = res_name;
        this.res_url = res_url;
        this.res_logo_url = res_logo_url;
        this.res_address = res_address;
        this.res_city = res_city;
        this.res_latitude = res_latitude;
        this.res_longitude = res_longitude;
        this.res_state = res_state;
        this.res_zip = res_zip;
        this.res_phone = res_phone;
        this.res_accept_cash = res_accept_cash;
        this.res_accept_card = res_accept_card;
        this.res_hours = res_hours;
        this.res_food_types = res_food_types;
        this.res_offers_pickup = res_offers_pickup;
        this.res_offers_delivery = res_offers_delivery;
        this.res_Waiting_Time = res_min_wait_time + " - " + res_max_wait_time;

    }

    public Restaurant(String res_id, String res_name, String res_url, String res_logo_url, String res_address, String res_city, Double res_latitude, Double res_longitude, String res_state, String res_zip, String res_phone, String res_Waiting_Time, Boolean res_accept_cash, Boolean res_accept_card, Boolean res_offers_delivery, Boolean res_offers_pickup, Map<String, String> res_hours, String res_food_types) {
        this.res_id = res_id;
        this.res_name = res_name;
        this.res_url = res_url;
        this.res_logo_url = res_logo_url;
        this.res_address = res_address;
        this.res_city = res_city;
        this.res_latitude = res_latitude;
        this.res_longitude = res_longitude;
        this.res_state = res_state;
        this.res_zip = res_zip;
        this.res_phone = res_phone;
        this.res_Waiting_Time = res_Waiting_Time;
        this.res_accept_cash = res_accept_cash;
        this.res_accept_card = res_accept_card;
        this.res_offers_delivery = res_offers_delivery;
        this.res_offers_pickup = res_offers_pickup;
        this.res_hours = res_hours;
        this.res_food_types = res_food_types;
    }

    public void setRes_menu(ArrayList<RestaurantMenu> res_menu) {
        this.res_menu = res_menu;
    }

    public ArrayList<RestaurantMenu> getRes_menu() {
        return res_menu;
    }

    public String getRes_id() {
        return res_id;
    }


    public String getRes_name() {
        return res_name;
    }


    public String getRes_url() {
        return res_url;
    }


    public String getRes_logo_url() {
        return res_logo_url;
    }


    public String getRes_address() {
        return res_address;
    }


    public String getRes_city() {
        return res_city;
    }


    public Double getRes_latitude() {
        return res_latitude;
    }


    public Double getRes_longitude() {
        return res_longitude;
    }


    public String getRes_state() {
        return res_state;
    }


    public String getRes_zip() {
        return res_zip;
    }


    public String getRes_phone() {
        return res_phone;
    }


    public Boolean getRes_accept_cash() {
        return res_accept_cash;
    }


    public Boolean getRes_accept_card() {
        return res_accept_card;
    }


    public Map<String, String> getRes_hours() {
        return res_hours;
    }


    public String getRes_food_types() {
        return res_food_types;
    }


    public Boolean getRes_offers_delivery() {
        return res_offers_delivery;
    }


    public Boolean getRes_offers_pickup() {
        return res_offers_pickup;
    }

    public String getRes_Waiting_Time() {
        return res_Waiting_Time;
    }

//    public String getFoodTypes(){
//
//        StringBuilder foodTypes = new StringBuilder();
//        for(int i = 0; i < this.getRes_food_types().size(); i++){
//            foodTypes.append(this.getRes_food_types().get(i));
//            if (i != this.getRes_food_types().size() - 1){
//                foodTypes.append(", ");
//            }
//        }
//
//
//        return foodTypes.toString();
//    }

    @Override
    public String toString() {
        return "Restaurant:" +
                "\n iD: " + res_id +
                ",\n Name: " + res_name +
                ",\n URL: " + res_url +
                ",\n Address: " + res_address +
                ",\n City: " + res_city +
                ",\n Latitude: " + res_latitude +
                ",\n Longitude: " + res_longitude +
                ",\n Zip: " + res_zip +
                ",\n State: " + res_state +
                ",\n Phone: " + res_phone +
                ",\n Logo URL: " + res_logo_url +
                ",\n Accept Cash: " + String.valueOf(res_accept_cash) +
                ",\n Accept Card: " + String.valueOf(res_accept_card) +
                ",\n Hours: " + res_hours +
                ",\n Food Types: " + res_food_types +
                ",\n Offers Delivery: " + res_offers_delivery +
                ",\n Offers Pickup: " + res_offers_pickup +
                ",\n Waiting Time: " + res_Waiting_Time +
                "\n";
    }
}

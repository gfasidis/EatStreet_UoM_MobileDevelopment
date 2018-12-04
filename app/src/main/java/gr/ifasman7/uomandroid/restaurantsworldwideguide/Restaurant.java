package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Restaurant implements Parcelable {
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
    private String res_Waiting_Time;

    private Boolean res_accept_cash;
    private Boolean res_accept_card;
    private Boolean res_offers_delivery;
    private Boolean res_offers_pickup;

    private Map<String,String> res_hours;
    private String res_food_types;

    private ArrayList<RestaurantMenu> res_menu;

    public Restaurant(String res_id, String res_name, String res_url, String res_logo_url, String res_address, String res_city,
                      Double res_latitude, Double res_longitude, String res_state, String res_zip, String res_phone,
                      String res_Waiting_Time, Boolean res_accept_cash, Boolean res_accept_card, Boolean res_offers_delivery,
                      Boolean res_offers_pickup, Map<String, String> res_hours, String res_food_types)
    {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.res_id);
        dest.writeString(this.res_name);
        dest.writeString(this.res_url);
        dest.writeString(this.res_logo_url);
        dest.writeString(this.res_address);
        dest.writeString(this.res_city);
        dest.writeValue(this.res_latitude);
        dest.writeValue(this.res_longitude);
        dest.writeString(this.res_state);
        dest.writeString(this.res_zip);
        dest.writeString(this.res_phone);
        dest.writeString(this.res_Waiting_Time);
        dest.writeValue(this.res_accept_cash);
        dest.writeValue(this.res_accept_card);
        dest.writeValue(this.res_offers_delivery);
        dest.writeValue(this.res_offers_pickup);
        dest.writeInt(this.res_hours.size());
        for (Map.Entry<String, String> entry : this.res_hours.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.res_food_types);
        dest.writeList(this.res_menu);
    }

    protected Restaurant(Parcel in) {
        this.res_id = in.readString();
        this.res_name = in.readString();
        this.res_url = in.readString();
        this.res_logo_url = in.readString();
        this.res_address = in.readString();
        this.res_city = in.readString();
        this.res_latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.res_longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.res_state = in.readString();
        this.res_zip = in.readString();
        this.res_phone = in.readString();
        this.res_Waiting_Time = in.readString();
        this.res_accept_cash = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.res_accept_card = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.res_offers_delivery = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.res_offers_pickup = (Boolean) in.readValue(Boolean.class.getClassLoader());
        int res_hoursSize = in.readInt();
        this.res_hours = new HashMap<String, String>(res_hoursSize);
        for (int i = 0; i < res_hoursSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.res_hours.put(key, value);
        }
        this.res_food_types = in.readString();
        this.res_menu = new ArrayList<RestaurantMenu>();
        in.readList(this.res_menu, RestaurantMenu.class.getClassLoader());
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}

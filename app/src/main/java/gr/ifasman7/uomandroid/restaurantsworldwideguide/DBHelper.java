package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ifasman7 on 13/11/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "iFasMan";

    private static final int VERSION_DB = 1;
    private static final String DATABASE_NAME = "EatStreetDB.db";

    private static final String RES_TABLE_NAME = "Restaurants";
    private static final String RES_RES_ID = "res_id";
    private static final String RES_RES_NAME = "res_name";
    private static final String RES_RES_URL = "res_url";
    private static final String RES_RES_LOGO_URL = "res_logo_url";
    private static final String RES_RES_ADDRESS = "res_address";
    private static final String RES_RES_CITY = "res_city";
    private static final String RES_RES_LATITUDE = "res_latitude";
    private static final String RES_RES_LONGITUDE = "res_longitude";
    private static final String RES_RES_STATE = "res_state";
    private static final String RES_RES_ZIP = "res_zip";
    private static final String RES_RES_PHONE = "res_phone";
    private static final String RES_RES_ACCEPT_CASH = "res_accept_cash";
    private static final String RES_RES_ACCEPT_CARD = "res_accept_card";
    private static final String RES_RES_FOOD_TYPES = "res_food_types";
    private static final String RES_RES_OFFERS_PICKUP = "res_offers_pickup";
    private static final String RES_RES_OFFERS_DELIVERY = "res_offers_delivery";
    private static final String RES_RES_WAITING_TIME = "res_waiting_time";
    private static final String RES_HOURS_MONDAY = "res_Monday";
    private static final String RES_HOURS_TUESDAY = "res_Tuesday";
    private static final String RES_HOURS_WEDNESDAY = "res_Wednesday";
    private static final String RES_HOURS_THURSDAY = "res_Thursday";
    private static final String RES_HOURS_FRIDAY = "res_Friday";
    private static final String RES_HOURS_SATURDAY = "res_Saturday";
    private static final String RES_HOURS_SUNDAY = "res_Sunday";

    private static final String FAV_RES_TABLE_NAME = "UsersFavRestaurant";
    private static final String FAV_RES_USER_ID = "user_id";
    private static final String FAV_RES_RES_ID = "res_id";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RES_TABLE_NAME + " (\n" +
                "\t" + RES_RES_ID + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_NAME + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_URL + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_LOGO_URL + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_ADDRESS + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_CITY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_LATITUDE + "\tREAL NOT NULL,\n" +
                "\t" + RES_RES_LONGITUDE + "\tREAL NOT NULL,\n" +
                "\t" + RES_RES_STATE + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_ZIP + "tTEXT NOT NULL,\n" +
                "\t" + RES_RES_PHONE + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_ACCEPT_CASH + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_ACCEPT_CARD + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_FOOD_TYPES + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_OFFERS_PICKUP + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_OFFERS_DELIVERY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_RES_WAITING_TIME + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_MONDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_TUESDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_WEDNESDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_THURSDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_FRIDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_SATURDAY + "\tTEXT NOT NULL,\n" +
                "\t" + RES_HOURS_SUNDAY + "\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(" + RES_RES_ID + ")\n" +
                ");");

        db.execSQL("CREATE TABLE " + FAV_RES_TABLE_NAME + " (\n" +
                "\t" + FAV_RES_USER_ID + "\tTEXT NOT NULL,\n" +
                "\t" + FAV_RES_RES_ID + "\tTEXT NOT NULL,\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\tFOREIGN KEY(" + FAV_RES_RES_ID + ") REFERENCES " + RES_TABLE_NAME + "(" + RES_RES_ID + ")\n" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAV_RES_TABLE_NAME);
        onCreate(db);
    }

    public void removeFavRestaurant(String userID, String resID){
        SQLiteDatabase db = this.getWritableDatabase();

        String whereQuery = FAV_RES_USER_ID + " = ? " + " AND " + FAV_RES_RES_ID + " = ? ";
        db.delete(FAV_RES_TABLE_NAME, whereQuery, new String[] {userID, resID});
    }

    public void insertRestaurant (Restaurant restaurant){

        if(!isRestaurantInDataBase(restaurant)){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues resValues = new ContentValues();
            resValues.put(RES_RES_ID,restaurant.getRes_id());
            resValues.put(RES_RES_NAME, restaurant.getRes_name());
            resValues.put(RES_RES_URL, restaurant.getRes_url());
            resValues.put(RES_RES_LOGO_URL, restaurant.getRes_logo_url());
            resValues.put(RES_RES_ADDRESS, restaurant.getRes_address());
            resValues.put(RES_RES_CITY, restaurant.getRes_city());
            resValues.put(RES_RES_LATITUDE, restaurant.getRes_latitude());
            resValues.put(RES_RES_LONGITUDE, restaurant.getRes_longitude());
            resValues.put(RES_RES_STATE, restaurant.getRes_state());
            resValues.put(RES_RES_ZIP, restaurant.getRes_zip());
            resValues.put(RES_RES_PHONE, restaurant.getRes_phone());
            resValues.put(RES_RES_ACCEPT_CASH, restaurant.getRes_accept_cash().toString());
            resValues.put(RES_RES_ACCEPT_CARD, restaurant.getRes_accept_card().toString());
            resValues.put(RES_RES_FOOD_TYPES, restaurant.getRes_food_types());
            resValues.put(RES_RES_OFFERS_PICKUP, restaurant.getRes_offers_pickup().toString());
            resValues.put(RES_RES_OFFERS_DELIVERY, restaurant.getRes_offers_delivery().toString());
            resValues.put(RES_RES_WAITING_TIME, restaurant.getRes_Waiting_Time());
            resValues.put(RES_HOURS_MONDAY, restaurant.getRes_hours().get("Monday"));
            resValues.put(RES_HOURS_TUESDAY, restaurant.getRes_hours().get("Tuesday"));
            resValues.put(RES_HOURS_WEDNESDAY, restaurant.getRes_hours().get("Wednesday"));
            resValues.put(RES_HOURS_THURSDAY, restaurant.getRes_hours().get("Thursday"));
            resValues.put(RES_HOURS_FRIDAY, restaurant.getRes_hours().get("Friday"));
            resValues.put(RES_HOURS_SATURDAY, restaurant.getRes_hours().get("Saturday"));
            resValues.put(RES_HOURS_SUNDAY, restaurant.getRes_hours().get("Sunday"));
            db.insert(RES_TABLE_NAME, null, resValues);
        }
        else
            Log.d(TAG, "insertRestaurant: Restaurant " + restaurant.getRes_id() + " " + restaurant.getRes_name() + " is already in DB");


    }

    public void addRestaurantToUser(String userID, Restaurant restaurant){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues userFavValues = new ContentValues();
        userFavValues.put(FAV_RES_USER_ID, userID);
        userFavValues.put(FAV_RES_RES_ID, restaurant.getRes_id());
        db.insert(FAV_RES_TABLE_NAME, null, userFavValues);

    }

    private boolean isRestaurantInDataBase(Restaurant restaurant){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + RES_TABLE_NAME + " WHERE " + RES_RES_ID + " = ? ";
        Cursor results = db.rawQuery( query, new String[] {restaurant.getRes_id()});
        if(results.getCount() == 1){
            return true;
        }
        return false;

    }

    public Boolean checkConBetweenUserRestaurant(String userID, Restaurant restaurant){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + FAV_RES_TABLE_NAME + " WHERE " + FAV_RES_USER_ID + " = ? " + " AND " + FAV_RES_RES_ID + " = ? ";
        Cursor results = db.rawQuery( query, new String[] {userID, restaurant.getRes_id()});
        if(results.getCount() == 1){
            return true;
        }
        return false;

    }

    public ArrayList<Restaurant> getFavoriteRestaurant(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.* FROM " + RES_TABLE_NAME + " r JOIN " + FAV_RES_TABLE_NAME + " u WHERE r." + RES_RES_ID + " = u." + FAV_RES_RES_ID + " AND u." + FAV_RES_USER_ID + " = ? ";
        Cursor results = db.rawQuery(query, new String[] {id});

        Log.d(TAG, "getFavoriteRestaurant: " + results.getCount());
        return parseCursorRestaurants(results);

    }

    private ArrayList<Restaurant> parseCursorRestaurants(Cursor results){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String res_id;
        String res_name;
        String res_url;
        String res_logo_url;

        String res_address;
        String res_city;
        Double res_latitude;
        Double res_longitude;
        String res_state;
        String res_zip;
        String res_phone;
        String res_Waiting_Time;

        Boolean res_accept_cash;
        Boolean res_accept_card;
        Boolean res_offers_delivery;
        Boolean res_offers_pickup;

        Map<String,String> res_hours;
        String res_food_types;

        while (results.moveToNext()){
            res_id = results.getString(results.getColumnIndex(RES_RES_ID));
            res_name = results.getString(results.getColumnIndex(RES_RES_NAME));
            res_url = results.getString(results.getColumnIndex(RES_RES_URL));
            res_logo_url = results.getString(results.getColumnIndex(RES_RES_LOGO_URL));

            res_address = results.getString(results.getColumnIndex(RES_RES_ADDRESS));
            res_city = results.getString(results.getColumnIndex(RES_RES_CITY));
            res_latitude = results.getDouble(results.getColumnIndex(RES_RES_LATITUDE));
            res_longitude = results.getDouble(results.getColumnIndex(RES_RES_LONGITUDE));
            res_state = results.getString(results.getColumnIndex(RES_RES_STATE));
            res_zip = results.getString(results.getColumnIndex(RES_RES_ZIP));
            res_phone = results.getString(results.getColumnIndex(RES_RES_PHONE));
            res_Waiting_Time = results.getString(results.getColumnIndex(RES_RES_WAITING_TIME));

            res_accept_cash = stringTobool(results.getString(results.getColumnIndex(RES_RES_ACCEPT_CASH)));
            res_accept_card = stringTobool(results.getString(results.getColumnIndex(RES_RES_ACCEPT_CARD)));
            res_offers_delivery = stringTobool(results.getString(results.getColumnIndex(RES_RES_OFFERS_DELIVERY)));
            res_offers_pickup = stringTobool(results.getString(results.getColumnIndex(RES_RES_OFFERS_PICKUP)));

            res_hours = new HashMap<>();
            res_hours.put("Monday",results.getString(results.getColumnIndex(RES_HOURS_MONDAY)));
            res_hours.put("Tuesday", results.getString(results.getColumnIndex(RES_HOURS_TUESDAY)));
            res_hours.put("Wednesday", results.getString(results.getColumnIndex(RES_HOURS_WEDNESDAY)));
            res_hours.put("Thursday", results.getString(results.getColumnIndex(RES_HOURS_THURSDAY)));
            res_hours.put("Friday", results.getString(results.getColumnIndex(RES_HOURS_FRIDAY)));
            res_hours.put("Saturday", results.getString(results.getColumnIndex(RES_HOURS_SATURDAY)));
            res_hours.put("Sunday", results.getString(results.getColumnIndex(RES_HOURS_SUNDAY)));

            res_food_types = results.getString(results.getColumnIndex(RES_RES_FOOD_TYPES));

            Restaurant thisRestaurant = new Restaurant(res_id, res_name, res_url, res_logo_url, res_address, res_city, res_latitude, res_longitude,
                    res_state, res_zip, res_phone, res_Waiting_Time, res_accept_cash, res_accept_card,
                    res_offers_delivery, res_offers_pickup, res_hours, res_food_types);

            restaurants.add(thisRestaurant);
        }
        return restaurants;
    }

    private Boolean stringTobool(String s){
        if (s.equalsIgnoreCase("true")){
            return true;
        }
        return false;
    }


}

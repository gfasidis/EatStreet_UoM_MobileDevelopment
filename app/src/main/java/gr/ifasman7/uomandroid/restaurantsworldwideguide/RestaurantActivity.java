package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class RestaurantActivity extends AppCompatActivity {

    private static final String TAG = "iFasMan";

    private static final String API_KEY = BuildConfig.ApiKey;

    private static FragmentManager fragmentManager;

    private Button mapBtn;
    private Button addToFavoritesBtn;
    private Button removeFromFavoritesBtn;
    private TextView resNameTextView;
    private ImageView resLogoImageView;

    /*
        Restaurant Buttons Fragment
     */
    private Button infoBtn;
    private Button menuBtn;

    private RestaurantInfoFragment infoFragment;
    private RestaurantMenuFragment menuFragment;

    private Restaurant thisRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        infoFragment = new RestaurantInfoFragment();
        menuFragment = new RestaurantMenuFragment();

        Intent thisIntent = getIntent();
        thisRestaurant = thisIntent.getParcelableExtra(RestaurantActivity.this.getString(R.string.Passing_Restaurant));

        createLayout();
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragmentLayout) != null){
            if(savedInstanceState != null)
                return;

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            putDataToFragment(infoFragment);
            fragmentTransaction.add(R.id.fragmentLayout,infoFragment,null);
            fragmentTransaction.commit();
        }

        DownloadMenuData downloadMenuData = new DownloadMenuData();
        downloadMenuData.execute("https://api.eatstreet.com/publicapi/v1/restaurant/" + thisRestaurant.getRes_id() + "/menu?includeCustomizations=false");

    }

    private void putDataToFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable(RestaurantActivity.this.getString(R.string.Passing_Restaurant),thisRestaurant);
        fragment.setArguments(bundle);
    }
    public void createLayout(){

        /*
            Fragment Buttons
         */
        infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Info");
                putDataToFragment(infoFragment);
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,infoFragment,null).commit();
            }
        });

        menuBtn = findViewById(R.id.menuInfo);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Menu");
                putDataToFragment(menuFragment);
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,menuFragment,null).commit();
            }
        });

        /*
            Rendering image from EatStreet's API with URL
         */
        resLogoImageView = findViewById(R.id.resImage);
        Picasso.get().load(thisRestaurant.getRes_logo_url()).resize(130,130).centerCrop().into(resLogoImageView);

        resNameTextView = findViewById(R.id.resNametxt);
        resNameTextView.setText(thisRestaurant.getRes_name());

        /*
            Checking in DB
         */
        addToFavoritesBtn = findViewById(R.id.addToFavbtn);
        removeFromFavoritesBtn = findViewById(R.id.removeFav);

        CheckInDataBase inDataBase = new CheckInDataBase();
        inDataBase.execute(AccessToken.getCurrentAccessToken().getUserId());

        /*
            Add to Favorites in Local DataBase
         */
        addToFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(RestaurantActivity.this);
                if(db.insertRestaurant(thisRestaurant)){
                    db.addRestaurantToUser(AccessToken.getCurrentAccessToken().getUserId(),thisRestaurant);
                    Toast.makeText(RestaurantActivity.this,"Added to favorites",Toast.LENGTH_SHORT).show();
                    db.close();
                    addToFavoritesBtn.setVisibility(View.INVISIBLE);
                    removeFromFavoritesBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        /*
            Remove from Favorites in Local DataBase
         */
        removeFromFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(RestaurantActivity.this);
                db.removeFavRestaurant(AccessToken.getCurrentAccessToken().getUserId(),thisRestaurant.getRes_id());
                Toast.makeText(RestaurantActivity.this, "Remove restaurant from favorites", Toast.LENGTH_SHORT).show();
                db.close();
                removeFromFavoritesBtn.setVisibility(View.INVISIBLE);
                addToFavoritesBtn.setVisibility(View.VISIBLE);
            }
        });

        /*
            Open in Maps with marker and restaurant's name
         */
        mapBtn = findViewById(R.id.openMapsbtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(Intent.ACTION_VIEW);
                mapActivity.setData(Uri.parse("geo:" + thisRestaurant.getRes_latitude() + "," + thisRestaurant.getRes_longitude() +
                        "?q=" + thisRestaurant.getRes_latitude() + "," + thisRestaurant.getRes_longitude() + "(" + thisRestaurant.getRes_name() + ")"));
                startActivity(mapActivity);
            }
        });

    }

    private class CheckInDataBase extends AsyncTask<String, Void, Boolean>{


        @Override
        protected void onPostExecute(Boolean isFav) {
            super.onPostExecute(isFav);

            if(isFav){
                addToFavoritesBtn.setVisibility(View.INVISIBLE);
                removeFromFavoritesBtn.setVisibility(View.VISIBLE);
            }
            else {
                addToFavoritesBtn.setVisibility(View.VISIBLE);
                removeFromFavoritesBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            DBHelper db = new DBHelper(RestaurantActivity.this);
            Boolean flag = db.checkConBetweenUserRestaurant(strings[0],thisRestaurant);
            db.close();

            return flag;
        }
    }

    private class DownloadMenuData extends AsyncTask<String, Void, String>{

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);

            JSONMenuParser parser = new JSONMenuParser();
            if (parser.parse(jsonData)){
                thisRestaurant.setRes_menu(parser.getMenuArrayList());

                for (int i = 0; i < thisRestaurant.getRes_menu().size(); i++){
                    Log.d(TAG, thisRestaurant.getRes_menu().get(i).toString());
                    Log.d(TAG, "----------------------------------------------------------------------------------");
                }
            }
            else{
                Log.d(TAG, "onPostExecute: Error Parsing");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground starts with: " + strings[0]);
            String menuData = downloadJSON(strings[0]);
            if (menuData == null)
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0]);

            return menuData;
        }

        private String downloadJSON(String urlPath) {

            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("X-Access-Token", API_KEY);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (line != null){
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }
                reader.close();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ",e);
            }

            return String.valueOf(sb);
        }
    }
}

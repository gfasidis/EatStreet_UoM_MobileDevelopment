package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class NearbyRestaurantActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private static final String TAG = "iFasMan";

    private static final String API_KEY = BuildConfig.ApiKey;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private Boolean mLocationPermissionsGranted = false;
    private Boolean mRequestingLocationUpdates = true;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Location currentLocation;
    private LocationCallback mLocationCallback;


    private static ArrayList<Restaurant> restaurants;
    private RestaurantAdapter restaurantAdapter;

    private ListView nearbyRestaurantsListView;
    private TextView emptyNearbyListTextView;

    private SearchView searchBarTextView;

    /*
        FaceBook Data
     */
    private TextView usersProfileNameTextView;
    private TextView usersProfileIdTextView;
    private TextView usersProfileEmailTextView;
    private ImageView usersImageView;

    private String userName;
    private String userID;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.nearby_res);
        setContentView(R.layout.activity_nearby_restaurant);

        /*
            Don't Change Anything // Start
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*
            Don't Change Anything // End
         */

        restaurants = new ArrayList<>();
        nearbyRestaurantsListView = findViewById(R.id.nearbyResListView);
        restaurantAdapter = new RestaurantAdapter(NearbyRestaurantActivity.this, R.layout.nearby_list_item, restaurants);
        nearbyRestaurantsListView.setAdapter(restaurantAdapter);
        nearbyRestaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent restaurantActivity = new Intent(NearbyRestaurantActivity.this, RestaurantActivity.class);

                Restaurant selectedRestaurant = restaurantAdapter.getFilteredRestaurants().get(position);
                restaurantActivity.putExtra(NearbyRestaurantActivity.this.getString(R.string.Passing_Restaurant),selectedRestaurant);

                startActivity(restaurantActivity);
            }
        });
        emptyNearbyListTextView = findViewById(R.id.emptyNearbyList);
        nearbyRestaurantsListView.setEmptyView(emptyNearbyListTextView);

        compileLayout();
    }

    private void compileLayout() {
        Log.d(TAG, "compileLayout: Request Location Permission");
        checkLocationPermission();
        if (mLocationPermissionsGranted) {
            createLocationRequest();
            getDeviceLocation();
        } else {
            Toast.makeText(NearbyRestaurantActivity.this, "Unable to get restaurants!", Toast.LENGTH_SHORT).show();
        }
        getUserFaceBookData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "onBackPressed: nothing happens");
            /*
                Disable Back Button
             */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchBarTextView = (SearchView) searchMenuItem.getActionView();

        searchBarTextView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBarTextView.setSubmitButtonEnabled(true);
        searchBarTextView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            // Handle the fav action
            favListAction();

        } else if (id == R.id.nav_fb_logout) {
            // Handle Logout Action
            logoutAction();

        } else if(id == R.id.nav_support){
            // Handle Support Action
            supportAction();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        Menu Actions // START
     */
    private void favListAction(){
        Intent favActivity = new Intent(NearbyRestaurantActivity.this, FavoritesActivity.class);
        startActivity(favActivity);
    }
    private void logoutAction(){
        disconnectFromFacebook();

        Log.d(TAG, "onNavigationItemSelected: Logout Action: Logout Done");
        Toast.makeText(NearbyRestaurantActivity.this, "Log out Successfully",Toast.LENGTH_SHORT).show();

        Intent backToLoginActivity = new Intent(NearbyRestaurantActivity.this,LoginActivity.class);
        startActivity(backToLoginActivity);
    }

    private void disconnectFromFacebook() {
        AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
        if (currentAccessToken == null) {
            return; // already logged out
        }

        new GraphRequest(currentAccessToken, "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }


    private void supportAction(){
        Intent emailActivity = new Intent(Intent.ACTION_SENDTO);
        emailActivity.setData(Uri.parse(Uri.parse("mailto:" + NearbyRestaurantActivity.this.getString(R.string.support_email))
                + "?subject=" + "Feedback: " + "&body=" + ""));
        startActivity(emailActivity);
    }

    /*
        Menu Actions // END
     */


    /*
       When Location change // START
    */
    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Log.d(TAG, "onLocationResult: Checking");
                if(locationChanged(locationResult)){
                    /*
                        Simulate walking! While walking location changed
                     */
                    for (Location location : locationResult.getLocations()) {
                        Log.d(TAG, "onLocationResult: " + location.toString());
                        currentLocation = location;
                        downloadDataViaLocation(currentLocation);
                    }

                }
                else{
                    Log.d(TAG, "onLocationResult: Nothing changed");
                    return;
                }
            }
        };

        Log.d(TAG, "startLocationUpdates: Location request");
        mFusedLocationProviderClient = new FusedLocationProviderClient(NearbyRestaurantActivity.this);
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback,null);
    }

    private Boolean locationChanged(LocationResult locationResult){

        Log.d(TAG, "locationChanged: " + locationResult.getLastLocation().getLongitude() + ", " + locationResult.getLastLocation().getLatitude());
        if(locationResult.getLastLocation().getLatitude() == currentLocation.getLatitude()){
            if(locationResult.getLastLocation().getLongitude() == currentLocation.getLongitude()){
                Log.d(TAG, "locationChanged: false");
                return false;
            }
            else {
                Log.d(TAG, "locationChanged: true");
                return true;
            }
        }
        else{
            Log.d(TAG, "locationChanged: true");
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
    /*
        When Location change // END
     */

    private void checkLocationPermission(){
        Log.d(TAG, "checkLocationPermission: getting location permissions");

        /* Permissions table with requesting permissions */
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /*
        Calling this function if permission is not granted!
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");

        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;

                    createLocationRequest();
                    startLocationUpdates();
                    getDeviceLocation();
                }
            }
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found location!");
                            currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                Log.d(TAG, "onComplete: current location != null");
                                downloadDataViaLocation(currentLocation);
                            } else {
                                /*
                                    Fused Location Provider Only Works AFTER maps or something like that has opened previously
                                    Added this to avoid crashing with null pointer exception in AVD
                                 */
                                Log.d(TAG, "onComplete: current location = null");
                                Location defaultLocation = new Location("");
                                defaultLocation.setLatitude(0.1);
                                defaultLocation.setLongitude(0.1);
                                currentLocation = defaultLocation;
                                downloadDataViaLocation(currentLocation);
                            }

                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(NearbyRestaurantActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    /*
        Calling AsyncTask classes
     */

    private void downloadDataViaLocation(Location location){
        Log.d(TAG, "downloadDataViaLocation: starting an async Task....");
        DownloadNearbyRestaurantsData downloadNearbyRestaurantsData = new DownloadNearbyRestaurantsData();
        downloadNearbyRestaurantsData.execute("https://api.eatstreet.com/publicapi/v1/restaurant/search?latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude() + "&method=both");

    }

    private void getUserFaceBookData() {
        Log.d(TAG, "getUserFaceBookData: starting an async Task....");
        DownloadFaceBookData downloadObject = new DownloadFaceBookData();
        downloadObject.execute();
    }


    /*
        Search Methods
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        restaurantAdapter.getFilter().filter(newText);
        return false;
    }

    /*
        JSON Data, AsyncTask classes
     */

    private class DownloadNearbyRestaurantsData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            restaurantAdapter.clear();

            JSONNearbyRestaurantParser parser = new JSONNearbyRestaurantParser();
            if (parser.parse(jsonData)){
                restaurants = parser.getRestaurants();

                for (int i = 0; i < restaurants.size(); i++){
                    Log.d(TAG, restaurants.get(i).toString());
                    Log.d(TAG, "----------------------------------------------------------------------------------");
                }
                restaurantAdapter.refreshRestaurants(restaurants);
            }
            else{
                Log.d(TAG, "onPostExecute: Error Parsing");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground starts with: " + strings[0]);

            String restaurantData = downloadJSON(strings[0]);
            if (restaurantData == null)
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0]);

            return restaurantData;
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


    private class DownloadFaceBookData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final String USER_NAME = "name";
            final String USER_ID = "id";
            final String USER_PICTURE = "picture";
            final String USER_EMAIL = "email";

            final AccessToken accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d(TAG, "onCompleted: getUserFaceBookData" + response.toString());
                    try {
                        //User's Name
                        userName = object.getString(USER_NAME);
                        usersProfileNameTextView = findViewById(R.id.usersProfileNameTextView);
                        usersProfileNameTextView.setText(userName);

                        //User's ID
                        userID = object.getString(USER_ID);
                        usersProfileIdTextView = findViewById(R.id.usersProfileIdTextView);
                        usersProfileIdTextView.setText(userID);

                        //User's Email
                        if(object.has(USER_EMAIL)) {
                            userEmail = object.getString(USER_EMAIL);
                            usersProfileEmailTextView = findViewById(R.id.usersProfileEmailTextView);
                            usersProfileEmailTextView.setText(userEmail);
                        }

                        //User's Profile Picture
                        String imageURL = object.getJSONObject(USER_PICTURE).getJSONObject("data").getString("url");
                        usersImageView = findViewById(R.id.usersImageView);
                        Picasso.get().load(imageURL).resize(120,120).transform(new CirlceTransformation()).into(usersImageView);

                    } catch (JSONException e) {
                        Log.e(TAG, "onCompleted: getUserFaceBookData Parse error", e);
                    }

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,picture.type(large),email");
            request.setParameters(parameters);
            request.executeAsync();
            return null;
        }
    }
}

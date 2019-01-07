package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.AccessToken;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "iFasMan";

    private FavoritesAdapter favoritesAdapter;
    private ListView favoriteRestaurantsListView;
    private SearchView searchBarTextView;

    private TextView emptyFavoritesListTextView;

    private ArrayList<Restaurant> favoriteRestaurants;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.Fav_Res));
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoriteRestaurants = new ArrayList<>();
        favoriteRestaurantsListView = findViewById(R.id.favResListView);
        favoritesAdapter = new FavoritesAdapter(FavoritesActivity.this, R.layout.nearby_list_item, favoriteRestaurants);
        favoriteRestaurantsListView.setAdapter(favoritesAdapter);
        favoriteRestaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent restaurantActivity = new Intent(FavoritesActivity.this, RestaurantActivity.class);

                Restaurant selectedRestaurant = favoritesAdapter.getFilteredRestaurants().get(position);
                restaurantActivity.putExtra(FavoritesActivity.this.getString(R.string.Passing_Restaurant),selectedRestaurant);

                startActivity(restaurantActivity);
            }
        });
        /*
            Show message when the list view is empty
         */
        emptyFavoritesListTextView = findViewById(R.id.emptyFavoritesList);
        favoriteRestaurantsListView.setEmptyView(emptyFavoritesListTextView);

        userId = AccessToken.getCurrentAccessToken().getUserId();
        Log.d(TAG, "onCreate: userID = " + userId);

        getFavorites(userId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFavorites(String userId) {
        FavoriteRestaurantsDB data = new FavoriteRestaurantsDB();
        data.execute(userId);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getFavorites(userId);
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        favoritesAdapter.getFilter().filter(newText);
        return false;
    }

    private class FavoriteRestaurantsDB extends AsyncTask<String, Void, ArrayList<Restaurant>>{

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurantArrayList) {
            super.onPostExecute(restaurantArrayList);
            favoriteRestaurants = restaurantArrayList;

            for (int i = 0; i < favoriteRestaurants.size(); i++){
                Log.d(TAG, favoriteRestaurants.get(i).toString());
                Log.d(TAG, "----------------------------------------------------------------------------------");
            }
            favoritesAdapter.refreshRestaurants(favoriteRestaurants);
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            DBHelper db = new DBHelper(FavoritesActivity.this);
            ArrayList<Restaurant> restaurants = db.getFavoriteRestaurant(strings[0]);
            db.close();
            return restaurants;
        }
    }

}

package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantMenuFragment extends Fragment {

    private static final String TAG = "iFasMan";

    private ListView menuListView;
    private Spinner menuSpinner;

    private Restaurant thisRestaurant;
    private List<String> categories;
    private List<RestaurantMenuItem> menuItems;
    private ArrayList<RestaurantMenu> menu;

    private ArrayAdapter<String> spinnerAdapter;
    private MenuAdapter menuAdapter;

    public RestaurantMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        int position = getArguments().getInt("position");
        thisRestaurant = RestaurantAdapter.getFilteredRestaurants().get(position);
        menu = new ArrayList<>();
        menu = thisRestaurant.getRes_menu();

        createLayout(view);

        return view;
    }

    private void createLayout(View view){

        menuListView = view.findViewById(R.id.menuListView);
        menuSpinner = view.findViewById(R.id.categSpinner);

        /*
            Categories Spinner
         */
        categories = RestaurantMenu.getCategories(menu);
        Log.d(TAG, "onCreateView: " + categories.toString());

        spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        menuSpinner.setAdapter(spinnerAdapter);

        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categories.get(position);
                menuItems = RestaurantMenu.getItemsByCategory(category,menu);

                menuAdapter = new MenuAdapter(getActivity(),R.layout.menu_list_item,menuItems);
                menuListView.setAdapter(menuAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: Nothing happen");
            }
        });


    }

}

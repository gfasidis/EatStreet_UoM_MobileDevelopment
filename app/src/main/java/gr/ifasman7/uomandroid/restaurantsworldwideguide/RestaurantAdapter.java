package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    private static final String TAG = "iFasMan";

    private final LayoutInflater inflater;
    private final int layoutResource;
    private List<Restaurant> nearbyRestaurants;

    private List<Restaurant> filteredRestaurants;

    private Filter resFilter;

    public RestaurantAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        nearbyRestaurants = objects;
        filteredRestaurants = objects;

        getFilter();
    }

    @Override
    public int getCount() {
        return filteredRestaurants.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        /*
            Works only with filteredRestaurants
         */
        Restaurant restaurant = filteredRestaurants.get(position);

        viewHolder.resName.setText(restaurant.getRes_name());
        viewHolder.resAddress.setText(restaurant.getRes_address());
        viewHolder.resFoodTypes.setText(restaurant.getRes_food_types());
        Picasso.get().load(restaurant.getRes_logo_url()).resize(80,80).centerCrop().into(viewHolder.resLogo);

        return convertView;
    }

    public List<Restaurant> getFilteredRestaurants() {
        return filteredRestaurants;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(resFilter == null){
            resFilter = new RestaurantNameFilter();
        }
        return resFilter;
    }

    public void refreshRestaurants(ArrayList<Restaurant> restaurants){
        this.filteredRestaurants.clear();
        this.filteredRestaurants.addAll(restaurants);
        notifyDataSetChanged();

    }

    private class RestaurantNameFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(TextUtils.isEmpty(constraint)){
                filterResults.values = nearbyRestaurants;
                filterResults.count = nearbyRestaurants.size();

            }else {
                List<Restaurant> newResList = new ArrayList<Restaurant>();
                for (int i = 0; i < nearbyRestaurants.size(); i++){
                    Restaurant restaurant = nearbyRestaurants.get(i);
                    if (restaurant.getRes_name().toLowerCase().contains(constraint.toString().toLowerCase()))
                        newResList.add(restaurant);
                }
                filterResults.values = newResList;
                filterResults.count = newResList.size();

            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredRestaurants = (List<Restaurant>) results.values;
            notifyDataSetChanged();

        }
    }

    private class ViewHolder{

        final ImageView resLogo;
        final TextView resName;
        final TextView resAddress;
        final TextView resFoodTypes;

        ViewHolder(View view){
            resName =view.findViewById(R.id.resNameTextView);
            resAddress = view.findViewById(R.id.resAddressTextView);
            resFoodTypes = view.findViewById(R.id.resFoodTypesTextView);
            resLogo = view.findViewById(R.id.logoImageView);
        }
    }
}

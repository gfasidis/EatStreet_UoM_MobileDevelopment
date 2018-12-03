package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MenuAdapter extends ArrayAdapter<RestaurantMenuItem> {

    private static final String TAG = "iFasMan";

    private final LayoutInflater inflater;
    private final int layoutResource;
    private List<RestaurantMenuItem> restaurantMenu;

    public MenuAdapter(@NonNull Context context, int resource, @NonNull List<RestaurantMenuItem> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        restaurantMenu = objects;
    }

    @Override
    public int getCount() {
        return restaurantMenu.size();
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

        RestaurantMenuItem menuItem = restaurantMenu.get(position);

        viewHolder.itemName.setText(menuItem.getItemName());
        viewHolder.itemDesc.setText(menuItem.getItemDescription());
        viewHolder.itemPrice.setText(String.valueOf(menuItem.getItemPrice()));

        return convertView;
    }

    private class ViewHolder{

        final TextView itemName;
        final TextView itemDesc;
        final TextView itemPrice;

        ViewHolder(View view){
            itemName = view.findViewById(R.id.menuItemNameTextView);
            itemDesc = view.findViewById(R.id.menuItemDescriptionTextView);
            itemPrice = view.findViewById(R.id.menuItemPriceTextView);
        }
    }
}

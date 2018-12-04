package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantInfoFragment extends Fragment {

    private static final String TAG = "iFasMan";

    private static final String API_KEY = BuildConfig.ApiKey;

    private static final int CALL_PHONE_REQUEST_CODE = 2525;
    private Boolean CALL_PHONE_PERMISSION_GRANTED = false;
    private Intent phoneCallActivity;

    /*
        Restaurant Info:
     */
    private TextView resAddressTextView;
    private Button resURLbtn;
    private Button phoneCallbtn;
    private TextView resCashTextView;
    private TextView resCreditCardTextView;
    private TextView resDeliveryTextView;
    private TextView resPickUpTextView;
    private TextView resWaitingTimeTextView;

    /*
        Restaurant Hours
     */
    private TextView resMondayHoursTextView;
    private TextView resTuesdayHoursTextView;
    private TextView resWednesdayHoursTextView;
    private TextView resThursdayHoursTextView;
    private TextView resFridayHoursTextView;
    private TextView resSaturdayHoursTextView;
    private TextView resSundayHoursTextView;

    /*
        Restaurant Food Types
     */
    private TextView resFoodTypesTextView;

    /*
        Selected Restaurant
    */
    private Restaurant thisRestaurant;


    public RestaurantInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        thisRestaurant = getArguments().getParcelable(RestaurantInfoFragment.this.getString(R.string.Passing_Restaurant));

        createLayout(view);

        return view;
    }

    private void createLayout(View view) {

        resAddressTextView = view.findViewById(R.id.resFullAddresstxt);
        resAddressTextView.setText(thisRestaurant.getRes_address() + ",  " + thisRestaurant.getRes_city() + ",  " + thisRestaurant.getRes_state());

        /*
            Open URL in web broswer
         */
        resURLbtn = view.findViewById(R.id.resURLbtn);
        resURLbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openWebPage = new Intent(Intent.ACTION_VIEW);
                openWebPage.setData(Uri.parse(thisRestaurant.getRes_url()));
                startActivity(openWebPage);
            }
        });

        /*
            Phone call directly with button
         */
        phoneCallbtn = view.findViewById(R.id.resPhonebtn);
        phoneCallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPhoneCall(thisRestaurant.getRes_phone());
            }
        });

        resCashTextView = view.findViewById(R.id.resCashtxt);
        resCashTextView.setText(booleanToString(thisRestaurant.getRes_accept_cash()));

        resCreditCardTextView = view.findViewById(R.id.resCardtxt);
        resCreditCardTextView.setText(booleanToString(thisRestaurant.getRes_accept_card()));

        resDeliveryTextView = view.findViewById(R.id.resDeliverytxt);
        resDeliveryTextView.setText(booleanToString(thisRestaurant.getRes_offers_delivery()));

        resPickUpTextView = view.findViewById(R.id.resPickUptxt);
        resPickUpTextView.setText(booleanToString(thisRestaurant.getRes_offers_pickup()));

        resWaitingTimeTextView = view.findViewById(R.id.resWaitingTimetxt);
        resWaitingTimeTextView.setText(thisRestaurant.getRes_Waiting_Time() + " min");

        Map<String,String> resHours = thisRestaurant.getRes_hours();

        resMondayHoursTextView = view.findViewById(R.id.resMondaytxt);
        resMondayHoursTextView.setText(resHours.get("Monday"));

        resTuesdayHoursTextView = view.findViewById(R.id.resTuesdaytxt);
        resTuesdayHoursTextView.setText(resHours.get("Tuesday"));

        resWednesdayHoursTextView = view.findViewById(R.id.resWednesdaytxt);
        resWednesdayHoursTextView.setText(resHours.get("Wednesday"));

        resThursdayHoursTextView = view.findViewById(R.id.resThursdaytxt);
        resThursdayHoursTextView.setText(resHours.get("Thursday"));

        resFridayHoursTextView = view.findViewById(R.id.resFridaytxt);
        resFridayHoursTextView.setText(resHours.get("Friday"));

        resSaturdayHoursTextView = view.findViewById(R.id.resSaturdaytxt);
        resSaturdayHoursTextView.setText(resHours.get("Saturday"));

        resSundayHoursTextView = view.findViewById(R.id.resSundaytxt);
        resSundayHoursTextView.setText(resHours.get("Sunday"));

        resFoodTypesTextView = view.findViewById(R.id.resFoodstxt);
        resFoodTypesTextView.setText(thisRestaurant.getRes_food_types());

    }

    private String booleanToString(Boolean flag){
        return flag ? "Yes" : "No";
    }

    private void attemptPhoneCall(final String res_phone) {

        AlertDialog.Builder callDialog = new AlertDialog.Builder(getActivity());
        callDialog.setCancelable(true);

        callDialog.setMessage(res_phone);
        callDialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                phoneCallActivity = new Intent(Intent.ACTION_CALL);
                phoneCallActivity.setData(Uri.parse("tel:" + res_phone));

                checkPhoneCallPermission();
                if(CALL_PHONE_PERMISSION_GRANTED){
                    startPhoneCallActivity();
                }
                else {
                    Log.d(TAG, "attemptPhoneCall permission not granted!! ");
                }
            }
        });
        callDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        callDialog.create();
        callDialog.show();
    }

    private void checkPhoneCallPermission() {
        Log.d(TAG, "checkPhoneCallPermission: Getting Phone Call permissions");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPhoneCallPermission: Request Permissions");
            String[] permissions = {Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(getActivity(), permissions, CALL_PHONE_REQUEST_CODE);
        }
        else {
            Log.d(TAG, "checkPhoneCallPermission: permission is granted");
            CALL_PHONE_PERMISSION_GRANTED = true;
        }
    }

    private void startPhoneCallActivity(){
        Log.d(TAG, "startPhoneCallActivity: Starting Activity");
        startActivity(phoneCallActivity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CALL_PHONE_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    CALL_PHONE_PERMISSION_GRANTED = true;
                    startPhoneCallActivity();
                }
                else{
                    CALL_PHONE_PERMISSION_GRANTED = false;
                }
                break;
        }
    }
}

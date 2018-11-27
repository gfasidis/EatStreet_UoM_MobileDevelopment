package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "iFasMan";

    /*
       Facebook Login Objects // Start
    */
    private CallbackManager callbackManager;
    private Button continueFBbtn;
    private boolean isLoggedIn;
    private TextView welcBacktxt;
    /*
       Facebook Login Objects // End
    */

    private ImageButton infoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
            Facebook Login Objects // Start
         */
        callbackManager = CallbackManager.Factory.create();
        /*
            Facebook Login Objects // End
         */

        setContentView(R.layout.activity_login);

        /*
            Facebook Login Objects // Start
         */

        Log.d(TAG, "onCreate: test");

        welcBacktxt = findViewById(R.id.welcBacktxt);
        continueFBbtn = findViewById(R.id.loggedINbtn);

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    LoginManager.getInstance().logOut();
                    Log.d(TAG, "onLogout catched");
                    continueFBbtn.setVisibility(View.INVISIBLE);
                    welcBacktxt.setVisibility(View.INVISIBLE);
                    loginAttempt();
                }
            }
        };
        loginAttempt();
        /*
            Facebook Login Objects // End
         */

        infoBtn = findViewById(R.id.infoButton);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: LoginActivity: Info Button: Just click my info button");

                Intent infoActivity = new Intent(LoginActivity.this, InfoActivity.class);
                startActivity(infoActivity);


            }
        });

        Log.d(TAG, "onCreate: test2");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }

    private void loginAttempt(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (!isLoggedIn){
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                            Intent nearbyRestaurantActivity = new Intent(LoginActivity.this, NearbyRestaurantActivity.class);
                            startActivity(nearbyRestaurantActivity);

                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "onCancel: ");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e(TAG, "onError: Login",exception );
                        }
                    });

        }else {
            welcBacktxt.setVisibility(View.VISIBLE);
            continueFBbtn.setVisibility(View.VISIBLE);
            continueFBbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "id"));
                    Intent nearbyRestaurantActivity = new Intent(LoginActivity.this, NearbyRestaurantActivity.class);
                    startActivity(nearbyRestaurantActivity);

                }
            });
        }
    }

}

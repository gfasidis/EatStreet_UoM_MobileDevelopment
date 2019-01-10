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
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "iFasMan";

    /*
       Facebook Login Objects // Start
    */
    private CallbackManager callbackManager;
    private Button continueFaceBookBtn;
    private boolean isLoggedIn;
    private TextView welcomeBackTextView;
    private LoginButton loginButton;

    /*
       Facebook Login Objects // End
    */

    private ImageButton infoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
            Facebook Login Objects // Start
         */
        callbackManager = CallbackManager.Factory.create();

        welcomeBackTextView = findViewById(R.id.welcBacktxt);
        continueFaceBookBtn = findViewById(R.id.loggedINbtn);

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    LoginManager.getInstance().logOut();
                    Log.d(TAG, "onCurrentAccessTokenChanged: onLogout catched");
                    continueFaceBookBtn.setVisibility(View.INVISIBLE);
                    welcomeBackTextView.setVisibility(View.INVISIBLE);
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
                Intent infoActivity = new Intent(LoginActivity.this, InfoActivity.class);
                startActivity(infoActivity);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }

    private void loginAttempt(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        
        if (!isLoggedIn){
            loginButton = findViewById(R.id.login_button);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
                public void onError(FacebookException error) {
                    Log.e(TAG, "onError: Login",error );
                }
            });
        }else {
            welcomeBackTextView.setVisibility(View.VISIBLE);
            continueFaceBookBtn.setVisibility(View.VISIBLE);
            continueFaceBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                    Intent nearbyRestaurantActivity = new Intent(LoginActivity.this, NearbyRestaurantActivity.class);
                    startActivity(nearbyRestaurantActivity);

                }
            });
        }
    }

}

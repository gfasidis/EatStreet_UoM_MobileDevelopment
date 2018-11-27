package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoActivity extends AppCompatActivity {

    private Button supportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("About " + this.getString(R.string.app_name));

        supportButton = findViewById(R.id.supportBtn);
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailActivity = new Intent(Intent.ACTION_SENDTO);
                emailActivity.setData(Uri.parse(Uri.parse("mailto:" + InfoActivity.this.getString(R.string.support_email))
                        + "?subject=" + "Feedback: " + "&body=" + ""));
                startActivity(emailActivity);
            }
        });
    }
}

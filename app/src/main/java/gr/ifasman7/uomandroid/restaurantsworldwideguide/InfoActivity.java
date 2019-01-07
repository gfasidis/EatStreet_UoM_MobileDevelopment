package gr.ifasman7.uomandroid.restaurantsworldwideguide;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class InfoActivity extends AppCompatActivity {

    private Button supportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}

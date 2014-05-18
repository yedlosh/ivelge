package cz.ctu.pda.ivelge;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        EditText serverAddress = (EditText) findViewById(R.id.server_address);
        serverAddress.setHint(R.string.ip_hint);

        SharedPreferences sp = getSharedPreferences("ivelge", MODE_PRIVATE);

        if (sp.contains("serverIP")) {
            serverAddress.setText(sp.getString("serverIP", "none"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveSettings(View view) {
        EditText serverAddress = (EditText) findViewById(R.id.server_address);
        android.util.Log.i(SettingsActivity.class.getName(), "Saving ip address: " + serverAddress.getText().toString());

        SharedPreferences sp = getSharedPreferences("ivelge", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("serverIP", serverAddress.getText().toString());
        ed.commit();

        NavUtils.navigateUpFromSameTask(this);
    }
}

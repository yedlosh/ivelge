package cz.ctu.pda.ivelge;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EventDetailActivity extends ActionBarActivity {

    private LogDataSource logDataSource;
    private CategoryDataSource categoryDataSource;
    private long logId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle b = getIntent().getExtras();
        this.logId = b.getLong("logId");
        logDataSource.open();
        categoryDataSource.open();
        Log log = logDataSource.getLog(logId);

        String title = "Task " + log.getTaskIndex() + " Event";

        setActionBar(title);
        setData(log);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_detail, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        logDataSource.close();
        categoryDataSource.close();
        super.onDestroy();
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

    private void setActionBar(String title) {
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.event_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.event_actionBarTitle);
        mTitleTextView.setText(title);

        Button button = (Button) mCustomView
                .findViewById(R.id.event_actionBarButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void setData(Log log) {
        Category category = categoryDataSource.getCategory(log.getCategoryId());
        String subCategory = category.getSubcategory(log.getSubcategoryIndex());

        TextView categoryLabel = (TextView) findViewById(R.id.eventCategory);
        categoryLabel.setText(category.getName());

        TextView subCategoryLabel = (TextView) findViewById(R.id.eventSubCategory);
        subCategoryLabel.setText(subCategory);

        TextView descriptionLabel = (TextView) findViewById(R.id.eventDescription);
        descriptionLabel.setText(log.getDescription());

        TextView priorityLabel = (TextView) findViewById(R.id.eventPriority);
        priorityLabel.setText(log.getPriority());

        TextView taskLabel = (TextView) findViewById(R.id.eventTask);
        taskLabel.setText("Task " + log.getTaskIndex());

        TextView timeLabel = (TextView) findViewById(R.id.eventTime);
        timeLabel.setText(new Long(log.getTimestamp()).toString());

        ImageView image = (ImageView) findViewById(R.id.eventImage);
        Bitmap bitmap = BitmapFactory.decodeFile(log.getPhoto().getAbsolutePath());
        image.setImageBitmap(bitmap);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, EventEditActivity.class);
        intent.putExtra("logId", logId);
        startActivity(intent);
    }
}

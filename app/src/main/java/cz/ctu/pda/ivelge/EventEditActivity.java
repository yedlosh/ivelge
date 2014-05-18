package cz.ctu.pda.ivelge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EventEditActivity extends ActionBarActivity {

    private LogDataSource logDataSource;
    private SessionDataSource sessiomDataSource;
    private TestDataSource testDataSource;
    private CategoryDataSource categoryDataSource;
    private long logId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        Bundle b = getIntent().getExtras();
        this.logId = b.getLong("logId");
        logDataSource.open();
        categoryDataSource.open();
        testDataSource.open();
        sessiomDataSource.open();


        Log log = logDataSource.getLog(logId);

        setData(log);
    }

    @Override
    public void onDestroy() {
        logDataSource.close();
        categoryDataSource.close();
        testDataSource.close();
        sessiomDataSource.close();

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_edit, menu);
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

    public void setData(Log log) {

        Category category = categoryDataSource.getCategory(log.getCategoryId());
        String subCategory = category.getSubcategory(log.getSubcategoryIndex());

        //Category
        Spinner categorySpinner = (Spinner) findViewById(R.id.edit_Category);
        List<String> list = CommonUttils.categoryListToString(categoryDataSource.getAllCategories());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        categorySpinner.setSelection(list.indexOf(category.getName()));

        //SubCategory


        Spinner subSpinner = (Spinner) findViewById(R.id.edit_SubCategory);
        List<String> subList = category.getSubcategories();
        ArrayAdapter<String> sDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subList);
        sDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(sDataAdapter);

        subSpinner.setSelection(log.getSubcategoryIndex());


        //Priority

        Spinner prioritySpinner = (Spinner) findViewById(R.id.edit_Priority);
        List<String> priorityList = CommonUttils.getAllPriorityInString();
        ArrayAdapter<String> prDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, priorityList);
        prDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(prDataAdapter);

        prioritySpinner.setSelection(priorityList.indexOf(log.getPriority()));

        //Task
        Session session=sessiomDataSource.getSession(log.getSessionId());
        Test test=testDataSource.getTest(session.getTestId());

        Spinner taskSpinner = (Spinner) findViewById(R.id.edit_Task);
        List<String> taskList = test.getTasks();
        ArrayAdapter<String> tDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, taskList);
        tDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(tDataAdapter);

        taskSpinner.setSelection(log.getTaskIndex());

       EditText description=(EditText) findViewById(R.id.edit_Description);
        description.setText(log.getDescription());

        ImageView image = (ImageView) findViewById(R.id.edit_Img);
        Bitmap bitmap = BitmapFactory.decodeFile(log.getPhoto().getAbsolutePath());
        image.setImageBitmap(bitmap);

    }


}

package cz.ctu.pda.ivelge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;


public class EventEditActivity extends ActionBarActivity {

    private LogDataSource logDataSource;
    private SessionDataSource sessionDataSource;
    private TestDataSource testDataSource;
    private CategoryDataSource categoryDataSource;
    private long logId;
    private long testId;
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        logDataSource = new LogDataSource(this);
        testDataSource = new TestDataSource(this);
        categoryDataSource = new CategoryDataSource(this);
        sessionDataSource = new SessionDataSource(this);

        Bundle b = getIntent().getExtras();
        this.logId = b.getLong("logId");
        logDataSource.open();
        categoryDataSource.open();
        testDataSource.open();
        sessionDataSource.open();


        Log log = logDataSource.getLog(logId);

        setData(log);

        Spinner spinner = (Spinner) findViewById(R.id.edit_Category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Category category=test.getCategories().get(position);
                reloadSubCategory(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public void onDestroy() {
        logDataSource.close();
        categoryDataSource.close();
        testDataSource.close();
        sessionDataSource.close();

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

        Session session= sessionDataSource.getSession(log.getSessionId());
        test = testDataSource.getTest(session.getTestId());
        testId = test.getId();

        //Category
        Spinner categorySpinner = (Spinner) findViewById(R.id.edit_Category);
        List<String> list = CommonUttils.categoryListToString(test.getCategories());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        categorySpinner.setSelection(list.indexOf(category.getName()));

        //SubCategory


        Spinner subSpinner = (Spinner) findViewById(R.id.edit_SubCategory);
        List<String> subList = category.getSubcategories();
        ArrayAdapter<String> sDataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, subList);
        sDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(sDataAdapter);

        subSpinner.setSelection(log.getSubcategoryIndex());


        //Priority

        Spinner prioritySpinner = (Spinner) findViewById(R.id.edit_Priority);
        List<String> priorityList = CommonUttils.getAllPriorityInString();
        ArrayAdapter<String> prDataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, priorityList);
        prDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(prDataAdapter);

        prioritySpinner.setSelection(priorityList.indexOf(log.getPriority()));

        //Task
        Spinner taskSpinner = (Spinner) findViewById(R.id.edit_Task);
        List<String> taskList = test.getTasks();
        ArrayAdapter<String> tDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, taskList);
        tDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(tDataAdapter);

        taskSpinner.setSelection(log.getTaskIndex());

       EditText description=(EditText) findViewById(R.id.edit_Description);
        description.setText(log.getDescription());

/*        ImageView image = (ImageView) findViewById(R.id.edit_Img);
        Bitmap bitmap = BitmapFactory.decodeFile(log.getPhoto().getAbsolutePath());
        image.setImageBitmap(bitmap);*/

    }

    public void saveLog(View view) {
        Log log = logDataSource.getLog(logId);

        Spinner categorySpinner = (Spinner) findViewById(R.id.edit_Category);
        Integer categoryPos=categorySpinner.getSelectedItemPosition();
        Category category=categoryDataSource.getAllCategories().get(categoryPos);
        log.setCategoryId(category.getId());

        Spinner subSpinner = (Spinner) findViewById(R.id.edit_SubCategory);
        Integer subPos=subSpinner.getSelectedItemPosition();
        log.setSubcategoryIndex(subPos);

        Spinner prioritySpinner = (Spinner) findViewById(R.id.edit_Priority);
        Integer prPos=prioritySpinner.getSelectedItemPosition();
        log.setPriority(prPos + 1);

        Spinner taskSpinner = (Spinner) findViewById(R.id.edit_Task);
        Integer tPos=taskSpinner.getSelectedItemPosition();
        log.setTaskIndex(tPos);

        EditText description=(EditText) findViewById(R.id.edit_Description);
        log.setDescription(description.getText().toString());

        //  ImageView image = (ImageView) findViewById(R.id.edit_Img);

        logDataSource.commitLog(log);
        resetLog(view);


    }

    public void resetLog(View view) {
        Log log = logDataSource.getLog(logId);
        Intent intent = new Intent(this, SessionActivity.class);
        intent.putExtra("sessionId", log.getSessionId());
        intent.putExtra("testId",testId);
        startActivity(intent);
    }

    private void reloadSubCategory(Category category){
        Spinner subSpinner = (Spinner) findViewById(R.id.edit_SubCategory);
        List<String> subList = category.getSubcategories();
        ArrayAdapter<String> sDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subList);
        sDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(sDataAdapter);
    }


}

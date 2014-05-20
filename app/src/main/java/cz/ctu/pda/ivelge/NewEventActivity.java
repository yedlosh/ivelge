package cz.ctu.pda.ivelge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class NewEventActivity extends ActionBarActivity {
    private int selectedTaskIndex = 0;
    private TestDataSource testDataSource;
    private CategoryDataSource categoryDataSource;
    private long testId;
    private long sessionId;
    private List<Category> categories;
    private int selectedCategory = 0;
    private Spinner subcategorySpinner;
    private ArrayAdapter<String> subcategoryAdapter;
    private double latitude;
    private double longitude;
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_event);

        Bundle b = getIntent().getExtras();
        testId = b.getLong("testId");
        sessionId = b.getLong("sessionId");
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");

        testDataSource = new TestDataSource(this);
        testDataSource.open();
        test = testDataSource.getTest(testId);

        categoryDataSource = new CategoryDataSource(this);
        categoryDataSource.open();
        categories = test.getCategories();

        //selectedTaskIndex = b.getInt("selectedTaskIndex");

        Spinner taskSpinner = (Spinner) findViewById(R.id.new_event_task_spinner);
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test.getTasks());
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setSelection(selectedTaskIndex);

        Spinner prioritySpinner = (Spinner) findViewById(R.id.new_event_priority_spinner);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,R.array.priority_array, android.R.layout.simple_spinner_item);
        prioritySpinner.setAdapter(priorityAdapter);

        List<String> categoryList = new ArrayList<String>();
        for(Category cat : categories){
            categoryList.add(cat.getName());
        }
        Spinner categorySpinner = (Spinner) findViewById(R.id.new_event_category_spinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categorySpinner.setAdapter(categoryAdapter);
        //categorySpinner.setOnItemSelectedListener(this);

        subcategorySpinner = (Spinner) findViewById(R.id.new_event_subcategory_spinner);
        subcategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories.get(selectedCategory).getSubcategories());
        subcategorySpinner.setAdapter(subcategoryAdapter);
        //subcategorySpinner.setOnItemSelectedListener(this);


        Spinner spinner = (Spinner) findViewById(R.id.new_event_category_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = position;
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
        categoryDataSource.close();
        testDataSource.close();
        super.onDestroy();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }*/

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

    public void discardEvent(View view) {
        Intent intent = new Intent(this, SessionActivity.class);
        Bundle b = new Bundle();
        b.putLong("testId", testId);
        b.putLong("sessionId", sessionId);
        intent.putExtras(b);
        NavUtils.navigateUpTo(this,intent);
    }

    public void saveEvent(View view) {
        long timestamp = System.currentTimeMillis() / 1000;
        Log log = new Log(timestamp, sessionId);

        Spinner prioritySpinner = (Spinner) findViewById(R.id.new_event_priority_spinner);
        log.setPriority(prioritySpinner.getSelectedItemPosition() + 1);

        log.setLatitude(latitude);
        log.setLongitude(longitude);

        EditText description = (EditText) findViewById(R.id.new_event_comment);
        log.setDescription(description.getText().toString());

        log.setCategoryId(categories.get(selectedCategory).getId());

        log.setSubcategoryIndex(subcategorySpinner.getSelectedItemPosition());

        Spinner taskSpinner = (Spinner) findViewById(R.id.new_event_task_spinner);
        log.setTaskIndex(taskSpinner.getSelectedItemPosition());


        LogDataSource logDAO = new LogDataSource(getApplicationContext());
        logDAO.open();
        logDAO.commitLog(log);
        logDAO.close();

        Intent intent = new Intent(this, SessionActivity.class);
        Bundle b = new Bundle();
        b.putLong("testId", testId);
        b.putLong("sessionId", sessionId);
        intent.putExtras(b);
        NavUtils.navigateUpTo(this,intent);
    }

    private void reloadCategory(){
        List<String> categoryList = new ArrayList<String>();
        for(Category cat : categories){
            categoryList.add(cat.getName());
        }
        Spinner categorySpinner = (Spinner) findViewById(R.id.new_event_category_spinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void reloadSubCategory(Category category){
        Spinner subSpinner = (Spinner) findViewById(R.id.new_event_subcategory_spinner);
        List<String> subList = category.getSubcategories();
        ArrayAdapter<String> sDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subList);
        sDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setAdapter(sDataAdapter);
    }

    public void addCategory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter new category");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = input.getText().toString();
                Category cat = new Category(category);
                categoryDataSource.commitCategory(cat);
                test.addCategory(cat);
                testDataSource.commitTest(test);
                reloadCategory();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addSubcategory(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter new subcategory");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subcategory = input.getText().toString();
                Category cat = categories.get(selectedCategory);
                cat.addSubcategory(subcategory);
                categoryDataSource.commitCategory(cat);
                reloadSubCategory(cat);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

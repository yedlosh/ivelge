package cz.ctu.pda.ivelge;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class NewEventActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private int selectedTaskIndex = 0;
    private TestDataSource testDataSource;
    private CategoryDataSource categoryDataSource;
    private long testId;
    private long sessionId;
    private List<Category> categories;
    private int selectedCategory = 0;
    private Spinner subcategorySpinner;
    private ArrayAdapter<String> subcategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_event);

        Bundle b = getIntent().getExtras();
        testId = b.getLong("testId");
        sessionId = b.getLong("sessionId");

        testDataSource = new TestDataSource(this);
        testDataSource.open();
        Test test = testDataSource.getTest(testId);

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
        categorySpinner.setOnItemSelectedListener(this);

        subcategorySpinner = (Spinner) findViewById(R.id.new_event_subcategory_spinner);
        subcategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories.get(selectedCategory).getSubcategories());
        subcategorySpinner.setAdapter(subcategoryAdapter);
        subcategorySpinner.setOnItemSelectedListener(this);


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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (view.getId() == R.id.new_event_category_spinner) {
            selectedCategory = pos;
            subcategoryAdapter.clear();
            subcategoryAdapter.addAll(categories.get(selectedCategory).getSubcategories());
            subcategorySpinner.setAdapter(subcategoryAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void discardEvent(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void saveEvent(View view) {
        long timestamp = System.currentTimeMillis() / 1000;
        Log log = new Log(timestamp, sessionId);

        Spinner prioritySpinner = (Spinner) findViewById(R.id.new_event_priority_spinner);
        log.setPriority(prioritySpinner.getSelectedItemPosition());

        Spinner taskSpinner = (Spinner) findViewById(R.id.new_event_task_spinner);
        log.setTaskIndex(taskSpinner.getSelectedItemPosition());

        log.setCategoryId(categories.get(selectedCategory).getId());

        log.setSubcategoryIndex(subcategorySpinner.getSelectedItemPosition());

        EditText description = (EditText) findViewById(R.id.new_event_comment);
        log.setDescription(description.getText().toString());

        NavUtils.navigateUpFromSameTask(this);
    }
}

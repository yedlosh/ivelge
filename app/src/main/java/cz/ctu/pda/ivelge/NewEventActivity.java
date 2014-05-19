package cz.ctu.pda.ivelge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;


public class NewEventActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
        private int selectedTaskIndex;
        private TestDataSource testDataSource;
        private CategoryDataSource categoryDataSource;
        private long testId;
        private List<Category> categories;
        private int selectedCategory=0;
        private Spinner subcategorySpinner;
        private ArrayAdapter<String> subcategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Bundle b = getIntent().getExtras();
        testId=b.getLong("testId");

        testDataSource=new TestDataSource(this);
        testDataSource.open();
        Test test=testDataSource.getTest(testId);

        categoryDataSource=new CategoryDataSource(this);
        categoryDataSource.open();
        categories=categoryDataSource.getAllCategories();

        selectedTaskIndex=b.getInt("selectedTaskInex");

        Spinner taskSpinner=(Spinner)findViewById(R.id.new_event_task_spinner);
        ArrayAdapter<String> taskAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,test.getTasks());
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setSelection(selectedTaskIndex);

        Spinner prioritySpinner=(Spinner)findViewById(R.id.new_event_priority_spinner);
        ArrayAdapter<CharSequence> priorityAdapter=ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        prioritySpinner.setAdapter(priorityAdapter);

        //???
        Spinner categorySpinner=(Spinner)findViewById(R.id.new_event_category_spinner);
        ArrayAdapter<Category> categoryAdapter=new ArrayAdapter<Category>(this,android.R.layout.simple_spinner_item,categories);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        subcategorySpinner=(Spinner)findViewById(R.id.new_event_subcategory_spinner);
        subcategoryAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories.get(selectedCategory).getSubcategories());
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
        if(id==R.id.new_event_category_spinner){
            selectedCategory=pos;
            subcategoryAdapter.clear();
            subcategoryAdapter.addAll(categories.get(selectedCategory).getSubcategories());
            subcategorySpinner.setAdapter(subcategoryAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void discardEvent(View view) {
        Intent intent=new Intent(this,StartSessionActivity.class);
        startActivity(intent);
    }
    public void saveEvent(View view){

        Intent intent=new Intent(this,StartSessionActivity.class);
        startActivity(intent);
    }
}

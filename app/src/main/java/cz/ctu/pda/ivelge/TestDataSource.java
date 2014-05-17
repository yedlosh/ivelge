package cz.ctu.pda.ivelge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class TestDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = {
            DatabaseSQLiteHelper.TEST_ID,
            DatabaseSQLiteHelper.TEST_NAME,
            DatabaseSQLiteHelper.TEST_PARTICIPANTS,
            DatabaseSQLiteHelper.TEST_TASKS};
    private Context context;

    public TestDataSource(Context context) {

        dbHelper = new DatabaseSQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Test> getAllTests() {
        List<Test> tests = new ArrayList<Test>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_TEST,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();

        while (!cursor.isAfterLast()) {
            Test test = cursorToTest(cursor, categoryDAO);
            tests.add(test);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        categoryDAO.close();

        //Collections.sort(tests);

        return tests;
    }

    public Test getTest(long id){

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_TEST,
                allColumns, DatabaseSQLiteHelper.TEST_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();

        Test test = cursorToTest(cursor,categoryDAO);

        // make sure to close the cursor
        cursor.close();
        categoryDAO.close();

        //Collections.sort(tests);

        return test;
    }

    private Test cursorToTest(Cursor cursor, CategoryDataSource categoryDAO) {

        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String participants = cursor.getString(2);
        String tasks = cursor.getString(3);
        String categories = cursor.getString(4);

        List<String> participantList = new ArrayList<String>(Arrays.asList(participants.split(",")));
        List<String> taskList = new ArrayList<String>(Arrays.asList(tasks.split(",")));

        String[] categoryTokens = categories.split(",");
        List<Category> categoryList = new ArrayList<Category>();


        for (String catIdString : categoryTokens) {
            Category category = categoryDAO.getCategory(Long.getLong(catIdString));
            categoryList.add(category);
        }

        Test test =new Test(id, name, participantList,taskList,categoryList);
        return test;
    }

    public boolean insertTest(Test test){

        if(isWritable(test)){
            ContentValues values = new ContentValues();
            values.put(DatabaseSQLiteHelper.TEST_NAME, test.getName());
            values.put(DatabaseSQLiteHelper.TEST_PARTICIPANTS,DatabaseSQLiteHelper.listToCsv(test.getParticipants(),','));
            values.put(DatabaseSQLiteHelper.TEST_TASKS,DatabaseSQLiteHelper.listToCsv(test.getTasks(),','));
            values.put(DatabaseSQLiteHelper.TEST_CATEGORIES,DatabaseSQLiteHelper.listToCsv(test.getCategories(),','));

            long result = database.insertWithOnConflict(DatabaseSQLiteHelper.TABLE_TEST, null, values, database.CONFLICT_REPLACE);
            if (result != -1) {
                return true;
            }
        }
        return false;
    }

    public boolean isWritable(Test test){
        if(test.getName() != null && !test.getName().isEmpty()){
            if(test.getParticipants() != null && !test.getParticipants().isEmpty()){
                if(test.getTasks() != null && !test.getTasks().isEmpty()){
                    if(test.getCategories() != null && test.getCategories().isEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

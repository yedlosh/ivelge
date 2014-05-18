package cz.ctu.pda.ivelge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
            DatabaseSQLiteHelper.TEST_TASKS,
            DatabaseSQLiteHelper.TEST_CATEGORIES,
            DatabaseSQLiteHelper.TEST_TIMESTAMP,
            DatabaseSQLiteHelper.TEST_UPLOADED
    };
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

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();

        SessionDataSource sessionDAO = new SessionDataSource(context);
        sessionDAO.open();

        while (!cursor.isAfterLast()) {
            Test test = cursorToTest(cursor, categoryDAO, sessionDAO);
            tests.add(test);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        categoryDAO.close();
        sessionDAO.close();

        //Collections.sort(tests);

        return tests;
    }

    public Test getTest(long id) {

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_TEST,
                allColumns, DatabaseSQLiteHelper.TEST_ID + " = " + id, null, null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();

        SessionDataSource sessionDAO = new SessionDataSource(context);
        sessionDAO.open();

        Test test = cursorToTest(cursor, categoryDAO, sessionDAO);

        // make sure to close the cursor
        cursor.close();
        categoryDAO.close();
        sessionDAO.close();

        //Collections.sort(tests);

        return test;
    }

    private Test cursorToTest(Cursor cursor, CategoryDataSource categoryDAO, SessionDataSource sessionDAO) {

        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String tasks = cursor.getString(2);
        String categories = cursor.getString(3);
        long timestamp = cursor.getLong(4);
        int uploaded = cursor.getInt(5);

        boolean uploadedBool = false;
        if (uploaded != 0) {
            uploadedBool = true;
        }

        List<String> taskList = new ArrayList<String>(Arrays.asList(tasks.split(",")));

        String[] categoryTokens = categories.split(",");
        List<Category> categoryList = new ArrayList<Category>();

        for (String catIdString : categoryTokens) {
            Category category = categoryDAO.getCategory(Long.getLong(catIdString));
            categoryList.add(category);
        }

        List<Session> sessions = sessionDAO.getTestSessions(id);

        Test test = new Test(id, name, taskList, categoryList, timestamp, uploadedBool, sessions);

        return test;
    }

    public boolean commitTest(Test test) {

        if (isWritable(test)) {

            ContentValues values = new ContentValues();
            if (test.getId() != -1) {
                values.put(DatabaseSQLiteHelper.TEST_ID, test.getId());
            }
            values.put(DatabaseSQLiteHelper.TEST_NAME, test.getName());
            values.put(DatabaseSQLiteHelper.TEST_TASKS, DatabaseSQLiteHelper.listToCsv(test.getTasks(), ','));

            List<Long> categoryIDs = new ArrayList<Long>();
            for (Category cat : test.getCategories()) {
                categoryIDs.add(cat.getId());
            }

            values.put(DatabaseSQLiteHelper.TEST_CATEGORIES, DatabaseSQLiteHelper.listToCsv(categoryIDs, ','));
            values.put(DatabaseSQLiteHelper.TEST_TIMESTAMP, test.getTimestamp());

            int uploaded = 0;
            if (test.isUploaded()) {
                uploaded = 1;
            }
            values.put(DatabaseSQLiteHelper.TEST_UPLOADED, uploaded);
            long result = database.insertWithOnConflict(DatabaseSQLiteHelper.TABLE_TEST, null, values, database.CONFLICT_REPLACE);

            if (result != -1) {
                if (test.getId() != result) {
                    test.setId(result);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isWritable(Test test) {
        if (test.getName() != null && !test.getName().isEmpty()) {
            if (test.getTasks() != null && !test.getTasks().isEmpty()) {
                if (test.getCategories() != null && !test.getCategories().isEmpty()) {
                    if (test.getTimestamp() != 0) {
                        if (test.getSessions() != null && !test.getSessions().isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean insertTest(Test test) {

        CategoryDataSource categoryDAO = new CategoryDataSource(context);
        categoryDAO.open();
        for (Category cat : test.getCategories())
            if (!categoryDAO.commitCategory(cat)) {
                Log.e(TestDataSource.class.getName(), "insertTest: Writing category to DB failed!");
                //throw new RuntimeException("insertTest: Writing category to DB failed!");
            }
        categoryDAO.close();

        if (!commitTest(test)) {
            Log.e(TestDataSource.class.getName(), "insertTest: Writing test to DB failed!");
            //throw new RuntimeException("insertTest: Writing test to DB failed!");
        }

        SessionDataSource sessionDAO = new SessionDataSource(context);
        for (Session ses : test.getSessions()) {
            ses.setTestId(test.getId());
            if (!sessionDAO.commitSession(ses)) {
                Log.e(TestDataSource.class.getName(), "insertTest: Writing session to DB failed!");
                //throw new RuntimeException("insertTest: Writing session to DB failed!");
            }
        }
        sessionDAO.close();

        return true;
    }
}

package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public TestDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}

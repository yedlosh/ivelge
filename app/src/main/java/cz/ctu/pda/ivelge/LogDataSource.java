package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class LogDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = {
            DatabaseSQLiteHelper.LOG_ID,
            DatabaseSQLiteHelper.LOG_TIMESTAMP,
            DatabaseSQLiteHelper.LOG_PRIORITY,
            DatabaseSQLiteHelper.LOG_LOCATION,
            DatabaseSQLiteHelper.LOG_DESCRIPTION,
            DatabaseSQLiteHelper.LOG_IMGPATH,
            DatabaseSQLiteHelper.LOG_SESSIONID};

    public LogDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}

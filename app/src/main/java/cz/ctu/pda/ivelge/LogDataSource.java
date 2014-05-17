package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Context context;

    public LogDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Log> getSessionLogs(Long sessionId){
        List<Log> logs = new ArrayList<Log>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_LOG,
                allColumns, DatabaseSQLiteHelper.LOG_SESSIONID + " = " + sessionId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log log  = cursorToLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return logs;
    }

    private Log cursorToLog(Cursor cursor) {

        long id = cursor.getLong(0);
        long timestamp = cursor.getLong(1);
        int priority = cursor.getInt(2);
        String location = cursor.getString(3);
        String description = cursor.getString(4);
        String imgPath = cursor.getString(5);
        int sessionId = cursor.getInt(6);

        File img = new File(context.getFilesDir() + "/img" + imgPath);

        Log log = new Log(id, timestamp, priority, location, description, img,sessionId);
        return log;
    }
}

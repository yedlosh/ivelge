package cz.ctu.pda.ivelge;

import android.content.ContentValues;
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
            DatabaseSQLiteHelper.LOG_LATITUDE,
            DatabaseSQLiteHelper.LOG_LONGITUDE,
            DatabaseSQLiteHelper.LOG_DESCRIPTION,
            DatabaseSQLiteHelper.LOG_IMGPATH,
            DatabaseSQLiteHelper.LOG_SESSIONID,
            DatabaseSQLiteHelper.LOG_CATEGORYID,
            DatabaseSQLiteHelper.LOG_SUBCATEGORYINDEX,
            DatabaseSQLiteHelper.LOG_TASKINDEX};
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

    public Log getLog(long id){
        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_LOG,
                allColumns, DatabaseSQLiteHelper.LOG_ID + " = " + id, null, null, null, null);

        if(cursor.getCount() == 0){
            return null;
        }

        cursor.moveToFirst();
        Log log  = cursorToLog(cursor);

        cursor.close();
        return log;
    }

    public List<Log> getSessionLogs(Long sessionId){
        List<Log> logs = new ArrayList<Log>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_LOG,
                allColumns, DatabaseSQLiteHelper.LOG_SESSIONID + " = " + sessionId, null, null, null, null);

        if (cursor.getCount() == 0) {
            return logs;
        }

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
        double latitude = cursor.getDouble(3);
        double longitude = cursor.getDouble(4);
        String description = cursor.getString(5);
        String imgPath = cursor.getString(6);
        int sessionId = cursor.getInt(7);
        long categoryId = cursor.getLong(8);
        int subcategoryIndex = cursor.getInt(9);
        int taskIndex = cursor.getInt(10);

        //File img = new File(context.getFilesDir() + "/img" + imgPath);
        File img = null;

        if(imgPath != null && imgPath.isEmpty())
        img = new File(imgPath);

        Log log = new Log(id, timestamp, priority, latitude, longitude, description, img, sessionId, categoryId, subcategoryIndex, taskIndex);
        return log;
    }

    public boolean commitLog(Log log) {

        if(log.getSessionId() == -1){
            return false;
        }

        ContentValues values = new ContentValues();
        if (log.getId() != -1) {
            values.put(DatabaseSQLiteHelper.LOG_ID, log.getId());
        }
        values.put(DatabaseSQLiteHelper.LOG_TIMESTAMP, log.getTimestamp());
        values.put(DatabaseSQLiteHelper.LOG_PRIORITY, log.getPriority());
        values.put(DatabaseSQLiteHelper.LOG_LATITUDE, log.getLatitude());
        values.put(DatabaseSQLiteHelper.LOG_LONGITUDE, log.getLongitude());
        if(log.getDescription() != null){
        values.put(DatabaseSQLiteHelper.LOG_DESCRIPTION, log.getDescription());
        } else {
            values.putNull(DatabaseSQLiteHelper.LOG_DESCRIPTION);
        }
        if(log.getPhoto() != null){
        values.put(DatabaseSQLiteHelper.LOG_IMGPATH, log.getPhoto().getAbsolutePath());
        } else {
            values.putNull(DatabaseSQLiteHelper.LOG_IMGPATH);
        }
        values.put(DatabaseSQLiteHelper.LOG_SESSIONID, log.getSessionId());
        values.put(DatabaseSQLiteHelper.LOG_CATEGORYID, log.getCategoryId());
        values.put(DatabaseSQLiteHelper.LOG_SUBCATEGORYINDEX, log.getSubcategoryIndex());
        values.put(DatabaseSQLiteHelper.LOG_TASKINDEX, log.getTaskIndex());

        long id = database.insertWithOnConflict(DatabaseSQLiteHelper.TABLE_LOG, null, values, database.CONFLICT_REPLACE);

        if (id != -1) {
            if (log.getId() != id) {
                log.setId(id);
            }
            return true;
        }
        return false;
    }

}

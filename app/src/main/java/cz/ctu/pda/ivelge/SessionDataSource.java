package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class SessionDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = {
            DatabaseSQLiteHelper.SESSION_ID,
            DatabaseSQLiteHelper.SESSION_STARTTIME,
            DatabaseSQLiteHelper.SESSION_ENDTIME,
            DatabaseSQLiteHelper.SESSION_PRETEST,
            DatabaseSQLiteHelper.SESSION_POSTTEST};
    private Context context;

    public SessionDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_SESSION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        LogDataSource logDAO = new LogDataSource(context);
        logDAO.open();

        while (!cursor.isAfterLast()) {
            Session session = cursorToSession(cursor, logDAO);
            sessions.add(session);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        logDAO.close();

        //Collections.sort(tests);

        return sessions;
    }

    public List<Session> getTestSessions(Long testId){
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_SESSION,
                allColumns, DatabaseSQLiteHelper.SESSION_TESTID + " = " + testId, null, null, null, null);

        cursor.moveToFirst();

        LogDataSource logDAO = new LogDataSource(context);
        logDAO.open();

        while (!cursor.isAfterLast()) {
            Session session = cursorToSession(cursor, logDAO);
            sessions.add(session);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        logDAO.close();

        return sessions;
    }

    private Session cursorToSession(Cursor cursor, LogDataSource logDAO) {

        long id = cursor.getLong(0);
        long startTime = cursor.getLong(1);
        long endTime = cursor.getLong(2);
        String preTest = cursor.getString(3);
        String postTest = cursor.getString(4);

        List<Log> logs = logDAO.getSessionLogs(id);

        Session session = new Session(id, startTime, endTime,preTest,postTest,logs);
        return session;
    }
}

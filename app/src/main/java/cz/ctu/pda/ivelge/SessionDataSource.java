package cz.ctu.pda.ivelge;

import android.content.ContentValues;
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
            DatabaseSQLiteHelper.SESSION_POSTTEST,
            DatabaseSQLiteHelper.SESSION_TESTID,
            DatabaseSQLiteHelper.SESSION_PARTICIPANTNAME};
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

    public Session getSession(long id) {

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_SESSION,
                allColumns, DatabaseSQLiteHelper.SESSION_ID + " = " + id, null, null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        LogDataSource logDAO = new LogDataSource(context);
        logDAO.open();

        Session session = cursorToSession(cursor, logDAO);

        // make sure to close the cursor
        cursor.close();
        logDAO.close();

        //Collections.sort(tests);

        return session;
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_SESSION,
                allColumns, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            return sessions;
        }

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

        if (cursor.getCount() == 0) {
            return sessions;
        }

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
        long testId = cursor.getLong(5);
        String participantName = cursor.getString(6);

        List<Log> logs = logDAO.getSessionLogs(id);

        Session session = new Session(id, startTime, endTime, preTest, postTest, logs, testId, participantName);
        return session;
    }

    public boolean commitSession(Session session){
        if (session.getTestId() == -1) {
            return false;
        }
        ContentValues values = new ContentValues();

        if (session.getId() != -1) {
            values.put(DatabaseSQLiteHelper.SESSION_ID, session.getId());
        }
        values.put(DatabaseSQLiteHelper.SESSION_STARTTIME, session.getStartTime());
        values.put(DatabaseSQLiteHelper.SESSION_ENDTIME, session.getEndTime());
        values.put(DatabaseSQLiteHelper.SESSION_PRETEST, session.getPreTest());
        values.put(DatabaseSQLiteHelper.SESSION_POSTTEST, session.getPostTest());
        values.put(DatabaseSQLiteHelper.SESSION_TESTID, session.getTestId());
        values.put(DatabaseSQLiteHelper.SESSION_PARTICIPANTNAME, session.getParticipantName());

        long id = database.insertWithOnConflict(DatabaseSQLiteHelper.TABLE_SESSION, null, values, database.CONFLICT_REPLACE);

        if (id != -1) {
            if (session.getId() != id) {
                session.setId(id);
            }
            return true;
        }
        return false;
    }

}

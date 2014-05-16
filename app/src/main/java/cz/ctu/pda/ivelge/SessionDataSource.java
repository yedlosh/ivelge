package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public SessionDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

}

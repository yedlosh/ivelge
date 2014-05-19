package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by yedlosh on 07/12/13.
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TEST = "Test";
    public static final String TEST_ID = "_id";
    public static final String TEST_NAME = "name";
    public static final String TEST_TASKS = "tasks";
    public static final String TEST_CATEGORIES = "categories";
    public static final String TEST_TIMESTAMP = "timestamp";
    public static final String TEST_UPLOADED = "uploaded";

    public static final String TABLE_SESSION = "Session";
    public static final String SESSION_ID = "_id";
    public static final String SESSION_STARTTIME = "startTime";
    public static final String SESSION_ENDTIME = "endTime";
    public static final String SESSION_PRETEST = "preTest";
    public static final String SESSION_POSTTEST = "postTest";
    public static final String SESSION_TESTID = "testId";
    public static final String SESSION_PARTICIPANTNAME = "participantName";

    public static final String TABLE_LOG = "Log";
    public static final String LOG_ID = "_id";
    public static final String LOG_TIMESTAMP = "timestamp";
    public static final String LOG_PRIORITY = "priority";
    public static final String LOG_LATITUDE = "latitude";
    public static final String LOG_LONGITUDE = "longitude";
    public static final String LOG_DESCRIPTION = "description";
    public static final String LOG_IMGPATH = "imgpath";
    public static final String LOG_SESSIONID = "sessionId";
    public static final String LOG_CATEGORYID = "categoryId";
    public static final String LOG_SUBCATEGORYINDEX = "subcategoryIndex";
    public static final String LOG_TASKINDEX = "taskIndex";

    public static final String TABLE_CATEGORY = "Category";
    public static final String CATEGORY_ID = "_id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_SUBCATEGORIES = "subcategories";


    private static final String DATABASE_NAME = "ivelge.db";
    private static final int DATABASE_VERSION = 3;

    // Table creation sql statements
    private static final String TEST_CREATE = "create table "
            + TABLE_TEST + "("
            + TEST_ID + " integer primary key not null, "
            + TEST_NAME + " text not null, "
            + TEST_TASKS + " integer not null, "
            + TEST_CATEGORIES + " text not null, "
            + TEST_TIMESTAMP + " integer not null, "
            + TEST_UPLOADED + " integer" + ");";

    private static final String SESSION_CREATE = "create table "
            + TABLE_SESSION + "("
            + SESSION_ID + " integer primary key not null, "
            + SESSION_STARTTIME + " integer, "
            + SESSION_ENDTIME + " integer, "
            + SESSION_PRETEST + " text, "
            + SESSION_POSTTEST + " text, "
            + SESSION_TESTID + " integer, "
            + SESSION_PARTICIPANTNAME + " text not null,"
            + " FOREIGN KEY("+ SESSION_TESTID + ") REFERENCES "+ TABLE_TEST +"("+TEST_ID+")" + ");";

    private static final String LOG_CREATE = "create table "
            + TABLE_LOG + "("
            + LOG_ID + " integer primary key not null, "
            + LOG_TIMESTAMP + " integer not null, "
            + LOG_PRIORITY + " integer, "
            + LOG_LATITUDE + " real, "
            + LOG_LONGITUDE + " real, "
            + LOG_DESCRIPTION + " text, "
            + LOG_IMGPATH + " text, "
            + LOG_SESSIONID + " integer not null, "
            + LOG_CATEGORYID + " integer not null, "
            + LOG_SUBCATEGORYINDEX + " integer, "
            + LOG_TASKINDEX + " integer not null, "
            + " FOREIGN KEY("+ LOG_SESSIONID + ") REFERENCES "+ TABLE_SESSION +"("+SESSION_ID+")" + ");";

    private static final String CATEGORY_CREATE = "create table "
            + TABLE_CATEGORY + "("
            + CATEGORY_ID + " integer primary key not null, "
            + CATEGORY_NAME + " text not null, "
            + CATEGORY_SUBCATEGORIES + " text" + ");";

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w(DatabaseSQLiteHelper.class.getName(),"BUILDING DATABASE");
        Log.i(DatabaseSQLiteHelper.class.getName(),"Executing: \r\n" + TEST_CREATE);
        database.execSQL(TEST_CREATE);
        Log.i(DatabaseSQLiteHelper.class.getName(),"Executing: \r\n" + SESSION_CREATE);
        database.execSQL(SESSION_CREATE);
        Log.i(DatabaseSQLiteHelper.class.getName(),"Executing: \r\n" + LOG_CREATE);
        database.execSQL(LOG_CREATE);
        Log.i(DatabaseSQLiteHelper.class.getName(),"Executing: \r\n" + CATEGORY_CREATE);
        database.execSQL(CATEGORY_CREATE);

        //database.execSQL(insertArticleRow("Nadpis 1", "Obsah clanku 1 z kategorie vyhradne novinek", 1527776581, 0, 1386439128));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

/*    private String insertArticleRow(String title, String content, int author, int category, long timestamp, String imgpath) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(TABLE_ARTICLES).append("(");
        sb.append(
                COLUMN_TITLE + ", "
                        + COLUMN_CONTENT + ", "
                        + COLUMN_AUTHOR + ", "
                        + COLUMN_CATEGORY + ", "
                        + COLUMN_TIMESTAMP + ", "
                        + COLUMN_IMGPATH);
        sb.append(") VALUES (");
        sb.append("'").append(title).append("',");
        sb.append("'").append(content).append("',");
        sb.append(author).append(",");
        sb.append(category).append(",");
        sb.append(timestamp).append(",");
        sb.append(imgpath);
        sb.append(");");

        return sb.toString();
    }*/

    public static String listToCsv(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        // all but last
        for(int i = 0; i < list.size() - 1 ; i++) {
            sb.append(list.get(i).toString());
            sb.append(separator);
        }

        // last string, no separator
        if(list.size() > 0){
            sb.append(list.get(list.size()-1));
        }

        return sb.toString();
    }
}

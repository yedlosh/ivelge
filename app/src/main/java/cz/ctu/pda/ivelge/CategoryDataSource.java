package cz.ctu.pda.ivelge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class CategoryDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = {
            DatabaseSQLiteHelper.CATEGORY_ID,
            DatabaseSQLiteHelper.CATEGORY_NAME,
            DatabaseSQLiteHelper.CATEGORY_PARENTCATEGORY};

    public CategoryDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

}

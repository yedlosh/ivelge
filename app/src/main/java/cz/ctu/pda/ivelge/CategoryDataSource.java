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
public class CategoryDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = {
            DatabaseSQLiteHelper.CATEGORY_ID,
            DatabaseSQLiteHelper.CATEGORY_NAME,
            DatabaseSQLiteHelper.CATEGORY_SUBCATEGORIES};

    public CategoryDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
    }

    public CategoryDataSource(SQLiteDatabase database){

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_CATEGORY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category category = cursorToCategory(cursor);
            categories.add(category);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return categories;
    }

    public Category getCategory(Long id){

        Cursor cursor = database.query(DatabaseSQLiteHelper.TABLE_CATEGORY,
                allColumns, DatabaseSQLiteHelper.CATEGORY_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        Category category = cursorToCategory(cursor);

        // make sure to close the cursor
        cursor.close();
        return category;
    }

    private Category cursorToCategory(Cursor cursor) {

        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String subcategories = cursor.getString(2);

        List<String> subcategoriesList = new ArrayList<String>(Arrays.asList(subcategories.split(",")));

        Category category = new Category(id, name, subcategoriesList);
        return category;
    }
}

package edu.uw.tacoma.css.diseasemap.database_connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.css.diseasemap.R;

public class UserPrefsDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "UserPrefs.db";
    private UserPrefsDBHelper mUserPrefsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private static final String USER_PREFS_TABLE = "UserPrefs";

    public UserPrefsDB(Context context) {
        mUserPrefsDBHelper = new UserPrefsDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mUserPrefsDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the user preferences into the local sqlite table. Returns true if successful, false otherwise.
     *
     * @param email the email
     * @param coolColor the cool color (Integer representation of hex)
     * @param warmColor the warm color (Integer representation of hex)
     *
     * @return true or false success
     */
    public boolean insertPrefs(String email, Integer coolColor, Integer warmColor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("cool_color", coolColor);
        contentValues.put("warm_color", warmColor);
        long rowId = mSQLiteDatabase.insert(USER_PREFS_TABLE, null, contentValues);
        return rowId != -1;
    }

    /**
     * Updates the user preferences in the local sqlite table. Returns true if successful, false otherwise.
     *
     * @param email the email
     * @param coolColor the cool color (Integer representation of hex)
     * @param warmColor the warm color (Integer representation of hex)
     *
     * @return true or false success
     */
    public boolean updatePrefs(String email, Integer coolColor, Integer warmColor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cool_color", coolColor);
        contentValues.put("warm_color", warmColor);
        String where = "email = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = email;
        long rowId = mSQLiteDatabase.update(USER_PREFS_TABLE, contentValues, where, whereArgs);
        return rowId != -1;
    }

    /**
     * Delete all the data from the COURSE_TABLE
     */
    public void deletePrefs() {
        mSQLiteDatabase.delete(USER_PREFS_TABLE, null, null);
    }

    /**
     * Returns the colors for the given email.
     *
     * @return list
     */
    public List<Integer> getColors(String email) {
        String[] columns = {"cool_color", "warm_color"};
        String where = "email = ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = email;
        Cursor c = mSQLiteDatabase.query(USER_PREFS_TABLE, // The table to query
                columns,// The columns to return
                where, // The columns for the WHERE clause
                whereArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        c.moveToFirst();
        List<Integer> list = new ArrayList<Integer>(2);
        for (int i = 0; i < c.getCount(); i++) {
            Integer cool = c.getInt(0);
            list.add(cool);
            Integer warm = c.getInt(1);
            list.add(warm);

            c.moveToNext();
        }
        return list;
    }

    class UserPrefsDBHelper extends SQLiteOpenHelper {
        private final String CREATE_PREFS_SQL;
        private final String DROP_PREFS_SQL;

        public UserPrefsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_PREFS_SQL = context.getString(R.string.CREATE_PREFS_SQL);
            DROP_PREFS_SQL = context.getString(R.string.DROP_PREFS_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_PREFS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_PREFS_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}

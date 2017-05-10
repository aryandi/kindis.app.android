package co.digdaya.kindis.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL on 5/11/2017.
 */

public class KindisDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kindis.db";
    private static final int DATABASE_VERSION = 1;
    public KindisDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + KindisDBname.TABLE_NAME+ " (" +
                KindisDBname.COLUMN_ID + " INTEGER PRIMARY KEY," +
                KindisDBname.COLUMN_TITLE+ " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_PATH + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_ALBUM + " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ARTIST + " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ARTIST_ID + " INTEGER NOT NULL" +
                "); ";

        // COMPLETED (7) Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_WAITLIST_TABLE
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KindisDBname.TABLE_NAME);
        onCreate(db);
    }

    public void addToDatabase(String uid, String title, String path, String album, String artist, String artist_id){
        SQLiteDatabase mDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KindisDBname.COLUMN_ID, uid);
        values.put(KindisDBname.COLUMN_TITLE, title);
        values.put(KindisDBname.COLUMN_PATH, path);
        values.put(KindisDBname.COLUMN_ALBUM, album);
        values.put(KindisDBname.COLUMN_ARTIST, artist);
        values.put(KindisDBname.COLUMN_ARTIST_ID, artist_id);
        mDb.insert(KindisDBname.TABLE_NAME, null, values);
        mDb.close();
    }
}

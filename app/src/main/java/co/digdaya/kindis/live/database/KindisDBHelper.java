package co.digdaya.kindis.live.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.digdaya.kindis.live.helper.ApiHelper;

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
        final String SQL_CREATE_SINGLE_TABLE = "CREATE TABLE " + KindisDBname.TABLE_SINGLE + " (" +
                KindisDBname.COLUMN_ID + " INTEGER PRIMARY KEY," +
                KindisDBname.COLUMN_TITLE+ " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_PATH + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_IMAGE + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_ALBUM + " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ARTIST + " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ARTIST_ID + " INTEGER NOT NULL, " +
                KindisDBname.COLUMN_FK + " VARCHAR(25) NOT NULL, " +
                " UNIQUE (" + KindisDBname.COLUMN_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_SINGLE_TABLE);

        final String SQL_CREATE_ALBUM_TABLE = "CREATE TABLE " + KindisDBname.TABLE_ALBUM + " (" +
                KindisDBname.COLUMN_ALBUM_ID + " INTEGER PRIMARY KEY," +
                KindisDBname.COLUMN_ALBUM+ " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ARTIST + " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_ALBUM_DESC + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_IMAGE + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_BANNER_IMAGE + " TEXT NOT NULL, " +
                " UNIQUE (" + KindisDBname.COLUMN_ALBUM_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_ALBUM_TABLE);

        final String SQL_CREATE_PLAYLIST_TABLE = "CREATE TABLE " + KindisDBname.TABLE_PLAYLIST + " (" +
                KindisDBname.COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY," +
                KindisDBname.COLUMN_PLAYLIST+ " VARCHAR(255) NOT NULL, " +
                KindisDBname.COLUMN_IMAGE + " TEXT NOT NULL, " +
                KindisDBname.COLUMN_BANNER_IMAGE + " TEXT NOT NULL, " +
                " UNIQUE (" + KindisDBname.COLUMN_PLAYLIST_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_PLAYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KindisDBname.TABLE_SINGLE);
        db.execSQL("DROP TABLE IF EXISTS " + KindisDBname.TABLE_ALBUM);
        db.execSQL("DROP TABLE IF EXISTS " + KindisDBname.TABLE_PLAYLIST);
        onCreate(db);
    }

    public void addToTableSingle(String uid, String title, String path, String image, String album, String artist, String artist_id, String fk){
        SQLiteDatabase mDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KindisDBname.COLUMN_ID, uid);
        values.put(KindisDBname.COLUMN_TITLE, title);
        values.put(KindisDBname.COLUMN_PATH, path);
        values.put(KindisDBname.COLUMN_IMAGE, ApiHelper.BASE_URL_IMAGE+image);
        values.put(KindisDBname.COLUMN_ALBUM, album);
        values.put(KindisDBname.COLUMN_ARTIST, artist);
        values.put(KindisDBname.COLUMN_ARTIST_ID, artist_id);
        values.put(KindisDBname.COLUMN_FK, fk);
        mDb.insert(KindisDBname.TABLE_SINGLE, null, values);
        mDb.close();
    }

    public void addToTableAlbum(String album_id, String album, String artist, String desc, String image, String banner){
        SQLiteDatabase mDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KindisDBname.COLUMN_ALBUM_ID, album_id);
        values.put(KindisDBname.COLUMN_ALBUM, album);
        values.put(KindisDBname.COLUMN_ARTIST, artist);
        values.put(KindisDBname.COLUMN_ALBUM_DESC, desc);
        values.put(KindisDBname.COLUMN_IMAGE, ApiHelper.BASE_URL_IMAGE+image);
        values.put(KindisDBname.COLUMN_BANNER_IMAGE, ApiHelper.BASE_URL_IMAGE+banner);
        mDb.insert(KindisDBname.TABLE_ALBUM, null, values);
        mDb.close();
    }

    public void addToTablePlaylist(String playlist_id, String playlist, String image, String banner){
        SQLiteDatabase mDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KindisDBname.COLUMN_PLAYLIST_ID, playlist_id);
        values.put(KindisDBname.COLUMN_PLAYLIST, playlist);
        values.put(KindisDBname.COLUMN_IMAGE, ApiHelper.BASE_URL_IMAGE+image);
        values.put(KindisDBname.COLUMN_BANNER_IMAGE, ApiHelper.BASE_URL_IMAGE+banner);
        mDb.insert(KindisDBname.TABLE_PLAYLIST, null, values);
        mDb.close();
    }
}

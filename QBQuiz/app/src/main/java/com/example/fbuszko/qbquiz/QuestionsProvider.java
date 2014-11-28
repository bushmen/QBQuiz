package com.example.fbuszko.qbquiz;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.fbuszko.qbquiz.database.MySQLiteHelper;

public class QuestionsProvider extends ContentProvider {
    static final String AUTHORITY = "content://com.example.fbuszko.qbquiz.provider";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MySQLiteHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUESTIONS,
                projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        if(uri.getLastPathSegment() == null) {
            return "vnd.android.cursor.dir/com.example.fbuszko.qbquiz.provider.questions";
        } else {
            return "vnd.android.cursor.item/com.example.fbuszko.qbquiz.provider.questions";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database = dbHelper.getWritableDatabase();
        long id = database.insert(MySQLiteHelper.TABLE_QUESTIONS, null, values);
        if(id != -1)
            uri = Uri.withAppendedPath(uri, Long.toString(id));

            return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

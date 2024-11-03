package com.example.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "db.db";
    private static String DB_LOCATION;
    private static final int DB_VERSION = 1;

    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        DB_LOCATION = context.getApplicationInfo().dataDir + "/databases/";

        CopyDB();
    }

    private boolean CheckDB() {
        File file = new File(DB_LOCATION + DB_NAME);

        return file.exists();
    }

    private void CopyDB() {
        if (!CheckDB()) {
            this.getReadableDatabase();
            try {
                CopyDBFile();
            } catch (IOException e) {}
        }
    }

    private void CopyDBFile() throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);

        OutputStream outputStream = new FileOutputStream(DB_LOCATION + DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

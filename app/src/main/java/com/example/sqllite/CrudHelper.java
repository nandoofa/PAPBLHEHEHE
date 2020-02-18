package com.example.sqllite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudHelper {
    DatabaseHelper dbHelper;

    public CrudHelper(DatabaseHelper _dbHelper) {
        this.dbHelper = _dbHelper;
    }

    //insert
    public long insertDosen(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //nullColumnHack menentukan kolom mana yang ingin
        // diisi dengan nilai null, jika tdk ingin memberi
        // nilai null maka isi parameter tsb dgn nilai null
        long _id = db.insert(DatabaseContract.TbDosen.TABLE_NAME, null, values);
        return _id;
    }

    public long insertMahasiswa(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id = db.insert(DatabaseContract.TbMahasiswa.TABLE_NAME, null, values);
        return _id;
    }

    public long insertJurusan(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id = db.insert(DatabaseContract.TbJurusan.TABLE_NAME,null, values);
        return _id;
    }

    //insert pakai transaksi
    public long insertMahasiswaTransaction(ContentValues values[]) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long _id = 0;
        try {
            for (int i = 0; i < values.length; i++) {
                db.insert(DatabaseContract.TbMahasiswa.TABLE_NAME, null, values[i]);
            }
            db.setTransactionSuccessful();
            _id = 1;
        } finally {
            _id = 0;
            db.endTransaction();
        }
        return _id;
    }

    //read
    public ArrayList<HashMap<String, String>> getMahasiswa(@Nullable String _id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.TbMahasiswa.TABLE_NAME + "." + BaseColumns._ID, DatabaseContract.TbMahasiswa.TABLE_NAME + "." +
                DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA + " as NamaMhs",
                DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_EMAIL,
                DatabaseContract.TbDosen.TABLE_NAME + "." + DatabaseContract.TbDosen.COLUMN_NAME_NAMA,
                DatabaseContract.TbJurusan.TABLE_NAME + "." + DatabaseContract.TbJurusan.COLUMN_NAME_NAMA
        };

        String selection =
                DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_ID_DOSENPA
                        + " = " +
                        DatabaseContract.TbDosen.TABLE_NAME + "." + DatabaseContract.TbDosen._ID
                        + "AND" +
                        DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA
                        + " = " +
                        DatabaseContract.TbJurusan.TABLE_NAME + "." + DatabaseContract.TbJurusan._ID;


        String[] selectionArgs = null;
        if (_id != null) {
            selectionArgs = new String[]{_id};
            selection =
                    DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_ID_DOSENPA + " = " + DatabaseContract.TbDosen.TABLE_NAME + "." + DatabaseContract.TbDosen._ID
                            + " AND " +
                            DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_ID_JURUSAN + "+ " + DatabaseContract.TbJurusan.TABLE_NAME + "." + DatabaseContract.TbJurusan._ID
                            + DatabaseContract.TbMahasiswa.TABLE_NAME + "." + BaseColumns._ID + " = ?";
        }

        //menggunakan contoh join
        Cursor cursor = db.query(
                DatabaseContract.TbMahasiswa.TABLE_NAME
                        + " , "
                        + DatabaseContract.TbDosen.TABLE_NAME,
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        ArrayList<HashMap<String, String>> Mahasiswas = new ArrayList<HashMap<String, String>>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa._ID));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow("NamaMhs"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_EMAIL));
            String dosenPa = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen.TABLE_NAME + "." + DatabaseContract.TbDosen.COLUMN_NAME_NAMA));
            String jurusan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbJurusan.TABLE_NAME + "." + DatabaseContract.TbJurusan.COLUMN_NAME_NAMA));
            HashMap<String, String> mahasiswa = new HashMap<String, String>();
            mahasiswa.put("id", id);
            mahasiswa.put("nama", nama);
            mahasiswa.put("email", email);
            mahasiswa.put("dosenPa", dosenPa);
            mahasiswa.put("jurusan",jurusan);
            Mahasiswas.add(mahasiswa);
        }
        cursor.close();
        return Mahasiswas;
    }

    public ArrayList<HashMap<String, String>> getDosen(@Nullable String _id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.TbDosen.COLUMN_NAME_NAMA,
                DatabaseContract.TbDosen.COLUMN_NAME_EMAIL
        };
        String selection = null;
        String[] selectionArgs = null;
        if (_id != null) {
            selectionArgs = new String[]{_id};
            selection = DatabaseContract.TbDosen._ID + " = ?";
        }
        String sortOrder = DatabaseContract.TbDosen.COLUMN_NAME_NAMA + " DESC";

        Cursor cursor = db.query(DatabaseContract.TbDosen.TABLE_NAME + "", // The table //to query
                projection, // The array of columns to
                //return (pass null to get all)
                selection, // The columns for the WHERE
                //clause
                selectionArgs, // The values for the WHERE
                //clause
                null, // don't group the rows
                null,  // don't filter by row groups
                sortOrder // The sort order
        );

        ArrayList<HashMap<String, String>> Dosens = new ArrayList<HashMap<String, String>>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen._ID));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen.COLUMN_NAME_NAMA));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen.COLUMN_NAME_EMAIL));

            HashMap<String, String> dosen = new HashMap<String, String>();
            dosen.put("id", id);
            dosen.put("nama", nama);
            dosen.put("email", email);
            Dosens.add(dosen);
        }
        cursor.close();
        return Dosens;
    }

    public ArrayList<HashMap<String, String>>
    getJurusan(@Nullable String _id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                DatabaseContract.TbJurusan.COLUMN_NAME_NAMA
        };
        String selection = null;
        String[] selectionArgs = null;
        if (_id != null) {
            selectionArgs = new String[]{_id};
            selection = DatabaseContract.TbJurusan._ID + " = ?";
        }
        String sortOrder = null;
        Cursor cursor = db.query(
                DatabaseContract.TbJurusan.TABLE_NAME + "", // The table
                //to query
                projection, // The array of columns to
                //return (pass null to get all)
                selection, // The columns for the WHERE
                //clause
                selectionArgs, // The values for the WHERE
                //clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        ArrayList<HashMap<String, String>> Jurusan = new
                ArrayList<HashMap<String, String>>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.TbJurusan._ID));
            String nama = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.TbJurusan.COLUMN_NAME_NAMA));
            HashMap<String, String> jurusan = new
                    HashMap<String, String>();
            jurusan.put("id", id);
            jurusan.put("namajur", nama);
            Jurusan.add(jurusan);
        }
        cursor.close();
        return Jurusan;
    }

    //update
    public int updateMahasiswa(String id, ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Define 'where' part of query.
        String selection = DatabaseContract.TbMahasiswa._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int updateRows = db.update(DatabaseContract.TbMahasiswa.TABLE_NAME, cv, selection, selectionArgs);
        return updateRows;
    }

    public int updateDosen(String id, ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Define 'where' part of query.
        String selection = DatabaseContract.TbDosen._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int updateRows = db.update(DatabaseContract.TbDosen.TABLE_NAME, cv, selection, selectionArgs);
        return updateRows;
    }

    public int updateJurusan(String id, ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = DatabaseContract.TbJurusan._ID + "= ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int updateRows =
                db.update(DatabaseContract.TbJurusan.TABLE_NAME, cv
                        , selection, selectionArgs);
        return updateRows;
    }

    //Delete
    public int deleteMahasiswa(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Define 'where' part of query.
        String selection = DatabaseContract.TbMahasiswa._ID + " = ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int deletedRows = db.delete(DatabaseContract.TbMahasiswa.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public int deleteDosen(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Define 'where' part of query.
        String selection = DatabaseContract.TbDosen._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int deletedRows = db.delete(DatabaseContract.TbDosen.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public void clearTable(String TABLE_NAME) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDBQuery);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");//reset auto increment
    }
}



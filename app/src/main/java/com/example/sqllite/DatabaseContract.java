package com.example.sqllite;

import android.provider.BaseColumns;

public class DatabaseContract {
    // To prevent someone from accidentally
    // instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {
    }

    /* Inner class that defines the table contents */
    public static class TbMahasiswa implements BaseColumns {
        public static final String TABLE_NAME = "mahasiswa";
        public static final String COLUMN_NAME_NAMA = "nama";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_ID_DOSENPA = "id_dosenpa";
        public static final String COLUMN_NAME_ID_JURUSAN = "id_jurusan";
    }

    public class TbDosen implements BaseColumns {
        public static final String TABLE_NAME = "dosen";
        public static final String COLUMN_NAME_NAMA = "nama";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public static class TbJurusan implements BaseColumns{
        public static final String TABLE_NAME = "jurusan";
        public static final String COLUMN_NAME_NAMA = "namajur";
    }
}
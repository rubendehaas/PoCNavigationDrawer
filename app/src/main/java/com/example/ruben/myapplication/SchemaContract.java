package com.example.ruben.myapplication;

import android.provider.BaseColumns;

public class SchemaContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SchemaContract() {}

    /* Inner class that defines the table contents */
    public static class KanjiEntry implements BaseColumns {
        public static final String TABLE_NAME = "kanji";
        public static final String COLUMN_NAME_CHARACTER = "character";
        public static final String COLUMN_NAME_ROMAJI = "romaji";
        public static final String COLUMN_NAME_HIRAGANA = "hiragana";
        public static final String COLUMN_NAME_KATAKANA = "katakana";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + KanjiEntry.TABLE_NAME + " (" +
                    KanjiEntry._ID + " INTEGER PRIMARY KEY," +
                    KanjiEntry.COLUMN_NAME_CHARACTER + " TEXT," +
                    KanjiEntry.COLUMN_NAME_ROMAJI + " TEXT," +
                    KanjiEntry.COLUMN_NAME_HIRAGANA + " TEXT," +
                    KanjiEntry.COLUMN_NAME_KATAKANA + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + KanjiEntry.TABLE_NAME;
}
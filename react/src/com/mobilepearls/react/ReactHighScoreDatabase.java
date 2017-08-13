package com.mobilepearls.react;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * http://developer.android.com/resources/samples/NotePad/src/com/example/android/notepad/NotePadProvider.html
 */
public class ReactHighScoreDatabase {

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY," + "name TEXT NOT NULL,"
					+ "score INTEGER NOT NULL" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(getClass().getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	public static class HighScoreEntry {
		public final String name;
		public final int score;

		public HighScoreEntry(String name, int score) {
			this.name = name;
			this.score = score;
		}
	}

	private static final String DATABASE_NAME = "reaction_highscore.db";
	private static final int DATABASE_VERSION = 1;
	public static final int MAX_ENTRIES = 10;
	private static final String TABLE_NAME = "reaction_highscore";

	public static ReactHighScoreDatabase getDatabase(Context context) {
		return new ReactHighScoreDatabase(context);
	}

	private final DatabaseHelper databaseHelper;

	private ReactHighScoreDatabase(Context context) {
		databaseHelper = new DatabaseHelper(context);
	}

	public void addEntry(String name, int score) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("score", score);

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		try {
			db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
		}
	}

	public int getPositionForScore(int score) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		try {
			Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE score <= " + score, null);
			try {
				c.moveToFirst();
				return c.getInt(0) + 1;
			} finally {
				c.close();
			}
		} finally {
			db.close();
		}
	}

	public List<HighScoreEntry> getSortedHighScores() {
		List<HighScoreEntry> result = new ArrayList<HighScoreEntry>();

		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(TABLE_NAME);

			String[] projection = null; // list of columns, null:=all
			String selection = null; // where clause excluding where, null:=all rows
			String[] selectionArgs = null; // replaces ?s in selection
			String groupBy = null; // SQL GROUP BY
			String having = null; // SQL HAVING
			String sortOrder = "score, _id"; // SQL ORDER BY

			int count = 0;
			Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
			try {
				if (!cursor.moveToFirst())
					return result;
				int idIndex = cursor.getColumnIndex("_id");
				int nameIndex = cursor.getColumnIndex("name");
				int scoreIndex = cursor.getColumnIndex("score");
				do {
					String name = cursor.getString(nameIndex);
					int score = cursor.getInt(scoreIndex);
					count++;
					if (count > MAX_ENTRIES) {
						int id = cursor.getInt(idIndex);
						db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = " + id);
					} else {
						HighScoreEntry entry = new HighScoreEntry(name, score);
						result.add(entry);
					}
				} while (cursor.moveToNext());

				return result;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}
}

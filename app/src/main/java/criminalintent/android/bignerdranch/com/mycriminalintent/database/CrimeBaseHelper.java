package criminalintent.android.bignerdranch.com.mycriminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import criminalintent.android.bignerdranch.com.mycriminalintent.database.CrimeDBSchema.CrimeTable;

/**
 * Created by Bender on 13/09/2015.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

	private final static int VERSION = 1;
	private final static String DB_NAME = "crimeBase.db";

	public CrimeBaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + CrimeTable.NAME + "( _id integer primary key autoincrement, "
						+ CrimeTable.Cols.UUID
						+ ", " + CrimeTable.Cols.TITLE
						+ ", " + CrimeTable.Cols.DATE
						+ ", " + CrimeTable.Cols.SOLVED
						+ ", " + CrimeTable.Cols.SUSPECT +")"
		);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}

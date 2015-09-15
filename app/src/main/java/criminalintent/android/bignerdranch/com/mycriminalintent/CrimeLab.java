package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.StringBuilderPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import criminalintent.android.bignerdranch.com.mycriminalintent.database.CrimeBaseHelper;
import criminalintent.android.bignerdranch.com.mycriminalintent.database.CrimeCursorWrapper;
import criminalintent.android.bignerdranch.com.mycriminalintent.database.CrimeDBSchema.CrimeTable;

/**
 * Created by Bender on 12/09/2015.
 */
public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private final Context mContext;
	private SQLiteDatabase mDataBase;

	private CrimeLab(Context context){
		mContext = context.getApplicationContext();
		mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();
	}

	public List<Crime> getCrimes() {
		ArrayList<Crime> crimes = new ArrayList<>();
		CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
		try {
			cursorWrapper.moveToFirst();
			while(!cursorWrapper.isAfterLast()) {
				Crime crime = cursorWrapper.getCrime();
				crimes.add(crime);
				cursorWrapper.moveToNext();
			}
		}finally {
			cursorWrapper.close();
		}

		return crimes;
	}



	public Crime getCrime(UUID id) {
		Crime crime = null;
		CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID +  " = ? ", new String[]{id.toString()});

		try {
			if(cursorWrapper.getCount() > 0) {
				cursorWrapper.moveToFirst();
				crime = cursorWrapper.getCrime();
			}
		}finally {
			cursorWrapper.close();
		}

		return crime;
	}

	public void updateCrime(Crime crime){
		ContentValues values = getContentValues(crime);
		mDataBase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID  + " = ? ", new String[]{crime.getId().toString()});
	}

	public void addCrime(Crime crime){
		mDataBase.insert(CrimeTable.NAME, null, getContentValues(crime));
	}

	public void removeCrime(Crime crime) {
		mDataBase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ? ", new String[]{crime.getId().toString()});
	}


	public static CrimeLab getCrimeLab(Context context){
		if(sCrimeLab == null){
			sCrimeLab = new CrimeLab(context);
		}
		return sCrimeLab;
	}

	public static ContentValues getContentValues(Crime crime){
		ContentValues contentValues = new ContentValues();
		contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
		contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
		contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
		contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved()? 1: 0);
		contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
		return contentValues;
	}

	private CrimeCursorWrapper queryCrimes(String where, String[] args){
		Cursor cursor = mDataBase.query(CrimeTable.NAME, null //select all columns
				, where, args, null, null, null);
		return new CrimeCursorWrapper(cursor);
	}

	public File getPhotoFile(Crime crime) {
		File externalDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if(externalDir == null){
			return null;
		}
		return new File(externalDir, crime.getPhotoFileName());
	}


}

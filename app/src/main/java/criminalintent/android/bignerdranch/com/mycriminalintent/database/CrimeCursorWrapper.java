package criminalintent.android.bignerdranch.com.mycriminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import criminalintent.android.bignerdranch.com.mycriminalintent.Crime;
import criminalintent.android.bignerdranch.com.mycriminalintent.database.CrimeDBSchema.CrimeTable;
/**
 * Created by Bender on 13/09/2015.
 */
public class CrimeCursorWrapper extends CursorWrapper {

	public CrimeCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public Crime getCrime(){
		String uuidStr = getString(getColumnIndex(CrimeTable.Cols.UUID));
		String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
		long time = getLong(getColumnIndex(CrimeTable.Cols.DATE));
		int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

		Crime crime = new Crime(UUID.fromString(uuidStr));
		crime.setTitle(title);
		crime.setDate(new Date(time));
		crime.setSolved(solved == 1);
		return crime;
	}
}

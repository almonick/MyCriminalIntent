package criminalintent.android.bignerdranch.com.mycriminalintent.database;

/**
 * Created by Bender on 13/09/2015.
 */
public class CrimeDBSchema {
	public final static class CrimeTable{

		public static final String NAME = "crimes";

		public static final class Cols{
			public static final String UUID = "uuid";
			public static final String DATE = "date";
			public static final String TITLE = "title";
			public static final String SOLVED = "solved";
		}

	}
}

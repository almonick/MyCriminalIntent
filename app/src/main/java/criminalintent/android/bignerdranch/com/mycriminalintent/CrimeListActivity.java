package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

	private static final String TAG = CrimeListActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}




}

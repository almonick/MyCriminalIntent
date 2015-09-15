package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks{

	private static final String TAG = CrimeListActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_master_detail;
	}


	@Override
	public void onCrimeSelected(Crime crime) {
		if(findViewById(R.id.detail_fragment_container) == null){
			Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
			startActivity(intent);
		}else{
			Fragment crimeFragment = CrimeFragment.newInstance(crime.getId());
			getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, crimeFragment).commit();
		}
	}
}

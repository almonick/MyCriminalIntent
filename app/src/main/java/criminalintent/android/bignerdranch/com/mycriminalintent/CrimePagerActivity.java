package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by Bender on 12/09/2015.
 */
public class CrimePagerActivity extends AppCompatActivity {

	private static final String EXTRA_CRIME_ID = "crimeId";
	public static final String TAG = CrimePagerActivity.class.getSimpleName();
	ViewPager mViewPager;
	List<Crime> mCrimes;

	public static Intent newIntent(Context context, UUID id){
		Intent intent = new Intent(context, CrimePagerActivity.class);
		intent.putExtra(EXTRA_CRIME_ID, id);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime_pager);
		UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		CrimeLab crimeLab = CrimeLab.getCrimeLab(this);

		mCrimes = crimeLab.getCrimes();
		Crime crime = crimeLab.getCrime(uuid);
		mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
		mViewPager.setAdapter(new CrimePagerAdapter(getSupportFragmentManager()));
		for(int i = 0 ; i < mCrimes.size() ; i++){
			if(mCrimes.get(i).getId().equals(uuid)){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}


	private class CrimePagerAdapter extends FragmentStatePagerAdapter{
		public CrimePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Crime crime = mCrimes.get(position);
			return CrimeFragment.newInstance(crime.getId());
		}

		@Override
		public int getCount() {
			return mCrimes.size();
		}
	}
}

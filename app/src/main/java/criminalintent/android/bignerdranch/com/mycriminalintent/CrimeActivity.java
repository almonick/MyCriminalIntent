package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity implements CrimeFragment.Callbacks {

	private static final String TAG = CrimeActivity.class.getSimpleName();
	public static final  String EXTRA_CRIME_ID = "criminalintent.android.bignerdranch.com.crimeActivity.crimeID";
	private UUID mId;
	private CrimeFragment mCrimeFragment;

	public static Intent newIntent(Context context, UUID id){
		Intent intent = new Intent(context, CrimeActivity.class);
		intent.putExtra(CrimeActivity.EXTRA_CRIME_ID, id);
		return intent;
	}

	@Override
	protected Fragment createFragment() {
		mId = (UUID) getIntent().getExtras().getSerializable(EXTRA_CRIME_ID);
		mCrimeFragment = CrimeFragment.newInstance(mId);
		return mCrimeFragment;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "###CrimeActivity onBackPressed");
	}


	@Override
	public void onCrimeUpdated(Crime crime) {

	}
}

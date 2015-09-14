package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Bender on 12/09/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
	protected abstract Fragment createFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
		FragmentManager fragmentManager = getSupportFragmentManager();

		Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
		if(fragment == null){
			fragment = createFragment();
			fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
		}

	}
}

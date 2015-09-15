package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Bender on 12/09/2015.
 */
public class CrimeListFragment extends Fragment {
	private static final String TAG = CrimeListFragment.class.getSimpleName();

	private static final String SAVE_SUBTITLE_VISIBLE = "subtitleVisible";

	private RecyclerView mCrimesRecyclerView;
	private CrimeAdapter mAdapter;
	private boolean mSubVisible;
	private Callbacks mCallbacks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
		if(savedInstanceState != null){
			mSubVisible = savedInstanceState.getBoolean(SAVE_SUBTITLE_VISIBLE);
		}
		mCrimesRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
		mCrimesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		updateUI();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "### crimeListFragment on resume");
		updateUI();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVE_SUBTITLE_VISIBLE, mSubVisible);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);

		MenuItem item = menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubVisible){
			item.setTitle(R.string.hide_subtitle);
		}else{
			item.setTitle(R.string.show_subtitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_item_new_crime:
				Crime crime = new Crime();
				CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
				Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
				startActivity(intent);
				return true;
			case R.id.menu_item_show_subtitle:
				mSubVisible = !mSubVisible;
				getActivity().invalidateOptionsMenu();
				updateSubtitle();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void updateSubtitle(){
		int numCrimes = CrimeLab.getCrimeLab(getActivity()).getCrimes().size();
		String subtitle = mSubVisible? getResources().getQuantityString(R.plurals.subtitle_plurals, numCrimes, numCrimes) : null;
		((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
	}

	private void updateUI() {
		List<Crime> crimes = CrimeLab.getCrimeLab(getActivity()).getCrimes();
		if(mAdapter == null) {
			mAdapter = new CrimeAdapter(crimes);
			mCrimesRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.setCrimes(crimes);
			mAdapter.notifyDataSetChanged();
		}
		updateSubtitle();
	}


	private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private Crime mCrime;
		private TextView mTitle, mDate;
		private CheckBox mSolved;

		public CrimeHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			mTitle = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
			mDate = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
			mSolved = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_checkBox);
			mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mCrime.setSolved(isChecked);
					CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
				}
			});
		}

		public void bindCrime(Crime crime) {
			mCrime = crime;
			mTitle.setText(mCrime.getTitle());
			mDate.setText(mCrime.getDate().toString());
			mSolved.setChecked(mCrime.isSolved());
		}

		@Override
		public void onClick(View v) {
			mCallbacks.onCrimeSelected(mCrime);
		}
	}

	private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

		public List<Crime> mCrimes;

		public CrimeAdapter(List<Crime> crimes) {
			mCrimes = crimes;
		}

		public void setCrimes(List<Crime> crimes) {
			mCrimes = crimes;
		}


		@Override
		public CrimeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.list_item_crime, viewGroup, false);
			return new CrimeHolder(view);
		}

		@Override
		public void onBindViewHolder(CrimeHolder crimeHolder, int i) {
			Crime crime = mCrimes.get(i);
			crimeHolder.bindCrime(crime);
		}


		@Override
		public int getItemCount() {
			return mCrimes.size();
		}
	}

	public interface Callbacks{
		void onCrimeSelected(Crime crime);

	}

}



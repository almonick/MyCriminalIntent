package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

	private static final String TAG = CrimeFragment.class.getSimpleName();
	private static final int REQUEST_DATE_CHANGE = 10;
	private static final int REQUEST_CONTACTS = 20;
	private static final String ARG_CRIME_ID = "crimeId";
	public static final String DIALOG_DATE = "DialogDate";
	private Crime mCrime;
	private EditText mTitle;
	private Button mDateButton, mSuspectBtn, mReportBtn;
	private CheckBox mSolvedCheckBox;
	private ImageView mImageView;
	private ImageButton mCameraButton;


	public static CrimeFragment newInstance(UUID id) {
		CrimeFragment fragment = new CrimeFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_CRIME_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	public CrimeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Fragment onCreate");
		setHasOptionsMenu(true);
		UUID id = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
		mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(id);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		Log.d(TAG, "Fragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_crime, container, false);
		mTitle = (EditText) view.findViewById(R.id.crime_title);
		mTitle.setText(mCrime.getTitle());
		mTitle.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				mCrime.setTitle(s.toString());
			}
		});
		mDateButton = (Button) view.findViewById(R.id.crime_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dialogFragment = DatePickerFragment.newInstance(mCrime.getDate());
				dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE_CHANGE);
				dialogFragment.show(getFragmentManager(), DIALOG_DATE);
			}
		});
		mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
			}
		});

		final Intent pickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		mSuspectBtn = (Button) view.findViewById(R.id.crime_suspect);
		mSuspectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(pickIntent, REQUEST_CONTACTS);
			}
		});
		updateSuspect(mCrime.getSuspect());

		if(getActivity().getPackageManager().resolveActivity(pickIntent, PackageManager.MATCH_DEFAULT_ONLY) == null){
			mSuspectBtn.setEnabled(false);
		}
		mReportBtn = (Button) view.findViewById(R.id.crime_report);
		mReportBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain").setText(getCrimeReport()).setSubject(getString(R.string.crime_report_title)).getIntent();
//				Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setType("text/plain");
//				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_title));
//				intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				startActivity(intent);

			}
		});

		mCameraButton = (ImageButton) view.findViewById(R.id.camera_btn);
		mCameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		mImageView = (ImageView) view.findViewById(R.id.crime_photo);
		return view;
	}

	private void updateDate() {
		String dateFormat = "EEE, dd MMM";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		mDateButton.setText(dateString);
	}

	private String getCrimeReport(){
		String  solvedString = mCrime.isSolved()? getString(R.string.crime_report_solved) : getString( R.string.crime_report_unsolved);
		String dateFormat = "EEE, dd MMM";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		String suspect =  mCrime.getSuspect() != null ? mCrime.getSuspect() : getString(R.string.crime_report_no_suspect);
		String report = getString(R.string.crime_report_format, mCrime.getTitle(), dateString, solvedString, suspect);
		return report;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK)return;

		if(requestCode == REQUEST_DATE_CHANGE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}else if(requestCode == REQUEST_CONTACTS && data!= null){
			Uri uri = data.getData();
			Log.d("", "### uri " + uri);
			String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
			final Cursor cursor = getActivity().getContentResolver().query(uri, queryFields, null, null, null);
			try{
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					updateSuspect(suspect);
				}
			}finally {
				cursor.close();
			}
		}
	}

	private void updateSuspect(String suspect){
		if(suspect != null){
			mSuspectBtn.setText(suspect);
			mCrime.setSuspect(suspect);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_item_delete_crime:
				CrimeLab.getCrimeLab(getActivity()).removeCrime(mCrime);
				getActivity().finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "Fragment onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "Fragment onAttach");

	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "Fragment onDetach");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "Fragment onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Fragment onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "Fragment onPause");
		CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Fragment onDestroy");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d(TAG, "Fragment onViewCreated");
	}
}

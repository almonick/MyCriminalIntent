package criminalintent.android.bignerdranch.com.mycriminalintent;

		import android.app.Activity;
		import android.app.Dialog;
		import android.content.DialogInterface;
		import android.content.Intent;
		import android.os.Bundle;
		import android.support.annotation.NonNull;
		import android.support.v4.app.DialogFragment;
		import android.support.v7.app.AlertDialog;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.widget.DatePicker;

		import java.util.Calendar;
		import java.util.Date;
		import java.util.GregorianCalendar;

/**
 * Created by Bender on 13/09/2015.
 */
public class DatePickerFragment extends DialogFragment {

	private static final String ARG_DATE = "ARG_DATE";
	public static final String EXTRA_DATE = "criminalintent.android.bignerdranch.com.mycriminalintent.datepicker.extraData";

	public static DatePickerFragment newInstance(Date date) {

		Bundle args = new Bundle();
		args.putSerializable(ARG_DATE, date);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Date date = (Date) getArguments().getSerializable(ARG_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

		return new AlertDialog.Builder(getActivity()).setTitle(R.string.crime_date_picker_title)
				.setView(view)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int year = datePicker.getYear();
						int month = datePicker.getMonth();
						int day = datePicker.getDayOfMonth();
						Date date = new GregorianCalendar(year, month, day).getTime();
						setResult(Activity.RESULT_OK, date);
					}
				}).create();
	}

	private void setResult(int resultCode, Date date) {
		if(getTargetFragment() == null) return;
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
}

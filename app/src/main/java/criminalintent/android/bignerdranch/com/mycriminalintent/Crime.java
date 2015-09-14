package criminalintent.android.bignerdranch.com.mycriminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Bender on 11/09/2015.
 */
public class Crime {
	private UUID mId;
	private String mTitle;
	private Date   mDate;
	private boolean mSolved;

	public Crime(){
		this(UUID.randomUUID());
	}


	public Crime(UUID id){
		mId = id;
		mDate = new Date();
	}


	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean isSolved){
		mSolved = isSolved;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	@Override
	public String toString() {
		return "Crime " + mTitle + " solved " + mSolved + " [" + mId + "]";
	}
}

package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.content.Context;

import java.io.File;
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
	private String mSuspect;

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}



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

	public String getPhotoFileName(){
		return "IMAGE_" + mId.toString() + ".jpg";
	}

	@Override
	public String toString() {
		return "Crime " + mTitle + " solved " + mSolved +  " suspect: " +  mSuspect +" [" + mId + "]";
	}
}

package com.xiaomi.smarthome.common.ui.dialog;

import java.text.NumberFormat;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaomi.common.R;

public class XQProgressHorizontalDialog extends MLAlertDialog {

	private Context mContext;

	private ProgressBar mDeterminateProgress;
	private TextView mProgressMessage;
	private TextView mProgressPercent;
//	private TextView mProgressNumber;

	private CharSequence mMessage = null;
	private String mProgressNumberFormat;
	private NumberFormat mProgressPercentFormat;

	public static XQProgressHorizontalDialog show(Context context,
			CharSequence message) {
		XQProgressHorizontalDialog dialog = new XQProgressHorizontalDialog(
				context);
		dialog.setMessage(message);
		dialog.show();
		return dialog;
	}

	public XQProgressHorizontalDialog(Context context) {
		this(context, R.style.V5_AlertDialog);
	}

	public XQProgressHorizontalDialog(Context context, int theme) {
		super(context, theme);

		initFormats();
		mContext = context;
		setCancelable(true);
	}

	private void initFormats() {
		mProgressNumberFormat = "%1d/%2d";
		mProgressPercentFormat = NumberFormat.getPercentInstance();
		mProgressPercentFormat.setMaximumFractionDigits(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.xq_progress_horizital_dialog,
				null);

		mDeterminateProgress = (ProgressBar) view.findViewById(R.id.progress);
		mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
		mProgressMessage = (TextView) view.findViewById(R.id.progress_message);
//		mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
		setView(view);

		if (mMessage != null) {
			setMessage(mMessage);
		}

		super.onCreate(savedInstanceState);
	}

	public void setProgress(int max, int progress) {
		if (mDeterminateProgress == null || max < 0)
			return;
		mDeterminateProgress.setMax(max);
		mDeterminateProgress.setProgress(progress);
		if (mProgressPercentFormat != null) {
			double percent = (double) progress / (double) max;
			SpannableString tmp = new SpannableString(
					mProgressPercentFormat.format(percent));
			mProgressPercent.setText(tmp);
		} else {
			mProgressPercent.setText("");
		}
//		if (max > 1) {
//			mProgressNumber.setText("" + (progress/1024) + "K/" + (max/1024)+"K");
//		} else {
//			mProgressNumber.setText("");
//		}
	}

	public int getMax() {
		if (mDeterminateProgress != null) {
			return mDeterminateProgress.getMax();
		}

		return 0;
	}

	public int getProgress() {
		if (mDeterminateProgress != null) {
			return mDeterminateProgress.getProgress();
		}

		return 0;
	}

	@Override
	public void setMessage(CharSequence message) {
		if (mProgressMessage != null) {
			mProgressMessage.setText(message);
		} else {
			mMessage = message;
		}
	}

}

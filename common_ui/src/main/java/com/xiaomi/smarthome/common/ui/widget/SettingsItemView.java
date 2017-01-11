package com.xiaomi.smarthome.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomi.common.R;

/**
 * Created by livy on 16/9/12.
 */
public class SettingsItemView extends FrameLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    TextView mTitleTextView;
    TextView mSubTitleTextView;
    TextView mInfoTextView;
    SwitchButton mSwitchButton;
    ImageView mOnclickImageView;
    View mContainerView;
    ImageView mSelectImageView;
    View mTitleContainer;
    int mType;
    OnClickListener mOnClickListener;
    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    boolean mSelected = false;

    public static interface OnSelectedListener {
        public void onSelected(View view);
    }

    OnSelectedListener mOnSelectedListener;

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mOnSelectedListener = listener;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public void setChecked(boolean checked) {
        if (mSwitchButton != null) {
            mSwitchButton.setOnCheckedChangeListener(null);
            mSwitchButton.setChecked(checked);
            mSwitchButton.setOnCheckedChangeListener(this);
        }
    }

    public boolean isChecked() {
        if (mSwitchButton != null) {
            return mSwitchButton.isChecked();
        }
        return false;
    }

    public View getInfoView() {
        return mInfoTextView;
    }

    public SettingsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.settings_item, null);
        mContainerView = itemView;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(itemView, lp);
        itemView.setBackgroundDrawable(getBackground());
        mTitleTextView = (TextView) itemView.findViewById(R.id.settings_item_title);
        mSubTitleTextView = (TextView) itemView.findViewById(R.id.settings_item_sub_title);
        mSwitchButton = (SwitchButton) itemView.findViewById(R.id.settings_item_switch_btn);
        mOnclickImageView = (ImageView) itemView.findViewById(R.id.settings_item_arrow);
        mInfoTextView = (TextView) itemView.findViewById(R.id.settings_item_info);
        mSelectImageView = (ImageView) itemView.findViewById(R.id.settings_item_select);
        mTitleContainer = itemView.findViewById(R.id.title_container);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsItem, 0, 0);
        String title = a.getString(R.styleable.SettingsItem_item_title);
        String subTitle = a.getString(R.styleable.SettingsItem_item_subtitle);
        String info = a.getString(R.styleable.SettingsItem_item_info);
        mType = a.getInt(R.styleable.SettingsItem_item_type, 1);
        mSelected = a.getBoolean(R.styleable.SettingsItem_item_select, false);
        int lineMargin = a.getDimensionPixelSize(R.styleable.SettingsItem_item_line_margin, 0);
        a.recycle();

        setTitle(title);
        setSubTitle(subTitle);
        setInfo(info);
        setType(mType);

        View view = new View(getContext());
        view.setBackgroundColor(0xffe5e5e5);
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lp.gravity = Gravity.BOTTOM;
//        int marging = getResources().getDimensionPixelOffset(R.dimen.settings_item_margin);
        lp.setMargins(lineMargin, 0, lineMargin, 0);
        addView(view, lp);
    }

    public void setTitle(String str) {
        mTitleTextView.setText(str);
    }

    public void setSubTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            mSubTitleTextView.setVisibility(View.GONE);
        } else {
            mSubTitleTextView.setText(str);
            mSubTitleTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setInfo(String str) {
        if (!TextUtils.isEmpty(str)) {
            mInfoTextView.setText(str);
            mInfoTextView.setVisibility(View.VISIBLE);
        } else {
            mInfoTextView.setVisibility(View.GONE);
        }
    }

    public void setSelect(boolean select) {
        mSelected = select;
        if (select) {
            mSelectImageView.setVisibility(VISIBLE);
            mTitleTextView.setTextColor(getResources().getColor(R.color.std_word_008));
        } else {
            mTitleTextView.setTextColor(getResources().getColor(R.color.settings_title_text_color));
            mSelectImageView.setVisibility(INVISIBLE);
        }
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setType(int type) {
        mType = type;
        if (mType == 0) {//none
            mSwitchButton.setVisibility(View.GONE);
            mOnclickImageView.setVisibility(View.GONE);
            mSelectImageView.setVisibility(GONE);
        } else if (mType == 1) {//arrows
            mSwitchButton.setVisibility(View.GONE);
            mContainerView.setOnClickListener(this);
            mSelectImageView.setVisibility(GONE);
        } else if (mType == 2) {//switch
            mOnclickImageView.setVisibility(View.GONE);
            mSwitchButton.setOnCheckedChangeListener(this);
            mSelectImageView.setVisibility(GONE);
        } else if (mType == 3) {//select
            mSwitchButton.setVisibility(View.GONE);
            mOnclickImageView.setVisibility(View.GONE);
            mContainerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mSelected) {
                        mSelected = true;
                        setSelect(mSelected);
                        if (mOnSelectedListener != null) {
                            mOnSelectedListener.onSelected(SettingsItemView.this);
                        }
                    }
                }
            });
            setSelect(mSelected);
        }
    }


    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        }
    }

    public void setSwitchEnable(boolean enable) {
        if (mSwitchButton != null) {
            mSwitchButton.setEnabled(enable);
        }
    }
}

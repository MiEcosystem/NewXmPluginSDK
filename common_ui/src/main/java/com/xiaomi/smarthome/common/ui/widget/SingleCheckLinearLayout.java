
package com.xiaomi.smarthome.common.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaomi.common.R;

/**
 * Created by zhangpengfei on 14-1-21.
 */
public class SingleCheckLinearLayout extends LinearLayout implements Checkable {

    View mSelectImageIconView;;
    Context mContext;
    TextView mTextView;

    public SingleCheckLinearLayout(Context context) {
        super(context);
    }

    public SingleCheckLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSelectImageIconView = findViewById(R.id.select_icon);
        mTextView = (TextView) findViewById(R.id.text1);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mSelectImageIconView.setVisibility(View.VISIBLE);
        } else {
            mSelectImageIconView.setVisibility(View.INVISIBLE);
        }
        mTextView.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return mTextView.isSelected();
    }

    @Override
    public void toggle() {

    }
}

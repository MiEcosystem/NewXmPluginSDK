
package com.xiaomi.xmplugindemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MyEditText extends EditText {

    public interface MyEditTextListener {
        void onImeBack(View view, String text);
    }

    private MyEditTextListener mListener;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addListener(MyEditTextListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mListener != null) {
                mListener.onImeBack(this, this.getText().toString());
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }

}

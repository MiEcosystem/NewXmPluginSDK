/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaomi.smarthome.common.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaomi.common.R;

/**
 * <p>A dialog showing a progress indicator and an optional text message or view.
 * Only a text message or a view can be used at the same time.</p>
 * <p>The dialog can be made cancelable on back key press.</p>
 * <p>The progress range is 0..10000.</p>
 */
public class XQProgressDialogSimple extends AlertDialog {

    private TextView mMessageView;

    private CharSequence mMessage;

    private Context mContext;

    public XQProgressDialogSimple(Context context) {
        this(context, R.style.XQProgressDialogSimple);
    }

    public XQProgressDialogSimple(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setIndeterminate(true);
    }

    public static XQProgressDialogSimple show(Context context) {
        return show(context, context.getString(R.string.refreshing_no_point));
    }

    public static XQProgressDialogSimple show(Context context, CharSequence message) {
        return show(context, message, true);
    }

    public static XQProgressDialogSimple show(Context context,
                                      CharSequence message, boolean cancelable) {
        return show(context, message, cancelable, null);
    }

    public static XQProgressDialogSimple show(Context context,
                                      CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        XQProgressDialogSimple dialog = new XQProgressDialogSimple(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.xq_progress_dialog_simple, null);
        setContentView(view);
        mMessageView = (TextView) view.findViewById(R.id.progress_message);
//        setView(view);
        setCanceledOnTouchOutside(false);
        if (mMessage != null) {
            setMessage(mMessage);
        }

    }

    public void setIndeterminate(boolean indeterminate) {
    }

    public boolean isIndeterminate() {
        return true;
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mMessageView != null) {
            mMessageView.setText(message);
        } else {
            mMessage = message;
        }
    }
}

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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xiaomi.common.R;
import com.xiaomi.smarthome.device.api.XmPluginCommonApi;

import java.util.Timer;
import java.util.TimerTask;

public class MLAlertDialog extends Dialog implements DialogInterface {

    private MLAlertController mAlert;

    public CharSequence[] mItemTexts;

    private Context mContext;

    private DismissCallBack mDismissCallBack;

    private int mStartY = 0;

    protected MLAlertDialog(Context context) {
        this(context, R.style.V5_AlertDialog);
    }

    protected MLAlertDialog(Context context, int theme) {
        this(context, theme, Gravity.BOTTOM, 0);
    }

    protected MLAlertDialog(Context context, int theme, int gravity, int startY) {
        super(context, theme);
        mAlert = new MLAlertController(context, this, getWindow(), gravity);
        mContext = context;
        mStartY = startY;
    }

    protected MLAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.V5_AlertDialog);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        mAlert = new MLAlertController(context, this, getWindow(), Gravity.BOTTOM);
        mContext = context;
    }

    protected MLAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener,
            int gravity, int startY) {
        super(context, R.style.V5_AlertDialog);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        mAlert = new MLAlertController(context, this, getWindow(), gravity);
        mContext = context;
        mStartY = startY;
    }

    /**
     * Gets one of the buttons used in the dialog.
     * <p/>
     * If a button does not exist in the dialog, null will be returned.
     * 
     * @param whichButton The identifier of the button that should be returned.
     *            For example, this can be
     *            {@link DialogInterface#BUTTON_POSITIVE}.
     * @return The button from the dialog, or null if a button does not exist.
     */
    public Button getButton(int whichButton) {
        return mAlert.getButton(whichButton);
    }

    /**
     * Gets the list view used in the dialog.
     * 
     * @return The {@link ListView} from the dialog.
     */
    public ListView getListView() {
        return mAlert.getListView();
    }

    public View getView() {
        return mAlert.getView();
    }

    public EditText getInputView() {
        return (EditText) mAlert.getView();
    }

    public CharSequence[] getItemTexts() {
        return this.mItemTexts;
    }

    private void showSoftInput() {
        if (mAlert.getView() != null && mAlert.getView() instanceof EditText) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mAlert.getView(),
                            InputMethodManager.RESULT_SHOWN);
                }
            }, 200);
        }
    }

    private void hideSoftInput() {
        if (mAlert.getView() != null) {
            final InputMethodManager inputMethodManager = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mAlert.getView().getWindowToken(), 0);
        }
    }

    @Override
    public void dismiss() {
        if (mDismissCallBack != null) {
            mDismissCallBack.beforeDismissCallBack();
        }
        hideSoftInput();
        super.dismiss();
        if (mDismissCallBack != null) {
            mDismissCallBack.afterDismissCallBack();
        }
    }

    /**
     * 对话框点击按钮之后默认会自动消失 如果不希望点击按钮之后自动消失，可以使用此方法
     * 
     * @param autoDismiss
     * @return
     */
    public void setAudoDismiss(boolean autoDismiss) {
        mAlert.setAudoDismiss(autoDismiss);
        if (autoDismiss) {
            mAlert.sendDismissMessage();
        }
    }

    /**
     * 设置dialog dismiss时的回调
     * 
     * @param callBack
     */
    public void setDismissCallBack(DismissCallBack callBack) {
        this.mDismissCallBack = callBack;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mAlert.setTitle(title);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.y = mStartY;
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lp);

        showSoftInput();
    }

    /**
     * @see Builder#setCustomTitle(View)
     */
    public void setCustomTitle(View customTitleView) {
        mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        mAlert.setMessage(message);
    }

    /**
     * Set the view to display in that dialog.
     */
    public void setView(View view) {
        mAlert.setView(view);
    }

    /**
     * Set the view to display in that dialog, specifying the spacing to appear
     * around that view.
     * 
     * @param view The view to show in the content area of the dialog
     * @param viewSpacingLeft Extra space to appear to the left of {@code view}
     * @param viewSpacingTop Extra space to appear above {@code view}
     * @param viewSpacingRight Extra space to appear to the right of
     *            {@code view}
     * @param viewSpacingBottom Extra space to appear below {@code view}
     */
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
            int viewSpacingBottom) {
        mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /**
     * Set a message to be sent when a button is pressed.
     * 
     * @param whichButton Which button to set the message for, can be one of
     *            {@link DialogInterface#BUTTON_POSITIVE},
     *            {@link DialogInterface#BUTTON_NEGATIVE}, or
     *            {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text The text to display in positive button.
     * @param msg The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, CharSequence text, Message msg) {
        mAlert.setButton(whichButton, text, null, msg);
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is
     * pressed.
     * 
     * @param whichButton Which button to set the listener on, can be one of
     *            {@link DialogInterface#BUTTON_POSITIVE},
     *            {@link DialogInterface#BUTTON_NEGATIVE}, or
     *            {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text The text to display in positive button.
     * @param listener The
     *            {@link OnClickListener} to
     *            use.
     */
    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null);
    }

    /**
     * @deprecated Use {@link #setButton(int, CharSequence, Message)}
     *             with {@link DialogInterface#BUTTON_POSITIVE}.
     */
    @Deprecated
    public void setButton(CharSequence text, Message msg) {
        setButton(BUTTON_POSITIVE, text, msg);
    }

    /**
     * @deprecated Use {@link #setButton(int, CharSequence, Message)}
     *             with {@link DialogInterface#BUTTON_NEGATIVE}.
     */
    @Deprecated
    public void setButton2(CharSequence text, Message msg) {
        setButton(BUTTON_NEGATIVE, text, msg);
    }

    /**
     * @deprecated Use {@link #setButton(int, CharSequence, Message)}
     *             with {@link DialogInterface#BUTTON_NEUTRAL}.
     */
    @Deprecated
    public void setButton3(CharSequence text, Message msg) {
        setButton(BUTTON_NEUTRAL, text, msg);
    }

    /**
     * Set a listener to be invoked when button 1 of the dialog is pressed.
     * 
     * @param text The text to display in button 1.
     * @param listener The
     *            {@link OnClickListener} to
     *            use.
     * @deprecated Use
     *             {@link #setButton(int, CharSequence, OnClickListener)}
     *             with {@link DialogInterface#BUTTON_POSITIVE}
     */
    @Deprecated
    public void setButton(CharSequence text, final OnClickListener listener) {
        setButton(BUTTON_POSITIVE, text, listener);
    }

    /**
     * Set a listener to be invoked when button 2 of the dialog is pressed.
     * 
     * @param text The text to display in button 2.
     * @param listener The
     *            {@link OnClickListener} to
     *            use.
     * @deprecated Use
     *             {@link #setButton(int, CharSequence, OnClickListener)}
     *             with {@link DialogInterface#BUTTON_NEGATIVE}
     */
    @Deprecated
    public void setButton2(CharSequence text, final OnClickListener listener) {
        setButton(BUTTON_NEGATIVE, text, listener);
    }

    /**
     * Set a listener to be invoked when button 3 of the dialog is pressed.
     * 
     * @param text The text to display in button 3.
     * @param listener The
     *            {@link OnClickListener} to
     *            use.
     * @deprecated Use
     *             {@link #setButton(int, CharSequence, OnClickListener)}
     *             with {@link DialogInterface#BUTTON_POSITIVE}
     */
    @Deprecated
    public void setButton3(CharSequence text, final OnClickListener listener) {
        setButton(BUTTON_NEUTRAL, text, listener);
    }

    /**
     * Set resId to 0 if you don't want an icon.
     * 
     * @param resId the resourceId of the drawable to use as the icon or 0 if
     *            you don't want an icon.
     */
    public void setIcon(int resId) {
        mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        mAlert.setIcon(icon);
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        mAlert.setInverseBackgroundForced(forceInverseBackground);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAlert.installContent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event))
            return true;
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private final MLAlertController.AlertParams P;
        private Context mContext;

        public EditText getInputView() {
            return (EditText) P.mView;
        }

        /**
         * Constructor using a context for this builder and the
         * {@link android.app.AlertDialog} it creates.
         */
        public Builder(Context context) {
            this.mContext = context;
            P = new MLAlertController.AlertParams(context);
        }

        /**
         * Set the title using the given resource id.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        /**
         * Set the title using the custom view {@code customTitleView}. The
         * methods {@link #setTitle(int)} and {@link #setIcon(int)} should be
         * sufficient for most titles, but this is provided if the title needs
         * more customization. Using this will replace the title and icon set
         * via the other methods.
         * 
         * @param customTitleView The custom view to use as the title.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setCustomTitle(View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        /**
         * Set the resource id of the {@link Drawable}
         * to be used in the title.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setIcon(int iconId) {
            P.mIconId = iconId;
            return this;
        }

        /**
         * Set the {@link Drawable} to be used in the
         * title.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setIcon(Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog
         * is pressed.
         * 
         * @param textId The resource id of the text to display in the positive
         *            button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog
         * is pressed.
         * 
         * @param text The text to display in the positive button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog
         * is pressed.
         * 
         * @param textId The resource id of the text to display in the negative
         *            button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog
         * is pressed.
         * 
         * @param text The text to display in the negative button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is
         * pressed.
         * 
         * @param textId The resource id of the text to display in the neutral
         *            button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setNeutralButton(int textId, final OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is
         * pressed.
         * 
         * @param text The text to display in the neutral button
         * @param listener The
         *            {@link OnClickListener} to
         *            use.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not. Default is true.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         * @see #setCancelable(boolean)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the
         * dialog.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. This
         * should be an array type i.e. R.array.foo
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setItems(int itemsId, final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setItems(CharSequence[] items, final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given
         * {@link ListAdapter}, to be displayed in the dialog as
         * the content, you will be notified of the selected item via the
         * supplied listener.
         * 
         * @param adapter The {@link ListAdapter} to supply the
         *            list of items
         * @param listener The listener that will be called when an item is
         *            clicked.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given
         * {@link Cursor}, to be displayed in the dialog as the
         * content, you will be notified of the selected item via the supplied
         * listener.
         * 
         * @param cursor The {@link Cursor} to supply the list
         *            of items
         * @param listener The listener that will be called when an item is
         *            clicked.
         * @param labelColumn The column name on the cursor containing the
         *            string to display in the label.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setCursor(final Cursor cursor, final OnClickListener listener,
                String labelColumn) {
            P.mCursor = cursor;
            P.mLabelColumn = labelColumn;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. This
         * should be an array type, e.g. R.array.foo. The list will have a check
         * mark displayed to the right of the text for each checked item.
         * Clicking on an item in the list will not dismiss the dialog. Clicking
         * on a button will dismiss the dialog.
         * 
         * @param itemsId the resource id of an array i.e. R.array.foo
         * @param checkedItems specifies which items are checked. It should be
         *            null in which case no items are checked. If non null it
         *            must be exactly the same length as the array of items.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
                final OnMultiChoiceClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * each checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         * 
         * @param items the text of the items to be displayed in the list.
         * @param checkedItems specifies which items are checked. It should be
         *            null in which case no items are checked. If non null it
         *            must be exactly the same length as the array of items.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                final OnMultiChoiceClickListener listener) {
            P.mItems = items;
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * each checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         * 
         * @param cursor the cursor used to provide the items.
         * @param isCheckedColumn specifies the column name on the cursor to use
         *            to determine whether a checkbox is checked or not. It must
         *            return an integer value where 1 means checked and 0 means
         *            unchecked.
         * @param labelColumn The column name on the cursor containing the
         *            string to display in the label.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn,
                String labelColumn,
                final OnMultiChoiceClickListener listener) {
            P.mCursor = cursor;
            P.mOnCheckboxClickListener = listener;
            P.mIsCheckedColumn = isCheckedColumn;
            P.mLabelColumn = labelColumn;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. This
         * should be an array type i.e. R.array.foo The list will have a check
         * mark displayed to the right of the text for the checked item.
         * Clicking on an item in the list will not dismiss the dialog. Clicking
         * on a button will dismiss the dialog.
         * 
         * @param itemsId the resource id of an array i.e. R.array.foo
         * @param checkedItem specifies which item is checked. If -1 no items
         *            are checked.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setSingleChoiceItems(int itemsId, int checkedItem,
                final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         * 
         * @param cursor the cursor to retrieve the items from.
         * @param checkedItem specifies which item is checked. If -1 no items
         *            are checked.
         * @param labelColumn The column name on the cursor containing the
         *            string to display in the label.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn,
                final OnClickListener listener) {
            P.mCursor = cursor;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mLabelColumn = labelColumn;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         * 
         * @param items the items to be displayed.
         * @param checkedItem specifies which item is checked. If -1 no items
         *            are checked.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
                final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         * 
         * @param adapter The {@link ListAdapter} to supply the
         *            list of items
         * @param checkedItem specifies which item is checked. If -1 no items
         *            are checked.
         * @param listener notified when an item on the list is clicked. The
         *            dialog will not be dismissed when an item is clicked. It
         *            will only be dismissed if clicked on a button, if no
         *            buttons are supplied it's up to the user to dismiss the
         *            dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem,
                final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Sets a listener to be invoked when an item in the list is selected.
         * 
         * @param listener The listener to be invoked.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         * @see AdapterView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
         */
        public Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            P.mOnItemSelectedListener = listener;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied
         * view is an instance of a {@link ListView} the light
         * background will be used.
         * 
         * @param view The view to use as the contents of the Dialog.
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setView(View view) {
            P.mView = view;
            P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * 该方法不能与setView()方法同时使用
         * 
         * @return
         */
        public Builder setInputView(String str, boolean singleLine) {
            View view = View.inflate(mContext, R.layout.ml_alert_dialog_input_view, null);
            setView(view,
                    mContext.getResources().getDimensionPixelSize(
                            R.dimen.alertdialog_button_panel_padding_horizontal),
                    0,
                    mContext.getResources().getDimensionPixelSize(
                            R.dimen.alertdialog_button_panel_padding_horizontal),
                    mContext.getResources().getDimensionPixelSize(
                            R.dimen.alertdialog_custom_panel_padding_bottom));
            EditText et = ((EditText) view.findViewById(R.id.input_text));
            et.setSingleLine(singleLine);
            if (!TextUtils.isEmpty(str))
                et.setHint(str);
            et.requestFocus();
            return this;
        }

        public void setCustomTitle(CharSequence title) {
            P.mCustomTitle = title;
        }

        /**
         * 设置dialog dismiss时的回调
         * 
         * @param callBack
         */
        public Builder setDismissCallBack(DismissCallBack callBack) {
            P.mDismissCallBack = callBack;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog, specifying the
         * spacing to appear around that view. If the supplied view is an
         * instance of a {@link ListView} the light background
         * will be used.
         * 
         * @param view The view to use as the contents of the Dialog.
         * @param viewSpacingLeft Spacing between the left edge of the view and
         *            the dialog frame
         * @param viewSpacingTop Spacing between the top edge of the view and
         *            the dialog frame
         * @param viewSpacingRight Spacing between the right edge of the view
         *            and the dialog frame
         * @param viewSpacingBottom Spacing between the bottom edge of the view
         *            and the dialog frame
         * @return This Builder object to allow for chaining of calls to set
         *         methods This is currently hidden because it seems like people
         *         should just be able to put padding around the view.
         * @hide
         */
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop,
                int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop,
                int viewSpacingRight, int viewSpacingBottom, boolean transplant) {
            P.mView = view;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            P.mCustomBgTransplant = transplant;
            return this;
        }

        /**
         * Sets the Dialog to use the inverse background, regardless of what the
         * contents is.
         * 
         * @param useInverseBackground Whether to use the inverse background
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        /**
         * @hide
         */
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            P.mRecycleOnMeasure = enabled;
            return this;
        }

        /**
         * 对话框点击按钮之后默认会自动消失 如果不希望点击按钮之后自动消失，可以使用此方法
         * 
         * @param autoDismiss
         * @return
         */
        public Builder setAudoDismiss(boolean autoDismiss) {
            P.mAutoDismiss = autoDismiss;
            return this;
        }

        /**
         * Creates a {@link android.app.AlertDialog} with the arguments supplied
         * to this builder. It does not {@link Dialog#show()} the
         * dialog. This allows the user to do any extra processing before
         * displaying the dialog. Use {@link #show()} if you don't have any
         * other processing to do and want this to be created and displayed.
         */
        public MLAlertDialog create() {

            final MLAlertDialog dialog = new MLAlertDialog(P.mContext);
            dialog.mItemTexts = P.mItems;
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            dialog.setDismissCallBack(P.mDismissCallBack);

            try {
                XmPluginCommonApi.instance().setWindowAnimations(dialog);
            } catch (Throwable e) {

            }
            return dialog;
        }

        public MLAlertDialog createCenter() {

            final MLAlertDialog dialog = new MLAlertDialog(P.mContext, true, null, Gravity.CENTER,
                    0);
            dialog.mItemTexts = P.mItems;
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            dialog.setDismissCallBack(P.mDismissCallBack);
            return dialog;
        }

        /**
         * Creates a {@link android.app.AlertDialog} with the arguments supplied
         * to this builder and {@link Dialog#show()}'s the dialog.
         */
        public MLAlertDialog show() {
            MLAlertDialog dialog = create();
            dialog.show();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            return dialog;
        }

        public MLAlertDialog showCenter() {
            MLAlertDialog dialog = createCenter();
            dialog.show();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            return dialog;
        }
    }

    public static interface DismissCallBack {

        public void beforeDismissCallBack();

        public void afterDismissCallBack();

    }

}

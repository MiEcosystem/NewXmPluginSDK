/*
 * Copyright (C) 2008 The Android Open Source Project
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.xiaomi.common.R;

public class MLAlertController {

    private static final int BIT_BUTTON_POSITIVE = 1;

    private static final int BIT_BUTTON_NEGATIVE = 2;

    private static final int BIT_BUTTON_NEUTRAL = 4;

    private final Context mContext;

    private final DialogInterface mDialogInterface;

    private final Window mWindow;

    private CharSequence mTitle;

    private CharSequence mMessage;

    private ListView mListView;

    private View mView;

    private int mViewSpacingLeft;

    private int mViewSpacingTop;

    private int mViewSpacingRight;

    private int mViewSpacingBottom;

    private boolean mViewSpacingSpecified = false;

    private Button mButtonPositive;

    private CharSequence mButtonPositiveText;

    private Message mButtonPositiveMessage;

    private Button mButtonNegative;

    private CharSequence mButtonNegativeText;

    private Message mButtonNegativeMessage;

    private Button mButtonNeutral;

    private CharSequence mButtonNeutralText;

    private Message mButtonNeutralMessage;

    private ScrollView mScrollView;

    private int mIconId = -1;

    private Drawable mIcon;

    private ImageView mIconView;

    private TextView mTitleView;

    private TextView mMessageView;

    private View mCustomTitleView;

    private boolean mForceInverseBackground;

    private ListAdapter mAdapter;

    private int mCheckedItem = -1;

    private int mAlertDialogLayout;

    private int mListLayout;

    private int mListLayoutWithTitle;

    private int mMultiChoiceItemLayout;

    private int mSingleChoiceItemLayout;

    private int mListItemLayout;

    // add by afei for progressDialog Top and normal is Bottom
    private int mGravity;

    private Handler mHandler;

    private boolean mTransplantBg = false;

    private boolean mAutoDismiss = true; // 对话框在点击按钮之后是否自动消失

    private boolean mCustomBgTransplant = false;

    View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Message m = null;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                m = Message.obtain(mButtonNegativeMessage);
            } else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
                m = Message.obtain(mButtonNeutralMessage);
            }
            if (m != null) {
                m.sendToTarget();
            }

            if (mAutoDismiss) {
                // Post a message so we dismiss after the above handlers are
                // executed
                mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialogInterface)
                        .sendToTarget();
            }
        }
    };

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;
                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    public void sendDismissMessage() {
        mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialogInterface).sendToTarget();
    }

    public MLAlertController(Context context, DialogInterface di, Window window) {
        this(context, di, window, Gravity.BOTTOM);
    }

    public MLAlertController(Context context, DialogInterface di, Window window, int gravity) {
        mContext = context;
        mDialogInterface = di;
        mWindow = window;
        mHandler = new ButtonHandler(di);
        mAlertDialogLayout = R.layout.ml_alert_dialog;
        mListLayout = R.layout.ml_select_dialog;
        mListLayoutWithTitle = R.layout.ml_select_dialog_center;
        mMultiChoiceItemLayout = R.layout.ml_select_dialog_multichoice;
        mSingleChoiceItemLayout = R.layout.ml_select_dialog_singlechoice;
        mListItemLayout = R.layout.ml_select_dialog_item;
        mGravity = gravity;
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }
        if (!(v instanceof ViewGroup)) {
            return false;
        }
        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }
        return false;
    }

    public void installContent() {
        /* We use a custom title so never request a window title */
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setGravity(mGravity);
        if (mView == null || !canTextInput(mView)) {
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        mWindow.setContentView(mAlertDialogLayout);
        setupView();
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    /**
     * @see android.app.AlertDialog.Builder#setCustomTitle(View)
     */
    public void setCustomTitle(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setAudoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(View view) {
        mView = view;
        mViewSpacingSpecified = false;
    }

    public void setCustomTransplant(boolean b) {
        mCustomBgTransplant = b;
    }

    /**
     * Set the view to display in the dialog along with the spacing around that
     * view
     */
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
            int viewSpacingBottom) {
        mView = view;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    /**
     * Sets a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code listener} or {@code msg}.
     * 
     * @param whichButton Which button, can be one of
     *            {@link DialogInterface#BUTTON_POSITIVE},
     *            {@link DialogInterface#BUTTON_NEGATIVE}, or
     *            {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text The text to display in positive button.
     * @param listener The
     *            {@link DialogInterface.OnClickListener} to
     *            use.
     * @param msg The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, CharSequence text,
            DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mButtonNeutralText = text;
                mButtonNeutralMessage = msg;
                break;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    /**
     * Set resId to 0 if you don't want an icon.
     * 
     * @param resId the resourceId of the drawable to use as the icon or 0 if
     *            you don't want an icon.
     */
    public void setIcon(int resId) {
        mIconId = resId;
        if (mIconView != null) {
            if (resId > 0) {
                mIconView.setImageResource(mIconId);
            } else if (resId == 0) {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        if ((mIconView != null) && (mIcon != null)) {
            mIconView.setImageDrawable(icon);
        }
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        mForceInverseBackground = forceInverseBackground;
    }

    public ListView getListView() {
        return mListView;
    }

    public View getView() {
        return mView;
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return mButtonPositive;
            case DialogInterface.BUTTON_NEGATIVE:
                return mButtonNegative;
            case DialogInterface.BUTTON_NEUTRAL:
                return mButtonNeutral;
            default:
                return null;
        }
    }

    @SuppressWarnings({
            "UnusedDeclaration"
    })
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mListView != null
                && mListView.getVisibility() == View.VISIBLE) {
            this.mDialogInterface.dismiss();
        }
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    @SuppressWarnings({
            "UnusedDeclaration"
    })
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    private void setupView() {
        LinearLayout contentPanel = (LinearLayout) mWindow.findViewById(R.id.contentPanel);
        setupContent(contentPanel);
        boolean hasButtons = setupButtons();

        LinearLayout topPanel = (LinearLayout) mWindow.findViewById(R.id.topPanel);
        boolean hasTitle = setupTitle(topPanel);

        View buttonPanel = mWindow.findViewById(R.id.buttonPanel);
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }

        FrameLayout customPanel = (FrameLayout) mWindow.findViewById(R.id.customPanel);
        if (mView != null) {
            // 自定义dialog透明背景
            // mWindow.findViewById(R.id.parentPanel).setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            FrameLayout custom = (FrameLayout) mWindow.findViewById(R.id.custom);
            custom.addView(mView);
            if (mViewSpacingSpecified) {
                custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight,
                        mViewSpacingBottom);
                if (mCustomBgTransplant)
                    mTransplantBg = true;
            }
            if (mListView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
            }
        } else {
            customPanel.setVisibility(View.GONE);
        }

        if (mTransplantBg) {
            mWindow.findViewById(R.id.parentPanel).setBackgroundColor(
                    mContext.getResources().getColor(android.R.color.transparent));
        } else {
//            mWindow.findViewById(R.id.parentPanel).setBackgroundColor(0xffffffff);
        }

        if (mListView != null) {
            // Listview有分割线divider，因此header和listview需要显示分割线
            mWindow.findViewById(R.id.title_divider_line).setVisibility(View.VISIBLE);
            mWindow.findViewById(R.id.title_divider_line_bottom).setVisibility(View.VISIBLE);
        } else {
            mWindow.findViewById(R.id.title_divider_line).setVisibility(View.GONE);
            mWindow.findViewById(R.id.title_divider_line_bottom).setVisibility(View.GONE);
        }
        /**
         * Add margin top for the button panel if we have not any panel
         */
        if (topPanel.getVisibility() == View.GONE && contentPanel.getVisibility() == View.GONE
                && customPanel.getVisibility() == View.GONE && hasButtons) {
            buttonPanel.setPadding(buttonPanel.getPaddingLeft(), buttonPanel.getPaddingBottom(),
                    buttonPanel.getPaddingRight(), buttonPanel.getPaddingBottom());
        }

        /*
         * Only display the divider if we have a title and a custom view or a
         * message.
         */
        if (hasTitle) {
            // View divider = null;
            // if (mMessage != null || mView != null || mListView != null) {
            // divider = mWindow.findViewById(R.id.titleDivider);
            // } else {
            // divider = mWindow.findViewById(R.id.titleDividerTop);
            // }
            //
            // if (divider != null) {
            // divider.setVisibility(View.VISIBLE);
            // }
        }
        setBackground(topPanel, contentPanel, customPanel, hasButtons, hasTitle, buttonPanel);
        if (TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mMessage)) {
            mWindow.findViewById(R.id.empty_view).setVisibility(View.GONE);
        }
    }

    private boolean setupTitle(LinearLayout topPanel) {
        boolean hasTitle = true;

        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            topPanel.addView(mCustomTitleView, 0, lp);
            // Hide the title template
            View titleTemplate = mWindow.findViewById(R.id.title_template);
            titleTemplate.setVisibility(View.GONE);
        } else {
            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);

            mIconView = (ImageView) mWindow.findViewById(R.id.icon);
            if (hasTextTitle) {
                /* Display the title if a title is supplied, else hide it */
                mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);
                mTitleView.setText(mTitle);
                /*
                 * Do this last so that if the user has supplied any icons we
                 * use them instead of the default ones. If the user has
                 * specified 0 then make it disappear.
                 */
                if (mIconId > 0) {
                    mIconView.setImageResource(mIconId);
                } else if (mIcon != null) {
                    mIconView.setImageDrawable(mIcon);
                } else if (mIconId == 0) {
                    /*
                     * Apply the padding from the icon to ensure the title is
                     * aligned correctly.
                     */
                    mTitleView.setPadding(mIconView.getPaddingLeft(),
                            mIconView.getPaddingTop(),
                            mIconView.getPaddingRight(),
                            mIconView.getPaddingBottom());
                    mIconView.setVisibility(View.GONE);
                }
            } else {
                // Hide the title template
                View titleTemplate = mWindow.findViewById(R.id.title_template);
                titleTemplate.setVisibility(View.GONE);
                mIconView.setVisibility(View.GONE);
                topPanel.setVisibility(View.GONE);
                hasTitle = false;
            }
        }
        return hasTitle;
    }

    private void setupContent(LinearLayout contentPanel) {
        mScrollView = (ScrollView) mWindow.findViewById(R.id.scrollView);
        mScrollView.setFocusable(false);
        // Special case for users that only want to display a String
        mMessageView = (TextView) mWindow.findViewById(R.id.message);
        if (mMessageView == null) {
            return;
        }
        if (mMessage != null) {
            mMessageView.setText(mMessage);
        } else {
            mMessageView.setVisibility(View.GONE);
            mScrollView.removeView(mMessageView);
            if (mListView != null) {
                contentPanel.removeView(mWindow.findViewById(R.id.scrollView));
                contentPanel.addView(mListView,
                        new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                contentPanel.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
            } else {
                contentPanel.setVisibility(View.GONE);
            }
        }
    }

    private boolean setupButtons() {
        int whichButtons = 0;
        mButtonPositive = (Button) mWindow.findViewById(R.id.button1);
        mButtonPositive.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegative = (Button) mWindow.findViewById(R.id.button2);
        mButtonNegative.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        mButtonNeutral = (Button) mWindow.findViewById(R.id.button3);
        mButtonNeutral.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }

        if (shouldCenterSingleButton(whichButtons)) {
            if (whichButtons == BIT_BUTTON_POSITIVE) {
                centerButton(mButtonPositive);
            } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
                centerButton(mButtonNegative);
            } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
                centerButton(mButtonNeutral);
            }
        }
        return whichButtons != 0;
    }

    private static boolean shouldCenterSingleButton(int whichButton) {
        return whichButton == BIT_BUTTON_POSITIVE
                || whichButton == BIT_BUTTON_NEGATIVE
                || whichButton == BIT_BUTTON_NEUTRAL;
    }

    private void centerButton(TextView button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.weight = 0.5f;
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.common_button);
    }

    private void setBackground(LinearLayout topPanel, LinearLayout contentPanel,
            View customPanel, boolean hasButtons, boolean hasTitle,
            View buttonPanel) {

        if (mTransplantBg) {
            /* Get all the different background required */
            int fullDark = mContext.getResources().getColor(android.R.color.transparent);
            int topDark = mContext.getResources().getColor(android.R.color.transparent);
            int centerDark = mContext.getResources().getColor(android.R.color.transparent);
            int bottomDark = mContext.getResources().getColor(android.R.color.transparent);
            int fullBright = mContext.getResources().getColor(android.R.color.transparent);
            int topBright = mContext.getResources().getColor(android.R.color.transparent);
            int centerBright = mContext.getResources().getColor(android.R.color.transparent);
            int bottomBright = mContext.getResources().getColor(android.R.color.transparent);
            int bottomMedium = mContext.getResources().getColor(android.R.color.transparent);

            /*
             * We now set the background of all of the sections of the alert.
             * First collect together each section that is being displayed along
             * with whether it is on a light or dark background, then run
             * through them setting their backgrounds. This is complicated
             * because we need to correctly use the full, top, middle, and
             * bottom graphics depending on how many views they are and where
             * they appear.
             */

            View[] views = new View[4];
            boolean[] light = new boolean[4];
            View lastView = null;
            boolean lastLight = false;
            int pos = 0;
            if (hasTitle) {
                views[pos] = topPanel;
                light[pos] = false;
                pos++;
            }
            /*
             * The contentPanel displays either a custom text message or a
             * ListView. If it's text we should use the dark background for
             * ListView we should use the light background. If neither are there
             * the contentPanel will be hidden so set it as null.
             */
            views[pos] = (contentPanel.getVisibility() == View.GONE)
                    ? null : contentPanel;
            light[pos] = mListView != null;
            pos++;
            if (customPanel != null) {
                views[pos] = customPanel;
                light[pos] = mForceInverseBackground;
                pos++;
            }
            if (hasButtons) {
                views[pos] = buttonPanel;
                light[pos] = true;
            }
            boolean setView = false;
            for (pos = 0; pos < views.length; pos++) {
                View v = views[pos];
                if (v == null) {
                    continue;
                }
                if (lastView != null) {
                    if (!setView) {
                        lastView.setBackgroundResource(lastLight ? topBright : topDark);
                    } else {
                        lastView.setBackgroundResource(lastLight ? centerBright : centerDark);
                    }
                    setView = true;
                }
                lastView = v;
                lastLight = light[pos];
            }
            if (lastView != null) {
                if (setView) {

                    /*
                     * ListViews will use the Bright background but buttons use
                     * the Medium background.
                     */
                    lastView.setBackgroundResource(
                            lastLight ? (hasButtons ? bottomMedium : bottomBright) : bottomDark);
                } else {
                    lastView.setBackgroundResource(lastLight ? fullBright : fullDark);
                }
            }
        }
        if ((mListView != null) && (mAdapter != null)) {
            mListView.setAdapter(mAdapter);
            if (mCheckedItem > -1) {
                mListView.setItemChecked(mCheckedItem, true);
                mListView.setSelection(mCheckedItem);
            }
        }
    }

    public static class RecycleListView extends ListView {
        boolean mRecycleOnMeasure = true;

        public RecycleListView(Context context) {
            super(context);
        }

        public RecycleListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RecycleListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        protected boolean recycleOnMeasure() {
            return mRecycleOnMeasure;
        }
    }

    public static class AlertParams {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public int mIconId = 0;
        public Drawable mIcon;
        public CharSequence mTitle;
        public View mCustomTitleView;
        public CharSequence mMessage;
        public CharSequence mPositiveButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public boolean mCancelable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public CharSequence[] mItems;
        public ListAdapter mAdapter;
        public DialogInterface.OnClickListener mOnClickListener;
        public View mView;
        public int mViewSpacingLeft;
        public int mViewSpacingTop;
        public int mViewSpacingRight;
        public int mViewSpacingBottom;
        public boolean mViewSpacingSpecified = false;
        public boolean[] mCheckedItems;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public int mCheckedItem = -1;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public Cursor mCursor;
        public String mLabelColumn;
        public String mIsCheckedColumn;
        public boolean mForceInverseBackground;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public boolean mRecycleOnMeasure = true;
        public boolean mAutoDismiss = true;
        public MLAlertDialog.DismissCallBack mDismissCallBack;
        public CharSequence mCustomTitle;
        public boolean mCustomBgTransplant = false;

        /**
         * Interface definition for a callback to be invoked before the ListView
         * will be bound to an adapter.
         */
        public interface OnPrepareListViewListener {

            /**
             * Called before the ListView is bound to an adapter.
             * 
             * @param listView The ListView that will be shown in the dialog.
             */
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void apply(MLAlertController dialog) {
            if (mCustomTitleView != null) {
                dialog.setCustomTitle(mCustomTitleView);
            } else {
                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                if (mIcon != null) {
                    dialog.setIcon(mIcon);
                }
                if (mIconId >= 0) {
                    dialog.setIcon(mIconId);
                }
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mPositiveButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveButtonText,
                        mPositiveButtonListener, null);
            }
            if (mNegativeButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText,
                        mNegativeButtonListener, null);
            }
            if (mNeutralButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText,
                        mNeutralButtonListener, null);
            }
            if (mForceInverseBackground) {
                dialog.setInverseBackgroundForced(true);
            }
            // For a list, the client can either supply an array of items or an
            // adapter or a cursor
            dialog.mTransplantBg = false;
            if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
                if (dialog.mGravity == Gravity.CENTER) {
                    createCenterListView(dialog);
                } else {
                    createListView(dialog);
                }

            }
            if (mView != null) {
                if (mViewSpacingSpecified) {
                    dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight,
                            mViewSpacingBottom);
                } else {
                    dialog.setView(mView);
                }
            }
            dialog.setAudoDismiss(mAutoDismiss);
            dialog.setCustomTransplant(mCustomBgTransplant);
        }

        private void createCenterListView(final MLAlertController dialog) {
            final LinearLayout customView = (LinearLayout)
                    mInflater.inflate(dialog.mListLayoutWithTitle, null);
            final RecycleListView listView = (RecycleListView) customView
                    .findViewById(R.id.select_dialog_listview);
            ListAdapter adapter;
            int layout = R.layout.ml_center_item;
            if (mCursor == null) {
                adapter = (mAdapter != null) ? mAdapter
                        : new ArrayAdapter<CharSequence>(mContext, layout, R.id.text1, mItems);
            } else {
                adapter = new SimpleCursorAdapter(mContext, layout,
                        mCursor, new String[] {
                                mLabelColumn
                        }, new int[] {
                                R.id.text1
                        });
            }

            if (mCustomTitle != null) {
                ((TextView) (customView.findViewById(R.id.title))).setText(mCustomTitle);
            }

            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(listView);
            }

            /*
             * Don't directly set the adapter on the ListView as we might want
             * to add a footer to the ListView later.
             */
            dialog.mAdapter = adapter;
            listView.setAdapter(adapter);
            dialog.mCheckedItem = mCheckedItem;

            if (mOnClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        mOnClickListener.onClick(dialog.mDialogInterface, position);
                        if (!mIsSingleChoice) {
                            dialog.mDialogInterface.dismiss();
                        }
                    }
                });
            } else if (mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        if (mCheckedItems != null) {
                            mCheckedItems[position] = listView.isItemChecked(position);
                        }
                        mOnCheckboxClickListener.onClick(
                                dialog.mDialogInterface, position, listView.isItemChecked(position));
                    }
                });
            }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mIsSingleChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            } else if (mIsMultiChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }

            listView.mRecycleOnMeasure = mRecycleOnMeasure;
            dialog.mView = customView;
            dialog.mTransplantBg = true;
            dialog.setCustomTransplant(mCustomBgTransplant);
        }

        private void createListView(final MLAlertController dialog) {
            final RecycleListView listView = (RecycleListView)
                    mInflater.inflate(dialog.mListLayout, null);
            ListAdapter adapter;

            if (mIsMultiChoice) {
                if (mCursor == null) {
                    adapter = new ArrayAdapter<CharSequence>(
                            mContext, dialog.mMultiChoiceItemLayout, R.id.text1, mItems) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (mCheckedItems != null) {
                                boolean isItemChecked = mCheckedItems[position];
                                if (isItemChecked) {
                                    listView.setItemChecked(position, true);
                                }
                            }
                            return view;
                        }
                    };
                } else {
                    adapter = new CursorAdapter(mContext, mCursor, false) {
                        private final int mLabelIndex;
                        private final int mIsCheckedIndex;

                        {
                            final Cursor cursor = getCursor();
                            mLabelIndex = cursor.getColumnIndexOrThrow(mLabelColumn);
                            mIsCheckedIndex = cursor.getColumnIndexOrThrow(mIsCheckedColumn);
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            CheckedTextView text = (CheckedTextView) view.findViewById(R.id.text1);
                            text.setText(cursor.getString(mLabelIndex));
                            listView.setItemChecked(cursor.getPosition(),
                                    cursor.getInt(mIsCheckedIndex) == 1);
                        }

                        @Override
                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            return mInflater.inflate(dialog.mMultiChoiceItemLayout,
                                    parent, false);
                        }

                    };
                }
            } else {
                int layout = mIsSingleChoice
                        ? dialog.mSingleChoiceItemLayout : dialog.mListItemLayout;
                if (mCursor == null) {
                    adapter = (mAdapter != null) ? mAdapter
                            : new ArrayAdapter<CharSequence>(mContext, layout, R.id.text1, mItems);
                } else {
                    adapter = new SimpleCursorAdapter(mContext, layout,
                            mCursor, new String[] {
                                    mLabelColumn
                            }, new int[] {
                                    R.id.text1
                            });
                }
            }

            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(listView);
            }

            /*
             * Don't directly set the adapter on the ListView as we might want
             * to add a footer to the ListView later.
             */
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;

            if (mOnClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        mOnClickListener.onClick(dialog.mDialogInterface, position);
                        if (!mIsSingleChoice) {
                            dialog.mDialogInterface.dismiss();
                        }
                    }
                });
            } else if (mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        if (mCheckedItems != null) {
                            mCheckedItems[position] = listView.isItemChecked(position);
                        }
                        mOnCheckboxClickListener.onClick(
                                dialog.mDialogInterface, position, listView.isItemChecked(position));
                    }
                });
            }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mIsSingleChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            } else if (mIsMultiChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            listView.mRecycleOnMeasure = mRecycleOnMeasure;
            dialog.mListView = listView;
            dialog.setCustomTransplant(mCustomBgTransplant);
        }
    }

}

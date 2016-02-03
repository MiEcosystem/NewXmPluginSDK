package com.xiaomi.smarthome.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomi.common.R;
import com.xiaomi.smarthome.common.plug.utils.TitleBarUtil;
import com.xiaomi.smarthome.device.api.XmPluginCommonApi;

/**
 * Created by livy on 15/10/22.
 */
public class MenuDialog extends Dialog {
    CharSequence[] mItems;
    OnClickListener onClickListener;
    ListView mListView;
    BaseAdapter mListViewAdapter;
    LayoutInflater mLayoutInflater;
    int mBackgroundColor = -1;
    View mRootView;

    public MenuDialog(Context context) {
        super(context,R.style.V5_MenuDialog);
    }

    //设置默认样式
    public void setItems(CharSequence[] items, OnClickListener listener) {
        this.mItems = items;
        this.onClickListener = listener;
    }
    //设置背景颜色
    public void setBackGroundColor(int color){
        mBackgroundColor = color;
    }
    //设置可定制样式
    public void setMenuAdapter(BaseAdapter adapter){
        mListViewAdapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setGravity(Gravity.TOP);
        TitleBarUtil.enableWhiteTranslucentStatus(getWindow());
        try {
            XmPluginCommonApi.instance().setMenuDialogWindowAnimations(getWindow());
        } catch (Throwable e) {
        }
        
        mLayoutInflater = LayoutInflater.from(getContext());
        mRootView = mLayoutInflater.inflate(R.layout.menu_dialog, null);
        getWindow().setContentView(mRootView);
        TitleBarUtil.setTitleBarPadding(getContext().getResources().getDimensionPixelSize(R.dimen.title_bar_top_padding), mRootView);
        if(mBackgroundColor>0){
            mRootView.setBackgroundColor(mBackgroundColor);
        }
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        mListView = (ListView) findViewById(R.id.select_dialog_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onClickListener!=null){
                    dismiss();
                    onClickListener.onClick(null,i);
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mListViewAdapter == null && mItems != null) {
            mListViewAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return mItems.length;
                }

                @Override
                public Object getItem(int i) {
                    return mItems[i];
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                    }
                    TextView textView = (TextView) view.findViewById(R.id.text1);
                    textView.setText(mItems[i]);
                    return view;
                }
            };
        }

        if(mListViewAdapter!=null)
            mListView.setAdapter(mListViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

}

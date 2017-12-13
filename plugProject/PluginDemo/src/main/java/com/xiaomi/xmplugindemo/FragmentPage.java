
package com.xiaomi.xmplugindemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.BaseFragment;

public class FragmentPage extends BaseFragment {
    TextView infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoView = (TextView) view.findViewById(R.id.info);
        infoView.setText(xmPluginActivity().getDeviceStat().name);
        view.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(xmPluginActivity().activity(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        view.findViewById(R.id.share).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, null);
    }
}

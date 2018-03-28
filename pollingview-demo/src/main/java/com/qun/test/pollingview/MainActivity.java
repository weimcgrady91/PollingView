package com.qun.test.pollingview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.qun.lib.pollingview.PollingBean;
import com.qun.lib.pollingview.PollingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private List<PollingBean> obtainData() {
        List<PollingBean> data = new ArrayList<>();
        int[] ids = new int[]{
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e
        };
        String[] title = new String[]{
                "巩俐不低俗,我就不低俗",
                "朴树又回来了",
                "东风吹过春满地",
                "乐视网TV版大放送",
                "热血屌丝的反击"
        };
        for (int i = 0; i < ids.length; i++) {
            PollingBean pollingBean = new PollingBean();
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            pollingBean.imageView = imageView;
            pollingBean.title = title[i];
            data.add(pollingBean);
        }
        return data;
    }

    private void initViews() {
        PollingView pollingView = findViewById(R.id.polling);
        pollingView.setData(obtainData());
    }
}

package com.qun.lib.pollingview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class PollingView extends LinearLayout {
    private List<PollingBean> data = new ArrayList<>();
    private PollingViewAdapter mAdapter;
    private Context mContext;
    private LinearLayout mLlDotContainer;
    private TextView mTvTitle;
    private ViewPager mViewPager;
    private List<View> mDots;
    private int mDotSize;
    private boolean mPollingFlag;

    public PollingView(Context context) {
        this(context, null);
    }

    public PollingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PollingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pollingview_view, this, true);
        mViewPager = view.findViewById(R.id.vp);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mAdapter = new PollingViewAdapter();
        mViewPager.setAdapter(mAdapter);
        mLlDotContainer = view.findViewById(R.id.ll_dot_container);
        mTvTitle = view.findViewById(R.id.tv_title);
    }

    public void setData(List<PollingBean> data) {
        this.data = data;
        mAdapter.notifyDataSetChanged();
        initDots();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    };

    public void startPolling() {
        mPollingFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mPollingFlag) {
                    try {
                        Thread.sleep(2000);
                        mHandler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopPolling() {
        mPollingFlag = false;
    }
    private void initDots() {
        mDotSize = data.size() >= 5 ? 5 : data.size();
        int dotWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
        mDots = new ArrayList<>();
        for (int i = 0; i < mDotSize; i++) {
            View dot = new View(mContext);
            dot.setEnabled(false);
            dot.setBackgroundResource(R.drawable.selector_dot);
            mDots.add(dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotWidth, dotWidth);
            if (i != 0) {
                params.leftMargin = 10;
            }
            mLlDotContainer.addView(dot, params);
        }
        selectDot(0);
        selectTitle(0);
    }

    private void selectDot(int index) {
        for (int i = 0; i < mDots.size(); i++) {
            if (i == index) {
                mDots.get(i).setEnabled(true);
            } else {
                mDots.get(i).setEnabled(false);
            }
        }
    }

    private void selectTitle(int index) {
        mTvTitle.setText(data.get(index).title);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            selectDot(position % 5);
            selectTitle(position % 5);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class PollingViewAdapter extends PagerAdapter {
        public PollingViewAdapter() {
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = data.get(position % 5).imageView;
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPolling();
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
    }
}

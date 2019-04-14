package com.cgg.android.tablayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgg.easyTabLayout.R;

import java.util.List;

import androidx.annotation.Nullable;

public class EasyTabLayout extends LinearLayout {
    private final static String TAG = EasyTabLayout.class.getSimpleName();
    public final static String UNSELECT_TEXT_COLOR = "#666666";
    public final static String SELECT_TEXT_COLOR = "#FF0000";
    private int mAutoAddId = -1;
    private int mSelectedId = -1;
    private int mUnSelectTextColor = Color.parseColor(UNSELECT_TEXT_COLOR);
    private int mSelectTextColor = Color.parseColor(SELECT_TEXT_COLOR);
    private TabListener mTabListener;
    private ImageListener mImageListener;
    private View mTopLine;
    private LinearLayout mTabContainer;

    public interface TabListener {
        void onTabSelected(int selectedId);

        void onTabReSelected(int selectedId);
    }

    public interface ImageListener {
        void setImageUrl(ImageView imageView, String url, int iconRes);
    }

    public EasyTabLayout(Context context) {
        super(context);
    }

    public EasyTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setClipChildren(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.easy_tablayout, this);
        mTopLine = view.findViewById(R.id.easy_tablayout_top_line);
        mTabContainer = view.findViewById(R.id.easy_tablayout_tab_container);
    }

    public void setTabListener(TabListener tabListener) {
        mTabListener = tabListener;
    }

    public void setImageListener(ImageListener imageListener) {
        mImageListener = imageListener;
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (height > -1) {
            layoutParams.height = UnitUtil.dp2px(getContext(), height);
        } else if (height == -2 || height == -1) {
            layoutParams.height = height;
        }
        setLayoutParams(layoutParams);
    }

    public void setTopLineHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mTopLine.getLayoutParams();
        layoutParams.height = UnitUtil.dp2px(getContext(), height);
        mTopLine.setLayoutParams(layoutParams);
    }

    public void setTopLineColor(int color) {
        mTopLine.setBackgroundColor(color);
    }

    public void setTextColorStr(String unSelectTextColorStr, String selectTextColorStr) {
        try {
            int unSelectTextColor = Color.parseColor(unSelectTextColorStr);
            int selectTextColor = Color.parseColor(selectTextColorStr);
            mUnSelectTextColor = unSelectTextColor;
            mSelectTextColor = selectTextColor;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTextColor(int unSelectTextColor, int selectTextColor) {
        mUnSelectTextColor = unSelectTextColor;
        mSelectTextColor = selectTextColor;
    }

    public void init(List<Tab> tabList) {
        init(tabList, 0);
    }

    public void init(List<Tab> tabList, int selectedId) {
        if (tabList == null || tabList.size() == 0) {
            return;
        }
        for (int i = 0; i < tabList.size(); i++) {
            Tab tab = tabList.get(i);
            insertTabView(tab, i);
        }
        selectTab(selectedId);
    }

    private void insertTabView(Tab tab, int index) {
        if (tab == null) {
            return;
        }
        if (tab.id == -1) {
            tab.id = ++mAutoAddId;
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.easy_tablayout_item, this, false);
        view.setTag(tab);
        updateTabViewState(view, tab.title, mUnSelectTextColor, tab.unSelectedUrl, tab.unSelectedIcon);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab tab = (Tab) view.getTag();
                selectTab(tab.id);
            }
        });
        int childCount = mTabContainer.getChildCount();
        if (index < 0) {
            mTabContainer.addView(view, 0);
        } else if (index >= childCount) {
            mTabContainer.addView(view);
        } else {
            mTabContainer.addView(view, index);
        }
    }

    public void selectTab(int selectedId) {
        Log.i(TAG, "selectTab() selectedId=" + selectedId);
        int childCount = mTabContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mTabContainer.getChildAt(i);
            Tab tab = (Tab) view.getTag();
            if (tab.id == selectedId) {
                updateTabViewState(view, tab.title, mSelectTextColor, tab.selectedUrl, tab.selectedIcon);
            } else {
                updateTabViewState(view, tab.title, mUnSelectTextColor, tab.unSelectedUrl, tab.unSelectedIcon);
            }
        }
        notifyTabSelected(selectedId);
    }

    private void notifyTabSelected(int selectedId) {
        if (mTabListener != null) {
            if (mSelectedId == selectedId) {
                mTabListener.onTabReSelected(selectedId);
            } else {
                mTabListener.onTabSelected(selectedId);
            }
        }
        mSelectedId = selectedId;
    }

    private void updateTabViewState(View view, String title, int textColor, String url, int defaultIconRes) {
        if (view == null) {
            return;
        }
        Log.i(TAG, "updateTabViewState() " + view + " title:" + title + " textColor:" + textColor + " url:" + url + " defaultIconRes" + defaultIconRes);
        ImageView tabIcon = view.findViewById(R.id.tab_icon);
        TextView tabTitle = view.findViewById(R.id.tab_title);
        if (!TextUtils.isEmpty(title)) {
            tabTitle.setVisibility(View.VISIBLE);
            tabTitle.setText(title);
            tabTitle.setTextColor(textColor);
        } else {
            tabTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(url)) {
            tabIcon.setVisibility(View.VISIBLE);
            if (mImageListener != null) {
                mImageListener.setImageUrl(tabIcon, url, defaultIconRes);
            }
        } else if (defaultIconRes != 0) {
            tabIcon.setVisibility(View.VISIBLE);
            tabIcon.setImageResource(defaultIconRes);
        } else {
            tabIcon.setVisibility(View.GONE);
        }
    }

    public void addTab(Tab tab) {
        int childCount = mTabContainer.getChildCount();
        insertTabView(tab, childCount);
    }

    public void insertTab(Tab tab, int index) {
        insertTabView(tab, index);
    }

    public void removeTab(int id) {
        int index = getTabIndex(id);
        if (index != -1) {
            removeViewAt(index);
        }
    }

    public int getTabIndex(int id) {
        int childCount = mTabContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mTabContainer.getChildAt(i);
            if (view != null) {
                Tab tab = (Tab) view.getTag();
                if (tab != null && tab.id == id) {
                    return i;
                }
            }
        }
        return -1;
    }

    public View getTabView(int id) {
        int index = getTabIndex(id);
        if (index != -1) {
            return mTabContainer.getChildAt(index);
        }
        return null;
    }

    public Tab getTab(int id) {
        View view = getTabView(id);
        if (view != null) {
            return (Tab) view.getTag();
        }
        return null;
    }

    public void setSuperScriptMargin(int id, float marginTopDp, float marginRightDp) {
        View view = getTabView(id);
        if (view != null) {
            LinearLayout linearLayout = view.findViewById(R.id.tab_super_script);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.topMargin = UnitUtil.dp2px(getContext(), marginTopDp);
            layoutParams.rightMargin = UnitUtil.dp2px(getContext(), marginRightDp);
            linearLayout.setLayoutParams(layoutParams);
        }
    }

    public void setPointSize(int id, int sizeDp) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_point);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.height = UnitUtil.dp2px(getContext(), sizeDp);
            layoutParams.width = UnitUtil.dp2px(getContext(), sizeDp);
            textView.setLayoutParams(layoutParams);
        }
    }

    public void setPointColor(int id, int color) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_point);
            GradientDrawable drawable = (GradientDrawable) textView.getBackground();
            drawable.setColor(color);
        }
    }

    public void showPoint(int id) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_point);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void hidePoint(int id) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_point);
            textView.setVisibility(View.GONE);
        }
    }

    public void setMsgSize(int id, int sizeDp) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_msg);
            textView.setTextSize(sizeDp);
        }
    }

    public void setMsgColor(int id, int color) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_msg);
            textView.setTextColor(color);
        }
    }

    public void setMsgLeftMargin(int id, float marginLeftDp) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_msg);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.leftMargin = UnitUtil.dp2px(getContext(), marginLeftDp);
            textView.setLayoutParams(layoutParams);
        }
    }

    public void showMsg(int id, String msg) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_msg);
            textView.setVisibility(View.VISIBLE);
            textView.setText(msg);
        }
    }

    public void hideMsg(int id) {
        View view = getTabView(id);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tab_msg);
            textView.setVisibility(View.GONE);
        }
    }
}

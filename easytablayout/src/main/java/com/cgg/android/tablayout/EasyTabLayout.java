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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cgg.easyTabLayout.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public class EasyTabLayout extends LinearLayout {
    private final static String TAG = EasyTabLayout.class.getSimpleName();
    public final static String UNSELECT_TEXT_COLOR = "#666666";
    public final static String SELECT_TEXT_COLOR = "#FF0000";
    private int mAutoAddId = -1;
    private int mSelectedId = -1;
    private int mUnSelectTextColor = Color.parseColor(UNSELECT_TEXT_COLOR);
    private int mSelectTextColor = Color.parseColor(SELECT_TEXT_COLOR);
    private int mLayoutRes = R.layout.layout_bottom_tablayout;
    private List<TabListener> mTabListeners = new ArrayList<>();
    private ImageListener mImageListener;
    private LinearLayout mTabContainer;

    private int mBackgroundColor;
    private int mDividerLineHeight = 1;
    private int mDividerLineColor;
    private int mItemPaddingLeft;
    private int mItemPaddingTop;
    private int mItemPaddingRight;
    private int mItemPaddingBottom;


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
    }

    public void addTabListener(TabListener tabListener) {
        if (tabListener == null) {
            return;
        }
        mTabListeners.add(tabListener);
    }

    public void setImageListener(ImageListener imageListener) {
        mImageListener = imageListener;
    }

    public void configLayoutRes(@LayoutRes int layoutRes) {
        mLayoutRes = layoutRes;
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

    public void configDividerLineColor(String colorStr) {
        try {
            mDividerLineColor = Color.parseColor(colorStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configDividerLineColor(int color) {
        mDividerLineColor = color;
    }

    public void configDividerLineHeight(int height) {
        mDividerLineHeight = height;
    }

    public void setHeight(View view, int height) {
        if (view == null || view.getLayoutParams() == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = UnitUtil.dp2px(getContext(), height);
        view.setLayoutParams(layoutParams);
    }

    public void setColor(View view, int color) {
        if (view == null) {
            return;
        }
        view.setBackgroundColor(color);
    }

    public void configTextColorStr(String unSelectTextColorStr, String selectTextColorStr) {
        try {
            mUnSelectTextColor = Color.parseColor(unSelectTextColorStr);
            mSelectTextColor = Color.parseColor(selectTextColorStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configTextColor(int unSelectTextColor, int selectTextColor) {
        mUnSelectTextColor = unSelectTextColor;
        mSelectTextColor = selectTextColor;
    }

    public void configBackgroundColor(String backgroundColorStr) {
        try {
            mBackgroundColor = Color.parseColor(backgroundColorStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void configItemPadding(int leftDp, int topDp, int rightDp, int bottomDp) {
        mItemPaddingLeft = UnitUtil.dp2px(getContext(), leftDp);
        mItemPaddingTop = UnitUtil.dp2px(getContext(), topDp);
        mItemPaddingRight = UnitUtil.dp2px(getContext(), rightDp);
        mItemPaddingBottom = UnitUtil.dp2px(getContext(), bottomDp);
    }

    public void init(List<Tab> tabList, int selectedId) {
        if (mLayoutRes == 0) {
            Log.e(TAG, "you has not set LayoutRes.");
            return;
        }
        View view = LayoutInflater.from(getContext()).inflate(mLayoutRes, this);
        if (view == null) {
            return;
        }
        view.setBackgroundColor(mBackgroundColor);
        View dividerLine = view.findViewById(R.id.easy_tablayout_divider_line);
        if (dividerLine != null) {
            setHeight(dividerLine, mDividerLineHeight);
            if (mDividerLineColor != 0) {
                dividerLine.setBackgroundColor(mDividerLineColor);
            }
        }
        mTabContainer = view.findViewById(R.id.easy_tablayout_container);
        if (mTabContainer == null || tabList == null || tabList.size() == 0) {
            return;
        }
        for (int i = 0; i < tabList.size(); i++) {
            Tab tab = tabList.get(i);
            insertTabView(tab, i);
        }
        selectTab(selectedId);
    }

    private void insertTabView(Tab tab, int index) {
        if (mTabContainer == null || tab == null) {
            return;
        }
        if (tab.id == -1) {
            tab.id = ++mAutoAddId;
        }
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.easy_tablayout_item, this, false);
        itemView.setTag(tab);
        updateTabViewState(itemView, tab.title, mUnSelectTextColor, tab.unSelectedUrl, tab.unSelectedIcon);
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab tab = (Tab) view.getTag();
                selectTab(tab.id);
            }
        });
        int childCount = mTabContainer.getChildCount();
        if (index < 0) {
            mTabContainer.addView(itemView, 0);
        } else if (index >= childCount) {
            mTabContainer.addView(itemView);
        } else {
            mTabContainer.addView(itemView, index);
        }
        itemView.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);
        setLayoutParams(itemView);
    }

    private void setLayoutParams(LinearLayout itemView) {
        Log.i(TAG, "setLayoutParams() getParent()=" + getParent());
        LinearLayout.LayoutParams layoutParams;
        ViewGroup parent = (ViewGroup) mTabContainer.getParent();
        if (parent instanceof HorizontalScrollView || parent instanceof ScrollView) {
            layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            //            if (mTabContainer.getOrientation() == LinearLayout.VERTICAL) {
            //                int height = getHeight() < parent.getHeight() ? parent.getHeight() : getHeight();
            //                Log.i(TAG, "setLayoutParams() height=" + height);
            //                layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
            //            } else {
            //                int width = getWidth() < parent.getWidth() ? parent.getWidth() : getWidth();
            //                Log.i(TAG, "setLayoutParams() width=" + width);
            //                layoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            //            }
        } else {
            if (mTabContainer.getOrientation() == LinearLayout.VERTICAL) {
                layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 1.0f);
            } else {
                layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            }
        }
        itemView.setLayoutParams(layoutParams);
    }

    private void updateTabViewState(View view, String title, int textColor, String url, int defaultIconRes) {
        if (view == null) {
            return;
        }
        Log.i(TAG, "updateTabViewState() " + view + " title:" + title + " textColor:" + textColor + " url:" + url + " defaultIconRes" + defaultIconRes);
        RelativeLayout tabIconContainer = view.findViewById(R.id.tab_icon_container);
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
            tabIconContainer.setVisibility(View.VISIBLE);
            if (mImageListener != null) {
                mImageListener.setImageUrl(tabIcon, url, defaultIconRes);
            }
        } else if (defaultIconRes != 0) {
            tabIconContainer.setVisibility(View.VISIBLE);
            tabIcon.setImageResource(defaultIconRes);
        } else {
            tabIconContainer.setVisibility(View.GONE);
        }
    }

    public void selectTab(int selectedId) {
        if (mTabContainer == null) {
            return;
        }
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
        if (mTabListeners != null) {
            for (TabListener tabListener : mTabListeners) {
                if (tabListener == null) {
                    return;
                }
                if (mSelectedId == selectedId) {
                    tabListener.onTabReSelected(selectedId);
                } else {
                    tabListener.onTabSelected(selectedId);
                }
            }
        }
        mSelectedId = selectedId;
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
        if (mTabContainer == null) {
            return -1;
        }
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

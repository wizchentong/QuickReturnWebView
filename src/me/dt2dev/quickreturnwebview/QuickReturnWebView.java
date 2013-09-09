package me.dt2dev.quickreturnwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nobu_games.android.view.web.TitleBarWebView;

public class QuickReturnWebView extends TitleBarWebView {

	private int mTitleBarHeight;
	private int mMinTranslationY;
	private int mMaxTranslationY;

	private boolean isActionUpOrCancel = false;

	private View mTitleBar;

	public QuickReturnWebView(Context context) {
		super(context);
		init();
	}

	public QuickReturnWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuickReturnWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mTitleBarHeight = 0;
		mMaxTranslationY = 0;
	}

	public void setTitleBar(View titleBar) {
		this.mTitleBar = titleBar;
		this.mTitleBar.post(new Runnable() {

			@Override
			public void run() {
				mTitleBarHeight = mTitleBar.getHeight();
				mMinTranslationY = -1 * mTitleBarHeight;
			}
		});
	}

	/**
	 * Use for removing the padding part when get WebView preview and so on.
	 */
	public int getPaddingOffset() {
		final int offset = mTitleBarHeight - getScrollY();
		return offset > 0 ? offset : 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			isActionUpOrCancel = false;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isActionUpOrCancel = true;
			onActionUpOrCancel();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void onActionUpOrCancel() {
		if (null == mTitleBar)
			return;
		final int oldTY = (int) ViewHelper.getTranslationY(mTitleBar);
		if (oldTY == mMinTranslationY || oldTY == mMaxTranslationY)
			return;
		final int scrollY = getScrollY();
		if (scrollY < 0) {
			ViewHelper.setTranslationY(mTitleBar, mMaxTranslationY);
			return;
		}
		if (oldTY * 2 > mMinTranslationY || scrollY < mTitleBarHeight)
			startAnimation(mTitleBar, mMaxTranslationY);
		else
			startAnimation(mTitleBar, mMinTranslationY);
	}

	private void startAnimation(View view, int translationY) {
		ViewPropertyAnimator.animate(view).cancel();
		ViewPropertyAnimator.animate(view).translationY(translationY);
	}

	/**
	 * offset > 0: Page Down; offset < 0: Page Up
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (null == mTitleBar)
			return;
		ViewPropertyAnimator.animate(mTitleBar).cancel();
		if (isActionUpOrCancel)
			onActionUpOrCancel();
		final int offset = t - oldt;
		if (t <= mTitleBarHeight && offset > 0) {
			if (t > 0)
				ViewHelper.setTranslationY(mTitleBar, -t);
			return;
		}
		final int oldTY = (int) ViewHelper.getTranslationY(mTitleBar);
		int newTY = oldTY - offset;
		if (offset > 0) {
			if (oldTY == mMinTranslationY)
				return;
			newTY = Math.max(newTY, mMinTranslationY);
			ViewHelper.setTranslationY(mTitleBar, newTY);
		} else if (offset < 0) {
			if (oldTY == mMaxTranslationY)
				return;
			newTY = Math.min(newTY, mMaxTranslationY);
			ViewHelper.setTranslationY(mTitleBar, newTY);
		}
	}

}

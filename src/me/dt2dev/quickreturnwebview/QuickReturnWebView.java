package me.dt2dev.quickreturnwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.nineoldandroids.view.ViewHelper;
import com.nobu_games.android.view.web.TitleBarWebView;

public class QuickReturnWebView extends TitleBarWebView {

	private int mLastMotionY;
	private int mTitleBarHeight;

	private View mTitleBar;

	public QuickReturnWebView(Context context) {
		super(context);
	}

	public QuickReturnWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QuickReturnWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setTitleBar(View titleBar) {
		this.mTitleBar = titleBar;
		this.mTitleBar.post(new Runnable() {

			@Override
			public void run() {
				mTitleBarHeight = mTitleBar.getHeight();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = getScrollY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTitleBar != null)
				onActionMove(mLastMotionY, getScrollY());
			mLastMotionY = getScrollY();
			break;
		case MotionEvent.ACTION_UP:
			if (mTitleBar != null)
				onActionUp(getScrollY());
			break;
		}
		return super.onTouchEvent(event);
	}

	public void onActionMove(int oldScrollY, int newScrollY) {
		final int offset = newScrollY - oldScrollY;
		if (newScrollY <= mTitleBarHeight && offset > 0) {
			ViewHelper.setTranslationY(mTitleBar, -newScrollY);
			return;
		}
		final int oldTranslationY = (int) ViewHelper.getTranslationY(mTitleBar);
		if (offset > 0) {
			if (oldTranslationY == -mTitleBarHeight)
				return;
			ViewHelper.setTranslationY(mTitleBar,
					-Math.min(offset - oldTranslationY, mTitleBarHeight));
		} else {
			if (oldTranslationY == 0)
				return;
			ViewHelper.setTranslationY(mTitleBar,
					Math.min(oldTranslationY - offset, 0));
		}
	}

	public void onActionUp(int scrollY) {
		Animation animation;
		final int translationY = (int) ViewHelper.getTranslationY(mTitleBar);
		if (-translationY * 2 < mTitleBarHeight) {
			ViewHelper.setTranslationY(mTitleBar, 0);
			animation = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE, translationY,
					Animation.ABSOLUTE, 0);
		} else {
			ViewHelper.setTranslationY(mTitleBar, -mTitleBarHeight);
			animation = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE, translationY,
					Animation.ABSOLUTE, -mTitleBarHeight);
			if (scrollY < mTitleBarHeight)
				scrollTo(0, mTitleBarHeight);
		}
		animation.setDuration(300);
		animation.setFillAfter(false);
		mTitleBar.startAnimation(animation);
	}
}

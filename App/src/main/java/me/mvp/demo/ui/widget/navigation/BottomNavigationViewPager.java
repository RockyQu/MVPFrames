package me.mvp.demo.ui.widget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 *
 */
public class BottomNavigationViewPager extends ViewPager {

	private boolean enabled;

	public BottomNavigationViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	/**
	 * Enable or disable the swipe navigation
	 * @param enabled
	 */
	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
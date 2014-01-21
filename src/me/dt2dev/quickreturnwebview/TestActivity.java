package me.dt2dev.quickreturnwebview;

import com.czt.webview.QuickReturnWithBottomBarWebView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestActivity extends Activity {

	private QuickReturnWithBottomBarWebView mWebView;
	private View mTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		findViews();
	}

	private void findViews() {
		mTitleBar = findViewById(R.id.titlebar);

		mWebView = (QuickReturnWithBottomBarWebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mWebView.disableScrollTitle();
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//尝试的代码，可以设置回去，但由于是先设置过来再设置回去，会出现屏幕一闪的情况。 
//				mWebView.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						mWebView.scrollTo(0, 0);						
//					}
//				}, 100);
				
				
			}
		});
		mWebView.loadUrl("http://v2ex.com");
		mWebView.setEmbeddedTitleBarCompat(getLayoutInflater().inflate(
				R.layout.header, null));
		mWebView.setTitleBar(mTitleBar);
		mWebView.setBottomBar(findViewById(R.id.bottom_bar));
	}

}

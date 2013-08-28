package me.dt2dev.quickreturnwebview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

public class TestActivity extends Activity {

	private QuickReturnWebView mWebView;
	private View mTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		findViews();
	}

	private void findViews() {
		mTitleBar = findViewById(R.id.titlebar);

		mWebView = (QuickReturnWebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.loadUrl("http://v2ex.com");
		mWebView.setEmbeddedTitleBarCompat(getLayoutInflater().inflate(
				R.layout.header, null));
		mWebView.setTitleBar(mTitleBar);
	}

}

package com.example.myquizapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.webkit.*;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging; // Firebase uchun qo'shildi

public class MainActivity extends Activity {

    private ValueCallback<Uri[]> filePathCallback;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // --- FIREBASE TOKEN OLISH (Bildirishnoma uchun) ---
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				String token = task.getResult();
				Log.d("FCM_TOKEN", "Token: " + token);
			}
		});

        final WebView wv = new WebView(this);
        WebSettings settings = wv.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        wv.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url == null) return false;
					if (url.startsWith("tg:") || url.contains("t.me") || url.startsWith("intent:")) {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							startActivity(intent);
							return true;
						} catch (Exception e) {
							try {
								if(url.startsWith("tg://")) {
									String webUrl = url.replace("tg://", "https://t.me/");
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
									startActivity(intent);
									return true;
								}
							} catch (Exception ex) {}
						}
					}
					return false;
				}

				@Override
				public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
					view.loadUrl("file:///android_asset/offline.html");
				}
			});

        wv.setWebChromeClient(new WebChromeClient() {
				@Override
				public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
					if (MainActivity.this.filePathCallback != null) {
						MainActivity.this.filePathCallback.onReceiveValue(null);
					}
					MainActivity.this.filePathCallback = filePathCallback;

					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Chekni tanlang"), FILECHOOSER_RESULTCODE);
					return true;
				}
			});

        wv.loadUrl("https://rahmonovssss.github.io/Sardorbekkk/");
        setContentView(wv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (filePathCallback == null) return;
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                results = new Uri[]{Uri.parse(data.getDataString())};
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

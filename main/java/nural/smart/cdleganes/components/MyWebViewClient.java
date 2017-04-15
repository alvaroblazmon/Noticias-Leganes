package nural.smart.cdleganes.components;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by alvaro on 15/4/17.
 */

public class MyWebViewClient extends WebViewClient {

    private ProgressBar progressBar;

    public MyWebViewClient(ProgressBar progressBar) {
        this.progressBar=progressBar;
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (progressBar != null) { progressBar.setVisibility(View.GONE); }
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (progressBar != null) { progressBar.setVisibility(View.VISIBLE); }
        super.onPageStarted(view, url, favicon);
    }
}

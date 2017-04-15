package nural.smart.cdleganes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import nural.smart.cdleganes.components.MyWebViewClient;

public class EntryDetailActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        String title = this.getIntent().getExtras().getString("title");
        String url = this.getIntent().getExtras().getString("url");

        setTitle(title);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarDetail);
        final WebView mWebView = (WebView) findViewById(R.id.detail_web_view);
        mWebView.setWebViewClient(new MyWebViewClient(progressBar));

        mWebView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProviderm
        final ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                String url = this.getIntent().getExtras().getString("url");

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Noticias CD Leganés: " + url);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, url);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

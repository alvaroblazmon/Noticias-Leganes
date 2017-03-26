package nural.smart.cdleganes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class EntryDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        String title = this.getIntent().getExtras().getString("title");
        String url = this.getIntent().getExtras().getString("url");
        //url = "https://www.youtube.com/results?q=resumenes+cd+leganes+highlights+laliga+santander+suscribete+al+canal+oficial+de&sp=CAI%253D";
        //url = "https://www.youtube.com/playlist?list=PLBTaSXagq1CAzf9wwFWJt-_7px3twGT9s";

        setTitle(title);
        mWebView = (WebView) findViewById(R.id.detail_web_view);
        //mWebView.setWebChromeClient(new WebChromeClient());
        //mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProviderm
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Noticias CD Leganés: " +this.getIntent().getExtras().getString("url") );
                sendIntent.setType("text/plain");
                //startActivity(sendIntent);
                //setShareIntent(sendIntent);
                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }







}

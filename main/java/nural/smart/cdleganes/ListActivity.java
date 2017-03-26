package nural.smart.cdleganes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import nural.smart.cdleganes.adapter.EntryAdapter;
import nural.smart.cdleganes.data.XMLNetwork;
import nural.smart.cdleganes.data.XMLParser;

/**
 * Created by alvaro on 16/2/17.
 */

public class ListActivity extends AppCompatActivity{

    private ListView mListView;
    final Context context = this;
    private SwipeRefreshLayout swipeView;

    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    private ProgressDialog progress;

    //TODO: Los literales ponerlos es un STRING
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        Log.d("MAIN", "ONCREATE");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Noticias " + getResources().getString(R.string.name));

        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorName));


        mListView = (ListView) findViewById(R.id.recipe_list_view);


        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeView.setRefreshing(true);

                        loadPage(false);
                    }
                }
        );

        loadPage(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_news:

                            case R.id.action_schedules:

                            case R.id.action_rating:

                        }
                        return true;
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MAIN", "ONSTART");
    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        super.onPause();
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {

        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = mListView.onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);


        Log.v("MAIN", "onSaveInstanceState");
    }


    public void loadPage(boolean showProgress) {
        if (isOnline()) {

            if(showProgress){

                //TODO: Hacer solo un spinner http://stackoverflow.com/questions/21751662/create-a-progressdialog-only-with-the-spinner-in-the-middle
                progress = new ProgressDialog(this);
                progress.setMessage("Actualizando noticias...");
                progress.show();
            }

            new ListActivity.DownloadXmlTask().execute();

        } else {
            showError();
        }
    }

    private class DownloadXmlTask extends AsyncTask<Object, Object, List<XMLParser.Entry>> {
        @Override
        protected List<XMLParser.Entry> doInBackground(Object... urls) {
            try {
                List<XMLParser.Entry> entries = XMLNetwork.loadXmlFromNetwork();
                entries = XMLNetwork.orderByDate(entries);
                return entries;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<XMLParser.Entry> result) {

            if(result==null || result.isEmpty()){
                showError();
            } else {
                EntryAdapter adapter = new EntryAdapter(ListActivity.this, result);
                mListView.setAdapter(adapter);
            }

            if(swipeView!=null)
                swipeView.setRefreshing(false);


            if (mListState != null){
                mListView.onRestoreInstanceState(mListState);
            }
            mListState = null;

            progress.dismiss();

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    XMLParser.Entry selectedRecipe = result.get(position);

                    Intent detailIntent = new Intent(context, EntryDetailActivity.class);

                    detailIntent.putExtra("title", selectedRecipe.title);
                    detailIntent.putExtra("url", selectedRecipe.link);

                    startActivityForResult(detailIntent, 0);
                }

            });

        }


    }



    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showError() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("No se han podido descargar las noticias");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Reintentar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadPage(true);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }






}

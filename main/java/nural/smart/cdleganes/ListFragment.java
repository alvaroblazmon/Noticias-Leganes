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
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.List;

import nural.smart.cdleganes.adapter.EntryAdapter;
import nural.smart.cdleganes.data.XMLNetwork;
import nural.smart.cdleganes.data.XMLParser;

/**
 * Created by alvaro on 27/3/17.
 */

public class ListFragment extends Fragment {

    private ListView mListView;
    private SwipeRefreshLayout swipeView;


    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    private ProgressDialog progress;

    private FirebaseAnalytics mFirebaseAnalytics;


    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_list, container, false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        mListView = (ListView) view.findViewById(R.id.recipe_list_view);

        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
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

        return view;
    }


    public void loadPage(boolean showProgress) {
        if (isOnline()) {

            if(showProgress){

                //TODO: Hacer solo un spinner http://stackoverflow.com/questions/21751662/create-a-progressdialog-only-with-the-spinner-in-the-middle
                progress = new ProgressDialog(getActivity());
                progress.setMessage("Actualizando noticias...");
                progress.show();
            }

            new ListFragment.DownloadXmlTask().execute();

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
                EntryAdapter adapter = new EntryAdapter(getActivity(), result);
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

                    Intent detailIntent = new Intent(getContext(), EntryDetailActivity.class);

                    detailIntent.putExtra("title", selectedRecipe.title);
                    detailIntent.putExtra("url", selectedRecipe.link);

                    startActivityForResult(detailIntent, 0);

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedRecipe.link);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
                }

            });

        }


    }



    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showError() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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

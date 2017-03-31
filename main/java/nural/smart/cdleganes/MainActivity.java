package nural.smart.cdleganes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by alvaro on 27/3/17.
 */

public class MainActivity  extends AppCompatActivity {

    private final String scheduleURL = "http://resultados.as.com/resultados/ficha/equipo/leganes/132/calendario/";
    private final String tableURL = "http://resultados.as.com/resultados/futbol/primera/clasificacion/";

    private final String idListFragment = "LIST";
    private final String idScheduleFragment = "SCHEDULE";
    private final String idTableFragment = "TABLE";

    private Fragment listFragment = ListFragment.newInstance();
    private Fragment scheduleFragment = WebViewFragment.newInstance(scheduleURL);
    private Fragment tableFragment = WebViewFragment.newInstance(tableURL);

    private FirebaseAnalytics mFirebaseAnalytics;

    //TODO: Controlar funcionamiento cuando se gira la pantalla
    //TODO: Spinner en los WebViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Noticias " + getResources().getString(R.string.name));

        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorName));



        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);



        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_news:
                                displayListFragment();
                                break;
                            case R.id.action_schedules:
                                displayScheduleFragment();
                                break;
                            case R.id.action_rating:
                                displayTableFragment();
                                break;
                        }

                        return true;
                    }
                });

        if (savedInstanceState == null) {
            displayListFragment();
            //TODO: Intentar hacer esto en el XML
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    private void displayListFragment() {
        displayFragment(listFragment, idListFragment, idScheduleFragment, idTableFragment);
    }

    private void displayScheduleFragment() {
        displayFragment(scheduleFragment, idScheduleFragment, idListFragment, idTableFragment);
    }

    private void displayTableFragment() {
        displayFragment(tableFragment, idTableFragment, idScheduleFragment, idListFragment);
    }

    private void displayFragment(Fragment showFragment, String idShowFragment, String idHideFragment1, String idHideFragment2){


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment prevShowFragment = getSupportFragmentManager().findFragmentByTag(idShowFragment);
        Fragment prevHideFragment1 = getSupportFragmentManager().findFragmentByTag(idHideFragment1);
        Fragment prevHideFragment2 = getSupportFragmentManager().findFragmentByTag(idHideFragment2);

        if(prevShowFragment == null){
            transaction.add(R.id.frame_layout, showFragment, idShowFragment);
        } else {
            transaction.show(prevShowFragment);
        }

        if(prevHideFragment1 != null && prevHideFragment1.isVisible()){
            transaction.hide(prevHideFragment1);
        }

        if(prevHideFragment2 != null && prevHideFragment2.isVisible()){
            transaction.hide(prevHideFragment2);
        }

        transaction.commit();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, idShowFragment);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }
}

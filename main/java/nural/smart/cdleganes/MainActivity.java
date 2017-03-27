package nural.smart.cdleganes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by alvaro on 27/3/17.
 */

public class MainActivity  extends AppCompatActivity {

    private final String scheduleURL = "http://resultados.as.com/resultados/ficha/equipo/leganes/132/calendario/";
    private final String tableURL = "http://resultados.as.com/resultados/futbol/primera/clasificacion/";

    //private Fragment listFragment = ListFragment.newInstance();
    //private Fragment scheduleFragment = WebViewFragment.newInstance(scheduleURL);
    //private Fragment tableFragment = WebViewFragment.newInstance(tableURL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Fragment selectedFragment = null;
                        String idFragment = "";
                        switch (item.getItemId()) {
                            case R.id.action_news:

                                idFragment = "News";
                                selectedFragment = getSupportFragmentManager().findFragmentByTag(idFragment);
                                if(selectedFragment == null) {
                                    selectedFragment = ListFragment.newInstance();
                                }
                                getSupportFragmentManager().popBackStack(idFragment, 0);
                                break;
                            case R.id.action_schedules:
                                selectedFragment = WebViewFragment.newInstance(scheduleURL);
                                idFragment = "Schedules";
                                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                                transaction2.replace(R.id.frame_layout, selectedFragment, idFragment);
                                transaction2.addToBackStack(null);
                                transaction2.commit();
                                break;
                            case R.id.action_rating:
                                selectedFragment = WebViewFragment.newInstance(tableURL);
                                idFragment = "Rating";
                                break;
                        }
                        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        //transaction.replace(R.id.frame_layout, selectedFragment, idFragment);
                        //transaction.addToBackStack(null);
                        //transaction.commit();
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_layout, ListFragment.newInstance(), "News");
            transaction.commit();

            //Used to select an item programmatically
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }



    }
}

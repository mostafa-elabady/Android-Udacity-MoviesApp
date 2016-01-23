package com.mostafa.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mostafa.moviesapp.adapters.ImagesGridAdapter;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.mostafa.moviesapp.tasks.FetchTask;
import com.mostafa.moviesapp.tasks.ParseMoviesTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView moviesGridView;
    private Spinner sorttypeSpinner;
    private ImagesGridAdapter moviesAdapter;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private FetchTask fetchFromServerTask;
    private ParseMoviesTask parseMoviesTask;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (isConnected(getActivity())) {
                parseMoviesTask = new ParseMoviesTask(getActivity(), moviesAdapter);
                fetchFromServerTask = new FetchTask(parseMoviesTask);
                String FORECAST_URL = String.format(Utility.MOVIES_API_URL, BuildConfig.MOVIES_DB_API_KEY, Utility.PAGE_DEFAULT_VALUE);
                fetchFromServerTask.execute(FORECAST_URL);
            } else {
                Toast.makeText(getActivity(), R.string.NOInternetConnection, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        sorttypeSpinner  = (Spinner) rootView.findViewById(R.id.spurcetypespinner);
//        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.sorttype_array, R.layout.item_spinner);
//        sorttypeSpinner.setAdapter(mSpinnerAdapter);

//        CustomSpinnerInteractionListener listener = new CustomSpinnerInteractionListener();
//        sorttypeSpinner.setOnTouchListener(listener);
//        sorttypeSpinner.setOnItemSelectedListener(listener);


        moviesGridView = (GridView) rootView.findViewById(R.id.maingridView);
        moviesAdapter = new ImagesGridAdapter(getContext(), new Movie[]{});
        moviesGridView.setAdapter(moviesAdapter);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = (Movie) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie", item);
                //Start details activity
                startActivity(intent);
            }
        });
        if (isConnected(getActivity())) {
            parseMoviesTask = new ParseMoviesTask(getActivity(),  moviesAdapter);
            fetchFromServerTask = new FetchTask(parseMoviesTask);
            String FORECAST_URL = String.format(Utility.MOVIES_API_URL, BuildConfig.MOVIES_DB_API_KEY ,  Utility.PAGE_DEFAULT_VALUE);
            fetchFromServerTask.execute(FORECAST_URL);
        } else {
            Toast.makeText(getActivity(), R.string.NOInternetConnection, Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }




}

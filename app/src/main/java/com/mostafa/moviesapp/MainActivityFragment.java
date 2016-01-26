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
import android.widget.ProgressBar;
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

    public enum SourceTypeEnum {MostPopular, HighestRated, Favorites}

    ;

    private GridView moviesGridView;
    private Spinner sorttypeSpinner;
    private ImagesGridAdapter moviesAdapter;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private FetchTask fetchFromServerTask;
    private ParseMoviesTask parseMoviesTask;
    private ProgressBar progressBar;
    private SourceTypeEnum currentDataSource = SourceTypeEnum.MostPopular;

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
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (Utility.isConnected(getActivity())) {
                refreshData();
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

        if (moviesAdapter == null) {
            sorttypeSpinner = (Spinner) rootView.findViewById(R.id.spurcetypespinner);
            sorttypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        currentDataSource = SourceTypeEnum.MostPopular;
                        refreshData();
                    } else if (position == 1) {
                        currentDataSource = SourceTypeEnum.HighestRated;
                        refreshData();
                    } else if (position == 2) {
                        currentDataSource = SourceTypeEnum.Favorites;
                        refreshData();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
            moviesGridView = (GridView) rootView.findViewById(R.id.maingridView);
            moviesAdapter = new ImagesGridAdapter(getContext(), new Movie[]{});
            moviesGridView.setAdapter(moviesAdapter);
            moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie item = (Movie) parent.getItemAtPosition(position);
                    ((ActivityCallBack) getActivity()).OnItemSelected(item);
                }
            });
            if (Utility.isConnected(getActivity())) {

            } else {
                Toast.makeText(getActivity(), R.string.NOInternetConnection, Toast.LENGTH_LONG).show();
            }
        }
        return rootView;
    }


    private void refreshData() {
        if (currentDataSource == SourceTypeEnum.MostPopular) {
            progressBar.setVisibility(View.VISIBLE);
            parseMoviesTask = new ParseMoviesTask(getActivity(), moviesAdapter, progressBar);
            fetchFromServerTask = new FetchTask(parseMoviesTask);
            String FORECAST_URL = String.format(Utility.MOVIES_API_URL, BuildConfig.MOVIES_DB_API_KEY, Utility.PAGE_DEFAULT_VALUE);
            fetchFromServerTask.execute(FORECAST_URL);
        } else if (currentDataSource == SourceTypeEnum.HighestRated) {
            progressBar.setVisibility(View.VISIBLE);
            parseMoviesTask = new ParseMoviesTask(getActivity(), moviesAdapter, progressBar);
            fetchFromServerTask = new FetchTask(parseMoviesTask);
            String FORECAST_URL = String.format(Utility.MOVIES_HIGHEST_RATED_API_URL, BuildConfig.MOVIES_DB_API_KEY, Utility.PAGE_DEFAULT_VALUE);
            fetchFromServerTask.execute(FORECAST_URL);
        } else if (currentDataSource == SourceTypeEnum.Favorites) {
            progressBar.setVisibility(View.VISIBLE);
            parseMoviesTask = new ParseMoviesTask(getActivity(), moviesAdapter, progressBar);
            fetchFromServerTask = new FetchTask(parseMoviesTask);
            String FORECAST_URL = String.format(Utility.MOVIES_API_URL, BuildConfig.MOVIES_DB_API_KEY, Utility.PAGE_DEFAULT_VALUE);
            fetchFromServerTask.execute(FORECAST_URL);
        }
    }
}

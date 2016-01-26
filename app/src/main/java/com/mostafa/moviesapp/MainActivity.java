package com.mostafa.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;


public class MainActivity extends AppCompatActivity implements ActivityCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        if (findViewById(R.id.detail_frameLayout) != null) {
            Utility.isTwoPane = true;
        }
        else {
            Utility.isTwoPane = false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public void OnItemSelected(Movie movie) {
        if (Utility.isTwoPane) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            if (movie != null) {
                bundle.putParcelable("movie", movie);
                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.detail_frameLayout, fragment);
                fragmentTransaction.commit();
            }

        } else {
            //Create intent
            Intent intent = new Intent(this, DetailActivity.class);
            if (movie != null) {
                intent.putExtra("movie", movie);
                //Start details activity
                startActivity(intent);
            }
        }
    }

}

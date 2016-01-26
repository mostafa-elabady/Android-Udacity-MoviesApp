package com.mostafa.moviesapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mostafa.moviesapp.models.Movie;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View mViewContainer = findViewById(R.id.detail_content_layout);
        if (mViewContainer != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.detail_content_layout, fragment);
                fragmentTransaction.commit();
            }
        }
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

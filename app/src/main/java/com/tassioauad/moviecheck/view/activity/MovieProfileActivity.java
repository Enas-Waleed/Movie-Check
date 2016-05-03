package com.tassioauad.moviecheck.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tassioauad.moviecheck.MovieCheckApplication;
import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.dagger.MovieProfileViewModule;
import com.tassioauad.moviecheck.model.entity.Movie;
import com.tassioauad.moviecheck.presenter.MovieProfilePresenter;
import com.tassioauad.moviecheck.view.MovieProfileView;
import com.tassioauad.moviecheck.view.fragment.MovieDetailFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieProfileActivity extends AppCompatActivity implements MovieProfileView {

    private static final String KEY_MOVIE = "MOVIE";

    @Inject
    MovieProfilePresenter presenter;

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieprofile);
        ButterKnife.bind(this);
        ((MovieCheckApplication) getApplication()).getObjectGraph().plus(new MovieProfileViewModule(this)).inject(this);

        setSupportActionBar(toolbar);

        final Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);

        presenter.init(movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return MovieDetailFragment.newInstance(movie);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.movieprofileactivity_general);
                    default:
                        return null;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieProfileActivity.class);
        intent.putExtra(KEY_MOVIE, movie);
        return intent;
    }

    @Override
    public void showMovieName(String title) {
        getSupportActionBar().setTitle(title);
    }
}
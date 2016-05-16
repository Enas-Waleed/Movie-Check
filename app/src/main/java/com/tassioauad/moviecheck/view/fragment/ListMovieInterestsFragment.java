package com.tassioauad.moviecheck.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tassioauad.moviecheck.MovieCheckApplication;
import com.tassioauad.moviecheck.R;
import com.tassioauad.moviecheck.dagger.ListMovieInterestViewModule;
import com.tassioauad.moviecheck.model.entity.MovieInterest;
import com.tassioauad.moviecheck.presenter.ListMovieInterestsPresenter;
import com.tassioauad.moviecheck.view.ListMovieInterestsView;
import com.tassioauad.moviecheck.view.activity.MovieProfileActivity;
import com.tassioauad.moviecheck.view.adapter.MovieInterestListAdapter;
import com.tassioauad.moviecheck.view.adapter.OnItemClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListMovieInterestsFragment extends Fragment implements ListMovieInterestsView {

    @Bind(R.id.recyclerview_movieinterests)
    RecyclerView recyclerViewMovieInterests;
    @Bind(R.id.linearlayout_anyfounded)
    LinearLayout linearLayoutAnyFounded;

    @Inject
    ListMovieInterestsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MovieCheckApplication) getActivity().getApplication()).getObjectGraph()
                .plus(new ListMovieInterestViewModule(this)).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listmovieinterest, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.loadMovieInterests();

        Tracker defaultTracker = ((MovieCheckApplication) getActivity().getApplication()).getDefaultTracker();
        defaultTracker.setScreenName("List Movie Interests of the User Screen");
        defaultTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static ListMovieInterestsFragment newInstance() {
        ListMovieInterestsFragment fragment = new ListMovieInterestsFragment();
        return fragment;
    }

    @Override
    public void showMovieInterests(List<MovieInterest> movieInterestList) {
        linearLayoutAnyFounded.setVisibility(View.GONE);
        recyclerViewMovieInterests.setVisibility(View.VISIBLE);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerViewMovieInterests.setLayoutManager(layoutManager);
        recyclerViewMovieInterests.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovieInterests.setAdapter(new MovieInterestListAdapter(movieInterestList, new OnItemClickListener<MovieInterest>() {
            @Override
            public void onClick(MovieInterest movieInterest, View view) {
                startActivity(MovieProfileActivity.newIntent(getActivity(), movieInterest.getMovie()));
            }
        }));
    }

    @Override
    public void warnAnyInterestFounded() {
        recyclerViewMovieInterests.setVisibility(View.VISIBLE);
        linearLayoutAnyFounded.setVisibility(View.GONE);
    }
}
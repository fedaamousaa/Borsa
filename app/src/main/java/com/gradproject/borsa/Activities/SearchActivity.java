package com.gradproject.borsa.Activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.StockSearchViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import io.realm.Realm;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity implements StockSearchViewAdapter.OnItemClickListener,
        AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //region Views
    @Bind(R.id.app_bar)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    RealmSearchView searchView;
    @Bind(R.id.realm_recycler_view)
    RealmRecyclerView recyclerView;
    //endregion

    Realm realm;
    RealmResults<Stock> stockList;
    StockSearchViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        appBarLayout.addOnOffsetChangedListener(this);

        // Realm search box and list view initialisation
        try {

            realm.init(this);
            realm = Realm.getDefaultInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.stockList = realm.where(Stock.class).findAll();
        adapter = new StockSearchViewAdapter(getBaseContext(), realm, "name");
        adapter.setOnItemClickListener(this);

//        searchView.associateRealmRecyclerView(recyclerView);
        searchView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }
    //endregion

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        appBarLayout.setExpanded(false);
        setFocusOnSearchView();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            // Not collapsed
            if (searchView.getVisibility() == View.VISIBLE) {
                hideSearchView();
            }
        } else {
            // Collapsed
            if (searchView.getVisibility() != View.VISIBLE) {
                showSearchView();
                //setFocusOnSearchView();
            }
        }
    }

    @Override
    public void onItemClick(View view, Stock stock, int index) {
        Snackbar.make(view, "Item (" + index + "): " + stock.getCompany().getName(), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    //region SearchView
    private void showSearchView() {
        searchView.setVisibility(View.VISIBLE);
    }

    private void hideSearchView() {
        searchView.setVisibility(View.INVISIBLE);
    }

    private void setFocusOnSearchView() {
//        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
//        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
    }


    }

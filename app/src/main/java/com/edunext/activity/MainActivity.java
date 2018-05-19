package com.edunext.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.edunext.R;
import com.edunext.adapter.SchoolRecyclerViewAdapter;
import com.edunext.database.DatabaseManager;
import com.edunext.model.SchoolModel;
import com.edunext.model.Utility;
import com.edunext.webservice.WebApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    @BindView(R.id.rv_data)
    RecyclerView rv_data;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private SchoolRecyclerViewAdapter schoolAdapter;
    private List<SchoolModel>  schoolList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context=MainActivity.this;
        setToolbar();
        setAdapter();
        setListener();
        fetchDataFromDatabaseAndDisplay();
        checkInternet_SchoolData();

    }

    private void setListener() {
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternet_SchoolData();
            }
        });

    }

    /*
     * Checking Internet and Fetching the school data from api
     */
    private void checkInternet_SchoolData() {
        enableRefreshing();
        if (Utility.isOnline((Activity) context)) {
            getSchoolDataApi();
        } else {
            disableRefreshing();
            Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
    private void disableRefreshing(){
        if(sw_refresh.isRefreshing())
            sw_refresh.setRefreshing(false);

    }

    private void enableRefreshing(){
            sw_refresh.setRefreshing(true);
    }

    /*
    * Fetching the school data from api
     */
    private void getSchoolDataApi() {
        WebApi.getService().getApiData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SchoolModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<SchoolModel> responseList) {
                        // Disabling refresh option
                        disableRefreshing();

                        // Saving School data into Database
                        DatabaseManager.saveOrUpdateSchoolData(context,responseList);

                        // Fetching dat from database and displaying it
                        fetchDataFromDatabaseAndDisplay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disableRefreshing();
                        System.out.print("Error: "+e.toString());
                        Toast.makeText(context, getString(R.string.error_occurred),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
    * Fetching school data from Database and updating the adapter for that.
     */
    private void fetchDataFromDatabaseAndDisplay() {
       List<SchoolModel> modelList= DatabaseManager.getSchoolData(this);
       if(modelList.size()>0) {
           tv_no_data.setVisibility(View.GONE);
           sw_refresh.setVisibility(View.VISIBLE);
           schoolList.clear();
           schoolList.addAll(modelList);
           schoolAdapter.setSchoolList(schoolList);
           schoolAdapter.notifyDataSetChanged();
       }else{
           tv_no_data.setVisibility(View.VISIBLE);
           sw_refresh.setVisibility(View.GONE);
       }

    }

    private void setAdapter() {
        tv_no_data.setVisibility(View.VISIBLE);
        sw_refresh.setVisibility(View.GONE);
        schoolList=new ArrayList<>();
        schoolAdapter=new SchoolRecyclerViewAdapter(this);
        rv_data.setAdapter(schoolAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rv_data.setLayoutManager(layoutManager);
        rv_data.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_list:
                loadListView();
                return true;

            case R.id.item_grid:
                loadGridView();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * setting Linear view
     */
    private void loadListView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rv_data.setLayoutManager(layoutManager);
        rv_data.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
        schoolAdapter.setLayoutType(SchoolRecyclerViewAdapter.TYPE_LIST);

    }



    /*
    * Setting Grid View
     */
    private void loadGridView() {
        GridLayoutManager layoutManager= new GridLayoutManager(this,2);
        rv_data.setLayoutManager(new GridLayoutManager(this,2));

        rv_data.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        schoolAdapter.setLayoutType(SchoolRecyclerViewAdapter.TYPE_GRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
}

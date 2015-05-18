package com.androidapp.manunya.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.adapters.ImageResultsAdapter;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.androidapp.manunya.gridimagesearch.models.SearchSetting;
import com.androidapp.manunya.gridimagesearch.subclasses.EndlessScrollListener;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements AdvancedSearchDialog.AdvancedSearchDialogListener{

    private SearchView searchView;
    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    protected ImageResultsAdapter aImageResults;
    private SearchSetting searchSetting;
    //private static final int REQUEST_CODE = 20;
    private static final int MAX_RESULTS = 64;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        searchSetting = new SearchSetting();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this,imageResults);
        gvResults.setAdapter(aImageResults);

    }


    private void setupViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        //set onclick to display each item's image
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch image display activity
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                //ImageResult imageResult = imageResults.get(position);
                //i.putExtra("url", imageResult.fullUrl);
                i.putExtra("result", imageResults.get(position));
                startActivity(i);
            }
        });
        //set infinite scroll
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("begin on load more: ", "page=" + page + ", totalItemsCount=" + totalItemsCount);
                if (totalItemsCount < MAX_RESULTS) {
                    String searchUrl = getSearchUrl(searchSetting, totalItemsCount);
                    loadDataFromApi(searchUrl);
                    aImageResults.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                Log.i("query text submitted: ", query);
                aImageResults.clear();
                String searchUrl = getSearchUrl(searchSetting, 0);
                loadDataFromApi(searchUrl);
                // Super important! Need this so onQueryTextSubmit won't fire twice
                searchView.clearFocus(); // http://stackoverflow.com/questions/17874951/searchview-onquerytextsubmit-runs-twice-while-i-pressed-once
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(this, "settings!", Toast.LENGTH_SHORT).show();
            // use dialog fragment instead
            /*Intent i = new Intent(SearchActivity.this, AdvancedSearchActivity.class);
            i.putExtra("setting", searchSetting);
            startActivityForResult(i, REQUEST_CODE);
            return true;*/
            Log.i("adapter length", aImageResults.getCount() + "");
            FragmentManager fm = getSupportFragmentManager();
            AdvancedSearchDialog advancedSearchDialog = AdvancedSearchDialog.newInstance("Advanced filters", searchSetting);
            advancedSearchDialog.show(fm, "dialog_advanced_search");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveAdvancedSearch(SearchSetting newSetting) {
        searchSetting = newSetting;
    }


    private void loadDataFromApi(String searchUrl) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJSON = null;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                    //aImageResults.notifyDataSetChanged();
                    //when you make changes to the adapter, it modifies the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.i("info", imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(SearchActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
                }
                Log.d("load data failed: ", statusCode + " - " + responseString);
            }
        });
    }

    private String getSearchUrl(SearchSetting searchSetting, int totalItemsCount) {
        String query = searchView.getQuery().toString();
        String imageSizeSetting = "";
        String colorFilterSetting = "";
        String imageTypeSetting = "";
        String siteFilterSetting = "";
        String startString = "";

        if (SearchSetting.hasValue(searchSetting.getSize())) {
            imageSizeSetting = "&imgsz=" + searchSetting.getSize();
        }
        if (SearchSetting.hasValue(searchSetting.getColorFilter())) {
            colorFilterSetting = "&imgcolor=" + searchSetting.getColorFilter();
        }
        if (SearchSetting.hasValue(searchSetting.getType())) {
            imageTypeSetting = "&imgtype=" + searchSetting.getType();
        }
        if (SearchSetting.hasValue(searchSetting.getSiteFilter())) {
            siteFilterSetting = "&as_sitesearch=" + searchSetting.getSiteFilter();
        }
        startString = "&start=" + totalItemsCount;
        Log.i("start at: ", startString);
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
                + query
                + "&rsz=8"
                + imageSizeSetting
                + colorFilterSetting
                + imageTypeSetting
                + siteFilterSetting
                + startString;
        Log.i("search URL: ", searchUrl);
        return searchUrl;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }




    /*
    // Handle Search setting results from advanced search activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for request code
        if (requestCode == REQUEST_CODE) {
            //check result code
            if (resultCode == RESULT_OK) {
                //extract result data
                searchSetting = (SearchSetting) data.getSerializableExtra("setting");
            }
        }

    }*/
}

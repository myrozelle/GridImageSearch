package com.androidapp.manunya.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.androidapp.manunya.gridimagesearch.EndlessScrollListener;
import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.adapters.ImageResultsAdapter;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.androidapp.manunya.gridimagesearch.models.SearchSetting;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    protected ImageResultsAdapter aImageResults;
    private SearchSetting searchSetting;
    private static final int REQUEST_CODE = 20;

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
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
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
                //Toast.makeText(SearchActivity.this, page + ", " + totalItemsCount, Toast.LENGTH_SHORT).show();
                String searchUrl = getSearchUrl(searchSetting, totalItemsCount);
                loadDataFromApi(searchUrl);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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
            Intent i = new Intent(SearchActivity.this, AdvancedSearchActivity.class);
            i.putExtra("setting", searchSetting);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Fire when the button is pressed
    public void onImageSearch(View view) {
        //imageResults.clear();//clear existing images from array
        aImageResults.clear();
        String searchUrl = getSearchUrl(searchSetting, 0);
        loadDataFromApi(searchUrl);
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
                Log.d("load data failed: ", statusCode + " - " + responseString);
            }
        });
    }

    private String getSearchUrl(SearchSetting searchSetting, int totalResCount) {
        String query = etQuery.getText().toString();
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
        if (totalResCount > 0) {
            startString = "&start=" + totalResCount;
        }
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
                + query
                + "&rsz=8"
                + imageSizeSetting
                + colorFilterSetting
                + imageTypeSetting
                + siteFilterSetting
                + startString;

        return searchUrl;
    }

    // HANDLE ALL FORM RESULTS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for request code
        if (requestCode == REQUEST_CODE) {
            //check result code
            if (resultCode == RESULT_OK) {
                //extract result data
                searchSetting = (SearchSetting) data.getSerializableExtra("setting");
                /*Toast.makeText(this, searchSetting.getSize()
                        + ", " + searchSetting.getColorFilter()
                        + ", " + searchSetting.getType()
                        + ", " + searchSetting.getSiteFilter(), Toast.LENGTH_SHORT).show();*/
            }
        }

    }
}

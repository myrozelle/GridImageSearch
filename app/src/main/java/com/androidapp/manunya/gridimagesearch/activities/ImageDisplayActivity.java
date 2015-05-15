package com.androidapp.manunya.gridimagesearch.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;


public class ImageDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        //remove actionbar on this activity
        getSupportActionBar().hide();
        //get data from intent
        //String url = getIntent().getStringExtra("url");
        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        ImageView ivImageDisplay = (ImageView) findViewById(R.id.ivImageDisplay);
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        Picasso.with(this).load(imageResult.fullUrl).resize(screenWidth, 0).into(ivImageDisplay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

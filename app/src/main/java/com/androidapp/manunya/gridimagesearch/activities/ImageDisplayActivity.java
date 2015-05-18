package com.androidapp.manunya.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.androidapp.manunya.gridimagesearch.subclasses.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageDisplayActivity extends ActionBarActivity {

    private ShareActionProvider miShareAction;
    private TouchImageView ivImageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        //remove actionbar on this activity
        //getSupportActionBar().hide();
        setupViews();

    }

    private void setupViews() {
        //get data from intent
        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        ivImageDisplay = (TouchImageView) findViewById(R.id.ivImageDisplay);
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        Picasso.with(this).load(imageResult.fullUrl).resize(screenWidth, 0).into(ivImageDisplay, new Callback() {
            @Override
            public void onSuccess() {
                // Setup share intent now that image has loaded
                setupShareIntent();
            }

            @Override
            public void onError() {
                Log.i("error", "image load error");
            }
        });
        /*ivImageDisplay.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {

            }
        });*/
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent() {
        Uri bmpUri = getLocalBitmapUri(ivImageDisplay);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Attach share event to the menu item provider
            //miShareAction.setShareIntent(shareIntent);
            // need the if condition to make repeat image display work, not sure why
            if (miShareAction != null) {
                miShareAction.setShareIntent(shareIntent);
            }
        } else {
            // ...sharing failed, handle error
            //Toast.makeText(this, "Can't get URI for sharing", Toast.LENGTH_SHORT).show();
            Log.i("error", "Can't get URI for sharing");
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_item_share));
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

        }

        return super.onOptionsItemSelected(item);
    }
}

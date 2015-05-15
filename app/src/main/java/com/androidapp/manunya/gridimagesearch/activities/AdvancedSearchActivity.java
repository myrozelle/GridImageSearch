package com.androidapp.manunya.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.SearchSetting;

public class AdvancedSearchActivity extends ActionBarActivity {
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        SearchSetting curSetting = (SearchSetting) getIntent().getSerializableExtra("setting");
        setupViews(curSetting);
    }

    private void setupViews(SearchSetting curSetting) {
        spImageSize = (Spinner) findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
        spImageType =(Spinner) findViewById(R.id.spImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        setSpinnerToValue(spImageSize, curSetting.getSize());
        setSpinnerToValue(spColorFilter, curSetting.getColorFilter());
        setSpinnerToValue(spImageType, curSetting.getType());
        if (SearchSetting.hasValue(curSetting.getSiteFilter())) {
            etSiteFilter.setText(curSetting.getSiteFilter());
        }
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_advanced_search, menu);
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

    public void OnSaveSettings(View view) {
        String imageSize = spImageSize.getSelectedItem().toString();
        String colorFilter = spColorFilter.getSelectedItem().toString();
        String imageType = spImageType.getSelectedItem().toString();
        String siteFilter = etSiteFilter.getText().toString();
        Intent i = new Intent();
        i.putExtra("setting", new SearchSetting(imageSize, colorFilter, imageType, siteFilter));
        setResult(RESULT_OK, i);
        finish();

    }
}

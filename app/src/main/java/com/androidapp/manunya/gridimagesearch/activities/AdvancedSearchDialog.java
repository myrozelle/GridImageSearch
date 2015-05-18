package com.androidapp.manunya.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.SearchSetting;


public class AdvancedSearchDialog extends DialogFragment {
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;
    private Button btSave;

    public interface AdvancedSearchDialogListener {
        void onSaveAdvancedSearch(SearchSetting newSetting);
    }

    public AdvancedSearchDialog() {
        // Required empty public constructor
    }

    public static AdvancedSearchDialog newInstance(String title, SearchSetting curSetting) {
        AdvancedSearchDialog fragment = new AdvancedSearchDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("current filters", curSetting);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_advanced_search, container);
        String title = getArguments().getString("title", "Advanced filters");
        SearchSetting curSetting = (SearchSetting) getArguments().getSerializable("current filters");
        getDialog().setTitle(title);
        //SearchSetting curSetting = (SearchSetting) getIntent().getSerializableExtra("setting");
        //setupViews(curSetting);
        setupViews(view, curSetting);
        return view;
    }

    private void setupViews(View view, SearchSetting curSetting) {
        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        spImageType =(Spinner) view.findViewById(R.id.spImageType);
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        btSave = (Button) view.findViewById(R.id.btSave);
        setSpinnerToValue(spImageSize, curSetting.getSize());
        setSpinnerToValue(spColorFilter, curSetting.getColorFilter());
        setSpinnerToValue(spImageType, curSetting.getType());
        if (SearchSetting.hasValue(curSetting.getSiteFilter())) {
            etSiteFilter.setText(curSetting.getSiteFilter());
        }
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageSize = spImageSize.getSelectedItem().toString();
                String colorFilter = spColorFilter.getSelectedItem().toString();
                String imageType = spImageType.getSelectedItem().toString();
                String siteFilter = etSiteFilter.getText().toString();
                AdvancedSearchDialogListener listener = (AdvancedSearchDialogListener) getActivity();
                listener.onSaveAdvancedSearch(new SearchSetting(imageSize, colorFilter, imageType, siteFilter));
                dismiss();
            }
        });
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


}

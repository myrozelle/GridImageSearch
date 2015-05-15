package com.androidapp.manunya.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Manunya on 5/12/2015.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        // clear out image
        ivImage.setImageResource(0);
        tvTitle.setText(Html.fromHtml(imageResult.title));
        //download image async
        Picasso.with(getContext()).load(imageResult.thumbUrl).into(ivImage);
        return convertView;
    }
}

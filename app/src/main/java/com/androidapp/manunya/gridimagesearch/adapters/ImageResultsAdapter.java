package com.androidapp.manunya.gridimagesearch.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidapp.manunya.gridimagesearch.R;
import com.androidapp.manunya.gridimagesearch.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Manunya on 5/12/2015.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static class ViewHolder {
        DynamicHeightImageView ivImage;
        //ImageView ivImage;
    }
    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageResult imageResult = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
            viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
            //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear out image
        viewHolder.ivImage.setImageResource(0);
        //tvTitle.setText(Html.fromHtml(imageResult.title));
        viewHolder.ivImage.setHeightRatio(1.0*imageResult.tbHeight/imageResult.tbWidth);
        //download image async

        Log.i("ImageResultsAdapter:",  "position: " + position + " thumb url: " + imageResult.thumbUrl);

        Picasso.with(getContext()).load(imageResult.thumbUrl).into(viewHolder.ivImage);
        return convertView;
    }

}

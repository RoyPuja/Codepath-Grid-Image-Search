package com.codepath.goohleimagesearcher.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.goohleimagesearcher.R;
import com.codepath.goohleimagesearcher.model.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pnroy on 9/25/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context,R.layout.item_image_result, images);
    }

    public View getView(int position,View convertView,ViewGroup parent){
       ImageResult imageInfo=getItem(position);
        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
        }
        ImageView ivImage=(ImageView)convertView.findViewById(R.id.ivImage);
        TextView txtDesc=(TextView)convertView.findViewById(R.id.tvDesc);
        //clear out image from last time
        ivImage.setImageResource(0);
        //Populate the title and remote image url
        txtDesc.setText(Html.fromHtml(imageInfo.title));
        //Remotely download the image data in the background
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
        //Return the completed view to be dispalyed
        return convertView;

    }
}

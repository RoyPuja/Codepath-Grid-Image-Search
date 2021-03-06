package com.codepath.goohleimagesearcher.dialogs;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.goohleimagesearcher.R;
import com.codepath.goohleimagesearcher.helperMethods.TouchImageView;
import com.codepath.goohleimagesearcher.model.ImageResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pnroy on 9/27/15.
 */
public class ImageDisplayDialog extends DialogFragment {

    private ImageResult image;
    private TouchImageView ivImageFullScreen;
    private TextView tvImageTitle;
    private Button btnShareImage;
    private Button btnCloseImage;

    public ImageDisplayDialog() {
    }

    public static ImageDisplayDialog newInstance(ImageResult image) {
        ImageDisplayDialog frag = new ImageDisplayDialog();
        frag.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_ImageDetail);
        Bundle args = new Bundle();
        args.putParcelable("image", image);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_image_display, container, false);
        image = getArguments().getParcelable("image");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        tvImageTitle = (TextView)view.findViewById(R.id.tvImageTitle);
        tvImageTitle.setText(Html.fromHtml(image.title));


        ivImageFullScreen = (TouchImageView)view.findViewById(R.id.ivImageFullScreen);
        // set image using Picasso
        Picasso.with(getActivity())
                .load(image.fullUrl)
                .into(ivImageFullScreen);

        btnShareImage = (Button)view.findViewById(R.id.btnShareImage);
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

        btnCloseImage = (Button)view.findViewById(R.id.btnCloseImage);
        btnCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    // Can be triggered by a view event such as a button press
    private void shareImage() {
        // Get access to bitmap image from view
        ImageView ivImage = ivImageFullScreen;
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    private Uri getLocalBitmapUri(ImageView imageView) {
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


}

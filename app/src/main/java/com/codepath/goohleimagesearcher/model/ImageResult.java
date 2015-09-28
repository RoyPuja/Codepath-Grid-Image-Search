package com.codepath.goohleimagesearcher.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pnroy on 9/25/15.
 */
public class ImageResult implements Parcelable {
    //private static final long serialVersionUID=-2893089570992474768L;
    public String fullUrl;
    public String tbUrl;
    public String title;
    public String content;
    public String originalContextUrl;

    //new image result(..raw item json)
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullUrl);
        dest.writeString(tbUrl);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(originalContextUrl);
    }

    public static final Parcelable.Creator<ImageResult> CREATOR
            = new Parcelable.Creator<ImageResult>() {
        @Override
        public ImageResult createFromParcel(Parcel in) {
            return new ImageResult(in);
        }

        @Override
        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };

    private ImageResult(Parcel in) {
        fullUrl = in.readString();
        tbUrl = in.readString();
        title = in.readString();
        content = in.readString();
        originalContextUrl = in.readString();
    }

    public ImageResult() {}
}

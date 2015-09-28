package com.codepath.goohleimagesearcher.helperMethods;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by pnroy on 9/27/15.
 */
public class SettingOptions implements Parcelable {
    public String imageSize;
    public String colorFilter;
    public String imageType;
    public String site;
    public String searchTerm;
    public String start;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(imageSize);
        out.writeString(colorFilter);
        out.writeString(imageType);
        out.writeString(site);
        out.writeString(searchTerm);
        out.writeString(start);
    }

    public static final Parcelable.Creator<SettingOptions> CREATOR
            = new Parcelable.Creator<SettingOptions>() {
        @Override
        public SettingOptions createFromParcel(Parcel in) {
            return new SettingOptions(in);
        }

        @Override
        public SettingOptions[] newArray(int size) {
            return new SettingOptions[size];
        }
    };

    private SettingOptions(Parcel in) {
        imageSize = in.readString();
        colorFilter = in.readString();
        imageType = in.readString();
        site = in.readString();
        searchTerm = in.readString();
        start = in.readString();
    }

    public SettingOptions(String query) {
        searchTerm = query;

        // default values
        imageSize = "any";
        colorFilter = "any";
        imageType = "any";
    }
}

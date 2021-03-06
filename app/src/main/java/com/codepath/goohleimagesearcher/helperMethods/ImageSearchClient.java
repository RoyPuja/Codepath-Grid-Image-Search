package com.codepath.goohleimagesearcher.helperMethods;

import android.util.Log;

import com.codepath.goohleimagesearcher.model.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pnroy on 9/27/15.
 */
public class ImageSearchClient {

    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private static final String SEARCH_PARAM = "q";
    private static final String IMAGE_TYPE_PARAM = "imgtype";
    private static final String SITE_PARAM = "as_sitesearch";
    private static final String IMAGE_COLOR_PARAM = "imgcolor";
    private static final String IMAGE_SIZE_PARAM = "imgsz";
    private static final String START_PARAM = "start";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void searchImages(SettingOptions searchOptions, JsonHttpResponseHandler responseHandler) {
        RequestParams params = constructParamsFromSearchOptions(searchOptions);

        // default required params
        params.put("v", "1.0");
        params.put("rsz", "8");

        client.get(BASE_URL, params, responseHandler);
    }

    private static RequestParams constructParamsFromSearchOptions(SettingOptions searchOptions) {
        RequestParams params = new RequestParams();
        if (searchOptions.imageType != null) {
            params.put(IMAGE_TYPE_PARAM, searchOptions.imageType);
        }
        if (searchOptions.imageSize != null) {
            params.put(IMAGE_SIZE_PARAM, searchOptions.imageSize);
        }
        if (searchOptions.colorFilter != null) {
            params.put(IMAGE_COLOR_PARAM, searchOptions.colorFilter);
        }
        if (searchOptions.site != null) {
            params.put(SITE_PARAM, searchOptions.site);
        }
        if (searchOptions.searchTerm != null) {
            params.put(SEARCH_PARAM, searchOptions.searchTerm);
        }
        if (searchOptions.start != null) {
            params.put(START_PARAM, searchOptions.start);
        }
        return  params;
    }


}

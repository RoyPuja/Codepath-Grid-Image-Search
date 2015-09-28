package com.codepath.goohleimagesearcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import com.codepath.goohleimagesearcher.dialogs.FilterDialog;
import com.codepath.goohleimagesearcher.dialogs.ImageDisplayDialog;
import com.codepath.goohleimagesearcher.helperMethods.EndlessScrollListener;
import com.codepath.goohleimagesearcher.helperMethods.ImageSearchClient;
import com.codepath.goohleimagesearcher.helperMethods.NetworkConnectivity;
import com.codepath.goohleimagesearcher.helperMethods.SettingOptions;
import com.etsy.android.grid.StaggeredGridView;

import com.codepath.goohleimagesearcher.adapters.ImageResultsAdapter;
import com.codepath.goohleimagesearcher.model.ImageResult;
import com.codepath.goohleimagesearcher.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public  class SearchActivity extends ActionBarActivity implements FilterDialog.ImageFiltersDialogListener {
    private EditText etSearch;
    private StaggeredGridView gvResults;
    ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private boolean isFetching;
    private SettingOptions settingOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //creates the datasource
        imageResults=new ArrayList<ImageResult>();
        //attaches the data source to an adapter
        aImageResults =new ImageResultsAdapter(this,imageResults);
        //Link the adapter to the gridview
        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                imageSearch(false);
            }
        });
        settingOptions=new SettingOptions("");
    }

    private void setupViews(){
        //etSearch=(EditText)findViewById(R.id.etQuery);
        gvResults=(StaggeredGridView)findViewById(R.id.gvSearchItems);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showImageDetailDialog(aImageResults.getItem(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                settingOptions.searchTerm = query;
                imageSearch(true);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void imageSearch(final boolean isNewQuery) {
        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "No internet! Try again later...", Toast.LENGTH_SHORT).show();
        }
        if (isFetching) {
            return;
        }
        ImageSearchClient.searchImages(settingOptions, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                isFetching = false;
                try {
                    if (isNewQuery) {
                        settingOptions.start = "0";
                        imageResults.clear();
                    }

                    JSONObject cursorJSON = response.getJSONObject("responseData").getJSONObject("cursor");
                    int currentPage = cursorJSON.getInt("currentPageIndex");

                    JSONArray pages = cursorJSON.getJSONArray("pages");
                    if ((pages.length() - 1) > currentPage) {
                        JSONObject page = pages.getJSONObject(currentPage + 1);
                        settingOptions.start = page.getString("start");
                    } else if (!isNewQuery) {
                        // stop searching once we have reached the end
                        Toast.makeText(getApplicationContext(), "No more results for this search!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray imagesJSON = response.getJSONObject("responseData").getJSONArray("results");
                    for (int i = 0; i < imagesJSON.length(); i++) {
                        JSONObject imageJSON = imagesJSON.getJSONObject(i);
                        ImageResult image = new ImageResult();
                        image.title = imageJSON.getString("title");
                        image.content = imageJSON.getString("content");
                        image.fullUrl = imageJSON.getString("url");
                        image.tbUrl = imageJSON.getString("tbUrl");
                        image.originalContextUrl = imageJSON.getString("originalContextUrl");
                        imageResults.add(image);
                    }
                    aImageResults.notifyDataSetChanged();

                    if (aImageResults.getCount() < 12) {
                        imageSearch(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Search failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showFiltersDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFiltersDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog imageFiltersDialog = FilterDialog.newInstance("Advanced Filters",
                settingOptions);
        imageFiltersDialog.show(fm, "settings_filter");
    }
    private void showImageDetailDialog(ImageResult image) {
        FragmentManager fm = getSupportFragmentManager();
        ImageDisplayDialog imageDisplayDialog = ImageDisplayDialog.newInstance(image);
        imageDisplayDialog.show(fm,"activity_image_display");
        //imageDetailDialog.show(fm, "activity_image_display");
    }

    @Override
    public void onFinishImageFiltersDialog(SettingOptions searchOptions) {
        this.settingOptions = searchOptions;
        imageSearch(true);
    }
}







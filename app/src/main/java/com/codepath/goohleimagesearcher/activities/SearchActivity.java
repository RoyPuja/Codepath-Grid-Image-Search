package com.codepath.goohleimagesearcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.goohleimagesearcher.adapters.ImageResultsAdapter;
import com.codepath.goohleimagesearcher.model.ImageResult;
import com.codepath.goohleimagesearcher.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private GridView gvResults;
    ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;


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
    }

    private void setupViews(){
        etSearch=(EditText)findViewById(R.id.etQuery);
        gvResults=(GridView)findViewById(R.id.gvSearchItems);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the image display activity
                //Creating an intent
            Intent i =new Intent(SearchActivity.this,ImageDisplayActivity.class);
                //Get the image results to display
            ImageResult result=imageResults.get(position);
                //Pass the image results into the intent
                i.putExtra("result",result);
                //Launch the new activity
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
    public void onImageSearch(View view) {
        String query=etSearch.getText().toString();
       // Toast.makeText(this, "Serach for :" + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client=new AsyncHttpClient();
        //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz-8
        String searchUrl="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8";
        client.get(searchUrl,new JsonHttpResponseHandler(){
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("DEBUG", response.toString());
             JSONArray imageResultJson=null;
             try {
                 imageResultJson=response.getJSONObject("responseData").getJSONArray("results");
                 imageResults.clear();//clear the existing images from the array(in cases of new search)
                 //When you make changes to the adapter it does modify the underlying data
                 //imageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                 aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                // aImageResults.notifyDataSetChanged();

             } catch (JSONException e) {
                 e.printStackTrace();
             }
             Log.i("INFO",imageResults.toString());
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

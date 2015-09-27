package com.codepath.goohleimagesearcher.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.codepath.goohleimagesearcher.R;
import com.codepath.goohleimagesearcher.model.ImageResult;
import com.squareup.picasso.Picasso;

/**
 * Created by pnroy on 9/26/15.
 */
public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        //hide the action bar
       // getActionBar().hide();
        //Pull out thr url from the intent
      ImageResult result=(ImageResult)getIntent().getSerializableExtra("result");
        //Find the image results view
        ImageView ivResult=(ImageView)findViewById(R.id.ivImageResult);
        //Load the image url into imge view using picasso
        Picasso.with(this).load(result.fullUrl).into(ivResult);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

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

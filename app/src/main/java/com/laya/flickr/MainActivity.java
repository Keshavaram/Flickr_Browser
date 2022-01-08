package com.laya.flickr;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.laya.flickr.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable,
                                                                RecyclerItemClickListener.OnRecyclerClickListener
{

    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //    private AppBarConfiguration appBarConfiguration;
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this,
                "https://www.flickr.com/services/feeds/photos_public.gne",
                "en-us",
                true);
        getFlickrJsonData.execute("cars,BMW,race,drift");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

@Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status)
    {
        Log.d(TAG, "onDataAvailable: starts");
        if (status ==  DownloadStatus.OK)
        {
            mFlickrRecyclerViewAdapter.loadNewData(data);
        } else
        {
            Log.e(TAG, "onDataAvailable: Error with status " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this, "Normal click at pos : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position)
    {
        Log.d(TAG, "onItemLongClick: starts");
        Toast.makeText(MainActivity.this, "Long Click at pos : " + position, Toast.LENGTH_SHORT).show();
    }
}
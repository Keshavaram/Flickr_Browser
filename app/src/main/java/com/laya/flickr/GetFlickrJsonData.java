package com.laya.flickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends AsyncTask<String,Void,List<Photo>> implements GetRawData.OnDownloadComplete
{
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;
    private boolean onSameThread = false;

    private final OnDataAvailable mCallBack;

    interface OnDataAvailable
    {
        void onDataAvailable(List<Photo> data,DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callBack, String baseUrl, String language, boolean matchAll)
    {
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }

    public void executeOnSameThread(String searchCriteria)
    {
        onSameThread = true;
        String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
    }

    @Override
    protected void onPostExecute(List<Photo> photos)
    {
        Log.d(TAG, "onPostExecute: starts");

        if(mCallBack != null)
        {
            mCallBack.onDataAvailable(mPhotoList,DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params)
    {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0],mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return null;
    }

    private String createUri(String criteria, String lang, boolean matchAll)
    {
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags",criteria)
                .appendQueryParameter("tagmode",matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownload(String data, DownloadStatus status)
    {
        if (status == DownloadStatus.OK)
        {
            mPhotoList = new ArrayList<>();
            try
            {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i = 0;i < jsonArray.length();i++)
                {
                    JSONObject jsonPhoto = jsonArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.","_b.");

                    Photo photo = new Photo(title,author,authorId,link,tags,photoUrl);
                    mPhotoList.add(photo);

                    Log.d(TAG, "onDownload: Photo =>" + photo);
                }
            }catch(JSONException e)
            {
                Log.e(TAG, "onDownload: Invalid JSON" + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (onSameThread && mCallBack != null)
        {
            mCallBack.onDataAvailable(mPhotoList,status);
        }
    }
}

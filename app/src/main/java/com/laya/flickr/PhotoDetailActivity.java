package com.laya.flickr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.laya.flickr.databinding.ActivityPhotoDetailBinding;

public class PhotoDetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        com.laya.flickr.databinding.ActivityPhotoDetailBinding binding = ActivityPhotoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }
}
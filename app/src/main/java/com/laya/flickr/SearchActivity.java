package com.laya.flickr;

import android.os.Bundle;

import com.laya.flickr.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        com.laya.flickr.databinding.ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        activateToolbar(true);
    }
}
package com.skript.photobombgo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.skript.photobombgo.R;
import com.skript.photobombgo.gallery.GalleryAdapter;
import com.skript.photobombgo.gallery.ImageModel;
import com.skript.photobombgo.gallery.RecyclerItemClickListener;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class PictureGallery extends AppCompatActivity {

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;
    String pokeName;

    ArrayList<ImageModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture_gallery);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        pokeName = extras.getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(pokeName);
        setSupportActionBar(toolbar);


        ArrayList<Integer> imageLocations = new ArrayList<Integer>();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

        Resources resources = this.getResources();
        Field[] drawables = R.drawable.class.getFields();
        int resourceId;
        for (Field f : drawables) {
            try {
                String name = f.getName();
                if((name.startsWith(pokeName.toLowerCase()) && !pokeName.equals("Mew")) || (pokeName.equals("Mew") && name.startsWith("mewa"))) {
                    resourceId = resources.getIdentifier(name, "drawable", this.getPackageName());
                    imageLocations.add(resourceId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < imageLocations.size(); i++){
            addImageModels(imageLocations.get(i));
        }

        updateAdapter(data);
    }

    private void addImageModels(int resourceId) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(pokeName);
        imageModel.setResourceId(resourceId);
        data.add(imageModel);
    }

    private void updateAdapter(ArrayList<ImageModel> completeData) {
        mAdapter = new GalleryAdapter(PictureGallery.this, completeData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(PictureGallery.this, Tattoo.class);
                        intent.putExtra("ID", data.get(position).getResourceId());
                        startActivity(intent);
                    }
                }));
    }



}


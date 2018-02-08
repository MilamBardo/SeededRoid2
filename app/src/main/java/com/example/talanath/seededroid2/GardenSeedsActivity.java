package com.example.talanath.seededroid2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.Garden;
import models.GardenSeed;
import models.SeedType;
import utils.Constants;

public class GardenSeedsActivity extends AppCompatActivity {

    List<models.SeedType> seedTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {
            setContentView(R.layout.activity_garden_seeds);
            android.widget.TextView gardenName = (android.widget.TextView) findViewById(R.id.txtGardenName);

            MySQLiteHelper helper = new MySQLiteHelper(this);

            seedTypes = helper.GetAllSeedTypes();
            //models.Garden garden = helper.GetGardenByID(long gardenID)

            // get the text from MainActivity
            Intent intent = getIntent();
            String gardenIdString = intent.getStringExtra(Intent.EXTRA_TEXT);
            long gardenId = Long.parseLong(gardenIdString);

            final Garden garden = helper.GetGardenByID(gardenId);

            gardenName.setText(garden.getGardenName());

            //should now have garden plu seeds, so set grids

            GridView gridView = (GridView) findViewById(R.id.grdNewSeeds);
            final List<GardenSeed> gseeds = garden.getSeeds();
            gridView.setAdapter(new GardenSeedsActivity.CustomGridSeedsAdapter(this, gseeds));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // DO something
                    // start the SecondActivity
                    models.GardenSeed gseed = gseeds.get(position);
                    long seedId = gseed.getSeedID();
                    SeedType seedType = gseed.getSeedType();

                    if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))
                    {
                        android.content.Intent intent = new android.content.Intent(parent.getContext(), ThoughtSeedViewActivity.class);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(seedId));
                        startActivity(intent);
                    }
                    else if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE)) {
                        android.content.Intent intent = new android.content.Intent(parent.getContext(), ImageSeedViewActivity.class);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(seedId));
                        startActivity(intent);
                    }


                }
            });
        }
        catch (Exception ex)
        {
                ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }



    public class CustomGridSeedsAdapter extends BaseAdapter {

        private android.content.Context Context;
        private List<GardenSeed> Seeds;
        LayoutInflater Inflater;

        public CustomGridSeedsAdapter(Context context, List<GardenSeed> seeds) {
            this.Context = context;
            this.Seeds = seeds;
            Inflater = (LayoutInflater) this.Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {

            View grid;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                grid = Inflater.inflate( R.layout.seeds_grid_item, parent, false );
                //TextView textView = (TextView) grid.findViewById(R.id.txtGardenName);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                //textView.setText(Seeds.get(position).getSeedType().getSeedTypeName());
                models.SeedType seedType = Seeds.get(position).getSeedType();
                if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))
                {
                    imageView.setImageResource(R.drawable.seedicon_lightbulb);
                }
                else {
                    imageView.setImageResource(R.drawable.seedicon_image);
                }
            }
            else {
                grid = (View) convertView;
            }
//            //exus
//            btn.setText(Gardens.get(position).getGardenName());
//            // filenames is an array of strings
//            btn.setTextColor(Color.WHITE);
//            btn.setBackgroundResource(R.drawable.gardenplus);
//            btn.setId(position);

            return grid;
        }

        @Override
        public int getCount() {
            return Seeds.size();
        }

        @Override
        public Object getItem(int position) {
            return Seeds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}

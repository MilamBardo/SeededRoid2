package com.example.talanath.seededroid2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.Garden;

public class GardensViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {



            android.widget.TextView noGarden = (android.widget.TextView) findViewById(R.id.txtNoGarden);
            setContentView(R.layout.activity_gardens_view);

            MySQLiteHelper helper = new MySQLiteHelper(this);

            noGarden = (android.widget.TextView) findViewById(R.id.txtNoGarden);
            final List<Garden> gardens = helper.GetAllGardens();
            if (gardens.size() > 0) {
                noGarden.setVisibility(View.INVISIBLE);
            }

            ArrayAdapter<Garden> adapter = new ArrayAdapter<Garden>(this,
                    android.R.layout.simple_list_item_1, gardens);

            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(new CustomGridAdapter(this, gardens));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // DO something
                    // start the SecondActivity
                    long gardenId = gardens.get(position).get_ID();
                    android.content.Intent intent = new android.content.Intent(parent.getContext(), GardenSeedsActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(gardenId));
                    startActivity(intent);
                }
            });

            addListenerOnAddGardenButton();
        }
        catch(Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    public class CustomGridAdapter extends BaseAdapter {

        private Context Context;
        private List<Garden> Gardens;
        LayoutInflater Inflater;

        public CustomGridAdapter(Context context, List<Garden> gardens) {
            this.Context = context;
            this.Gardens = gardens;
            Inflater = (LayoutInflater) this.Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {

            View grid;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                //btn = new Button(Context);
                //btn.setLayoutParams(new GridView.LayoutParams(100, 55));
                //btn.setPadding(8, 8, 8, 8);
                grid = Inflater.inflate( R.layout.gardensview_grid_item, parent, false );
                TextView textView = (TextView) grid.findViewById(R.id.txtGardenName);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                textView.setText(Gardens.get(position).getGardenName());
                imageView.setImageResource(R.drawable.garden);
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
            return Gardens.size();
        }

        @Override
        public Object getItem(int position) {
            return Gardens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void addListenerOnAddGardenButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.fabAddGarden);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(GardensViewActivity.this, AddGardenActivity.class);
                startActivity(intent);
            }
        });
    }
}

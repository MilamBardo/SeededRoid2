package com.example.talanath.seededroid2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.Garden;
import models.Tree;

public class TreesViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_trees_view);

            addListenerOnAddTreeButton();

            android.widget.TextView noTrees = (android.widget.TextView) findViewById(R.id.txtNoTrees);

            MySQLiteHelper helper = new MySQLiteHelper(this);

            final ArrayList<Tree> trees = helper.GetAllTrees();
            if (trees.size() > 0) {
                noTrees.setVisibility(View.INVISIBLE);
            }

            ArrayAdapter<Tree> adapter = new ArrayAdapter<Tree>(this,
                    android.R.layout.simple_list_item_1, trees);

            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(new TreesViewActivity.CustomGridAdapter(this, trees));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // DO something
                    // start the SecondActivity
                    long treeId = trees.get(position).get_ID();
                    android.content.Intent intent = new android.content.Intent(parent.getContext(), TreeActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(treeId));
                    startActivity(intent);
                }
            });


        }
        catch(Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    public class CustomGridAdapter extends BaseAdapter {

        private android.content.Context Context;
        private ArrayList<Tree> Trees;
        LayoutInflater Inflater;

        public CustomGridAdapter(Context context, ArrayList<Tree> trees) {
            this.Context = context;
            this.Trees = trees;
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
                textView.setText(Trees.get(position).getTreeName());
                imageView.setImageResource(R.drawable.tree_icon);
            }
            else {
                grid = (View) convertView;
            }

            return grid;
        }

        @Override
        public int getCount() {
            return Trees.size();
        }

        @Override
        public Object getItem(int position) {
            return Trees.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void addListenerOnAddTreeButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.fabAddTree);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(TreesViewActivity.this, TreeActivity.class);

                startActivity(intent);
            }
        });
    }
}

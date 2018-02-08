package com.example.talanath.seededroid2;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import interfaces.ISeed;
import models.BaseSeed;
import models.Garden;
import models.GardenSeed;
import models.ImageSeed;
import models.ThoughtSeed;
import models.User;
import utils.Constants;

public class MySeedsActivity extends AppCompatActivity {

    MySQLiteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_my_seeds);

            helper = new MySQLiteHelper(this);
            SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(MySeedsActivity.this);

            Long existing_token_id=Long.parseLong(pref.getString("token_id",null));
            User appUser = new User(existing_token_id);

            //What's the point casting to an ISeed now Base Seed isn't abstract?
            final List<BaseSeed> seeds = helper.GetBaseSeedsRecentByNumberAndUserId(100, appUser);
            ArrayAdapter<BaseSeed> adapter = new ArrayAdapter<BaseSeed>(this,
                    android.R.layout.simple_list_item_1, seeds);

            GridView gridView = (GridView) findViewById(R.id.grdSeeds);
            gridView.setAdapter(new MySeedsActivity.CustomGridSeedsAdapter(this, seeds));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // DO something
                    // start the SecondActivity
                    long gardenId = seeds.get(position).get_ID();
                    android.content.Intent intent = new android.content.Intent(parent.getContext(), GardenSeedsActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(gardenId));
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

    public class CustomGridSeedsAdapter extends BaseAdapter {

        private android.content.Context Context;
        private List<BaseSeed> Seeds;
        LayoutInflater Inflater;

        public CustomGridSeedsAdapter(android.content.Context context, List<BaseSeed> seeds) {
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

                BaseSeed bSeed = Seeds.get(position);
                models.SeedType seedType = bSeed.getSeedType();

                if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))
                {
                    imageView.setImageResource(R.drawable.seedicon_lightbulb);
                    imageView.setContentDescription(Constants.SEEDTYPE_THOUGHTSEED+"-"+bSeed.get_ID());
                }
                else {
                    imageView.setImageResource(R.drawable.seedicon_image);
                    imageView.setContentDescription(Constants.SEEDTYPE_IMAGE+"-"+bSeed.get_ID());
                }
                imageView.setOnTouchListener(new MySeedsActivity.ChoiceTouchListener(bSeed));
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

    private class ChoiceTouchListener implements View.OnTouchListener {

        BaseSeed BaseSeed;

        public ChoiceTouchListener(BaseSeed baseSeed) {
            BaseSeed = baseSeed;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {


                models.SeedType seedType = BaseSeed.getSeedType();
                long seedId = BaseSeed.get_ID();

                LinearLayout layout = (LinearLayout) findViewById(R.id.laySeedDisplay);
                layout.removeAllViews();


                 /* ROW 1 */
            LinearLayout laySeedType = new LinearLayout(MySeedsActivity.this);
            laySeedType.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams laySeedTypeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            laySeedTypeParams.weight = 2;
            laySeedType.setLayoutParams(laySeedTypeParams);

            LinearLayout.LayoutParams seedTypeFillLeftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            TextView txtSeedTypeFillLeft = new TextView(MySeedsActivity.this);
            seedTypeFillLeftParams.weight = 1;
            txtSeedTypeFillLeft.setLayoutParams(seedTypeFillLeftParams);

            LinearLayout.LayoutParams seedTypeParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            TextView txtSeedType = new TextView(MySeedsActivity.this);
            txtSeedType.setGravity(Gravity.CENTER);
            if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED)) {
                txtSeedType.setText("Ink Seed");
            }
            else if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE))
            {
                txtSeedType.setText("Image Seed");
            }

            seedTypeParams.weight = 2;
            txtSeedType.setLayoutParams(seedTypeParams);

            LinearLayout.LayoutParams seedTypeFillRightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            TextView txtSeedTypeFillRight = new TextView(MySeedsActivity.this);
            seedTypeFillRightParams.weight = 1;
            txtSeedTypeFillRight.setLayoutParams(seedTypeFillRightParams);

            laySeedType.addView(txtSeedTypeFillLeft);
            laySeedType.addView(txtSeedType);
            laySeedType.addView((txtSeedTypeFillRight));
            layout.addView(laySeedType);
            //txtSeedType.

                    /* ROW 2 */

            LinearLayout laySeed = new LinearLayout(MySeedsActivity.this);
            laySeed.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams laySeedParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, 0);
            laySeedParams.weight = 4;
            laySeed.setLayoutParams(laySeedParams);

            LinearLayout.LayoutParams seedFillLeftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            TextView txtSeedFillLeft = new TextView(MySeedsActivity.this);
            seedFillLeftParams.weight = 1;
            txtSeedFillLeft.setLayoutParams(seedFillLeftParams);
            laySeed.addView(txtSeedFillLeft);
//                            iParams.width = 0;
//                            iParams.weight = 1;
                if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED)) {

                    ThoughtSeed tSeed = null;
                    try
                    {
                    tSeed = helper.GetThoughtSeedByID(seedId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LinearLayout.LayoutParams seedParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    TextView txtSeedView = new TextView(MySeedsActivity.this);
                    txtSeedView.setPadding(20, 20, 20, 20);
                    txtSeedView.setText(tSeed.getThought());
                    seedParams.weight = 2;
                    txtSeedView.setLayoutParams(seedParams);
                    final int version = Build.VERSION.SDK_INT;
                    if (version >= 21) {
                        txtSeedView.setBackground(android.support.v4.content.ContextCompat.getDrawable(MySeedsActivity.this, R.drawable.border_custom));
                    } else {
                        txtSeedView.setBackground(getResources().getDrawable(R.drawable.border_custom));
                    }

                    laySeed.addView(txtSeedView);

                } else if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE)) {

                    LinearLayout.LayoutParams seedParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    ImageSeed iSeed = helper.GetImageSeedByID(seedId);
                    ImageView imgSeedView = new ImageView(MySeedsActivity.this);
                    imgSeedView.setPadding(20,20,20,20);
                    //ViewGroup.LayoutParams iParams = new ViewGroup.LayoutParams(android.view.ViewGroup);
                    //imgSeedView.setMaxHeight(75);
                    File imgFile = new File(iSeed.getImagefilelocation());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        imgSeedView.setImageBitmap(myBitmap);
                    }
                    seedParams.weight = 2;
                    imgSeedView.setLayoutParams(seedParams);
                    laySeed.addView(imgSeedView);
                }

                LinearLayout.LayoutParams seedFillRightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                TextView txtSeedFillRight = new TextView(MySeedsActivity.this);
                seedFillRightParams.weight = 1;
                txtSeedFillRight.setLayoutParams(seedFillRightParams);

                laySeed.addView((txtSeedFillRight));
                layout.addView(laySeed);

                /* ROW 3 */
                LinearLayout layStats = new LinearLayout(MySeedsActivity.this);
                layStats.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layStatsParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, 0);
                layStatsParams.weight = 2;
                layStats.setLayoutParams(layStatsParams);

                layout.addView(layStats);

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //setup drag
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    view.startDrag(data, shadowBuilder, view, 0);

                    return true;
                } else {
                    return false;
                }


        }
    }
}

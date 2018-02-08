package com.example.talanath.seededroid2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SceneRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_request);

        final LinearLayout layInk = (LinearLayout) findViewById(R.id.layRequestInk);
        final LinearLayout layImage = (LinearLayout) findViewById(R.id.layRequestImage);
        layInk.setVisibility(View.GONE);

        final ImageView imgImage = (ImageView)findViewById(R.id.imgImage);
        final ImageView imgInk = (ImageView) findViewById(R.id.imgInk);

        Drawable highlight = getResources().getDrawable( R.drawable.border_highlight);
        imgImage.setBackground(highlight);
        imgInk.setBackground(null);

        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable highlight = getResources().getDrawable( R.drawable.border_highlight);
                imgImage.setBackground(highlight);
                //
                //Do your other stuff here.
                imgInk.setBackground(null);
                layImage.setVisibility(View.VISIBLE);
                layInk.setVisibility(View.GONE);
                //
            }
        });

        imgInk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable highlight = getResources().getDrawable( R.drawable.border_highlight);
                imgInk.setBackground(highlight);
                //
                //Do your other stuff here.
                imgImage.setBackground(null);
                layInk.setVisibility(View.VISIBLE);
                layImage.setVisibility(View.GONE);
                //
            }
        });
    }

}

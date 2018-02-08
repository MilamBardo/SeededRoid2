package com.example.talanath.seededroid2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.Tree;
import models.TreeScene;
import utils.Constants;

public class SceneMakePhotoActivity extends AppCompatActivity {

    MySQLiteHelper database;
    Tree tree;
    TreeScene EditTreeScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_scene_make_photo);

            database = new MySQLiteHelper(this);
            Intent intent = getIntent();
            String treeIdString = intent.getStringExtra(Intent.EXTRA_TEXT);
            long treeId = Long.parseLong(treeIdString);
            tree = database.GetTreeByID(treeId);

            String treeSceneIdString = intent.getStringExtra(Constants.INTENT_EXTRA_TEXT_TREESCENEID);
            if (treeSceneIdString != null && !treeSceneIdString.isEmpty()) {
                EditTreeScene = tree.GetTreeSceneByID(Long.parseLong(treeSceneIdString));
            }

            android.widget.Button btnOpenCamera = (android.widget.Button) findViewById(R.id.btnOpenCamera);
            btnOpenCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.content.Intent intent = new android.content.Intent(SceneMakePhotoActivity.this, CaptureImageActivity2.class);
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }
}

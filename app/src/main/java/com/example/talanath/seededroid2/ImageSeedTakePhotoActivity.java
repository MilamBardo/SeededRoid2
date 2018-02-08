package com.example.talanath.seededroid2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.SeedFactory;
import models.SeedType;
import models.Tag;
import models.User;
import utils.Constants;

public class ImageSeedTakePhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    MySQLiteHelper database;

//    private SurfaceView cameraPreview;
//    private RelativeLayout overlay;

    private ImageView cameraPreview;
    private View overlay;
    private int cutheight;
    private int cutwidth;

    int bitmapWidth;
    int bitmapHeight;
    double ratio;
    int splitlength;

    boolean isLandscape = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_image_seed_take_photo);

            database = new MySQLiteHelper(this);

            Button btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);
            btnSavePhoto.setVisibility(View.INVISIBLE);

            Button btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
            btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });

            OnSaveButtonClicked();

        }
        catch (Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

//        // Get the preview size
//        int previewWidth = cameraPreview.getMeasuredWidth(),
//                previewHeight = cameraPreview.getMeasuredHeight();
//
//        // Set the height of the overlay so that it makes the preview a square
//        RelativeLayout.LayoutParams overlayParams = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
//        overlayParams.height = previewHeight - previewWidth;
//        overlay.setLayoutParams(overlayParams);

    }

    //Need to implement scroll feature on picture do that user can compose correctly.
    //Perhaps go straight to take photo, and then on returning after scroll select
    //to add tags, etc.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            Resources res=getResources();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            BitmapDrawable bDrawable = new BitmapDrawable(res, imageBitmap);

            ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
            imgPhoto.setImageBitmap(imageBitmap);
            Button btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);
            btnSavePhoto.setVisibility(View.VISIBLE);


            bitmapWidth = bDrawable.getIntrinsicWidth();
            bitmapHeight = bDrawable.getIntrinsicHeight();
            int imageLongside;
            if (bitmapWidth > bitmapHeight)
            {
                isLandscape = true;
                ratio = (double)bitmapWidth / bitmapHeight;
                imageLongside = imgPhoto.getWidth();
            }
            else
            {
                ratio = (double)bitmapHeight / bitmapWidth;
                imageLongside = imgPhoto.getHeight();
            }

            int imgLongside = imageLongside;
            long imgShortside = Math.round(imgLongside / ratio);
            int bottomLength = imgLongside - (int)imgShortside;
            splitlength = bottomLength / 2;

            int bottomstart = imgLongside - splitlength;
            cutheight = imgLongside - bottomLength;

            ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.mainConstraint);

            View viewtop = new View(this);
            viewtop.setBackgroundColor(Color.WHITE);
            viewtop.setId(View.generateViewId());

            View viewbottom = new View(this);
            viewbottom.setId(View.generateViewId());
//            android.view.ViewGroup.LayoutParams paramsb = viewbottom.getLayoutParams();
//            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)paramsb;
            viewbottom.setBackgroundColor(Color.WHITE);
            viewbottom.setContentDescription("Bottom overlay view");
            if (bitmapWidth > bitmapHeight)
            {
                viewtop.setLayoutParams(new LinearLayout.LayoutParams( splitlength, (int)imgLongside));
                layout.addView(viewtop);

                viewbottom.setLayoutParams(new ConstraintLayout.LayoutParams( splitlength, (int)imgLongside));
                android.view.ViewGroup.LayoutParams paramsb = viewbottom.getLayoutParams();
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)paramsb;

                lp.setMargins(bottomstart, 0, 0, 0);
                //notes = (TextView)findViewById(getResources().getIdentifier(VIEW_NAME, "id", getPackageName()));
                //lp.topToTop =
                lp.leftToRight = ConstraintLayout.LayoutParams.PARENT_ID;
//                lp.leftToRight = viewtop.getId();

                layout.addView(viewbottom);
            }
            else
            {
                viewtop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, splitlength));
                layout.addView(viewtop);

                viewbottom.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, splitlength));
                android.view.ViewGroup.LayoutParams paramsb = viewbottom.getLayoutParams();
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)paramsb;

                lp.setMargins(0, bottomstart, 0, 0);
                lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

                layout.addView(viewbottom);


            }
//            View viewbottom = findViewById(R.id.overlay);
//            android.view.ViewGroup.LayoutParams paramsb = viewbottom.getLayoutParams();
//            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)paramsb;
//            viewbottom.setBackgroundColor(Color.WHITE);
//
//            if (bitmapWidth > bitmapHeight)
//            {
//                viewtop.setLayoutParams(new LinearLayout.LayoutParams( splitlength, ViewGroup.LayoutParams.MATCH_PARENT));
//                layout.addView(viewtop);
//
//                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                lp.width = splitlength;
//                lp.setMargins(0, 0, bottomstart, 0);
//            }
//            else
//            {
//                viewtop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, splitlength));
//                layout.addView(viewtop);
//
//                lp.height = splitlength;
//                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                lp.setMargins(0, bottomstart, 0, 0);
//            }
        }
    }





    /*
    The following saves the image that has been selected and previewed.
    In order to get a scrollable selection so that users can better compose,
    we must implement a scrollable pane inside the preview bar which our
    overlay sits above.  So when users touch the scrollable, they can move
    preview box left and right.
    In order to save from this preview box, we just need to select the
    centre point of the current scroll view.
    Problems. Currently, only up and down will be possible. Not allowing
    pinching, etc.
     */
    public void OnSaveButtonClicked()
    {
        final services.ImageService imageService = new services.ImageService(this.getApplicationContext());

        Button btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);

        btnSavePhoto.setOnClickListener
            (new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
                    Bitmap bitmap =((BitmapDrawable)imgPhoto.getDrawable()).getBitmap();

                    try {
                        Bitmap cropped = processImage(bitmap);
                        //Resources res=getResources();
                        //BitmapDrawable bDrawable = new BitmapDrawable(res, bitmap);
                        //int bitmapWidth = bDrawable.getIntrinsicWidth();
                        //int bitmapHeight = bDrawable.getIntrinsicHeight();
                        //double ratio = (double)bitmapHeight / bitmapWidth;

                        SeedType seedTypeImage = new SeedType(Constants.SEEDTYPE_IMAGE, "SeedIcon_Image");

                        try {
                            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

                            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date today = Calendar.getInstance().getTime();
                            String reportDate = df.format(today);

//                        ActivityCompat.requestPermissions(ImageSeedTakePhotoActivity.this,
//                                new String[] {
//                                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                                },
//                                100);

                            Random randomGenerator = new Random();
                            int random = randomGenerator.nextInt(900000);
                            String imgname = reportDate + "-" + random + ".png";
                            File outputfile = null;
                            try {
                                int permissionCheck = ContextCompat.checkSelfPermission(ImageSeedTakePhotoActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                                outputfile
//                                        = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
                                    outputfile = new File(imageStorageLoc.getAbsolutePath(), imgname);
                                    if (!outputfile.exists()) {
                                        if (!outputfile.createNewFile()) {
                                            Log.i("Test", "This file is already exist: " + outputfile.getAbsolutePath());
                                        } else {
                                            FileOutputStream out = null;
                                            try {
                                                out = new FileOutputStream(outputfile.getAbsolutePath());
                                                cropped.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                                // PNG is a lossless format, the compression factor (100) is ignored
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    if (out != null) {
                                                        out.close();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            android.content.Intent intent = new android.content.Intent(ImageSeedTakePhotoActivity.this, HomeActivity.class);
                            startActivity(intent);

                            SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(ImageSeedTakePhotoActivity.this);

                            models.ImageSeed iSeed = SeedFactory.getInstance().CreateImageSeed(imgname, outputfile.getCanonicalPath(), ImageSeedTakePhotoActivity.this);

                            //get tags
                            EditText tagsText = (EditText) findViewById(R.id.txtTags);
                            String tagsString = tagsText.getText().toString();

                            String[] tags = tagsString.split(" ");
                            for (int i = 0; i < tags.length; i++)
                            {
                                Tag t = new Tag(tags[i]);
                                iSeed.AddSeedTag(t);
                            }

                            iSeed = database.SaveImageSeed(iSeed);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        );

    }

    // private Bitmap processImage(byte[] data) throws IOException {
    private Bitmap processImage(Bitmap bitmap) throws IOException {
        // Determine the width/height of the image

        // Load the bitmap from the byte array
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Rotate and crop the image into a square
//        int croppedWidth = (width > height) ? height : width;
//        int croppedHeight = (width > height) ? height : width;

        Matrix matrix = new Matrix();
        //matrix.postRotate(IMAGE_ORIENTATION);
        Bitmap cropped;
        if (isLandscape) {
            int difference = bitmapWidth - bitmapHeight;
            int startpoint = difference / 2;
            cropped = Bitmap.createBitmap(bitmap, startpoint, 0, bitmapHeight, bitmapHeight, matrix, true);

        }
        else {
            int difference = bitmapHeight-bitmapWidth;
            int startpoint = difference / 2;
            cropped = Bitmap.createBitmap(bitmap, 0, startpoint, bitmapWidth, bitmapWidth, matrix, true);

        }
        bitmap.recycle();
        // Scale down to the output size
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE, true);
        //cropped.recycle();

        //return scaledBitmap;
        return cropped;
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        || grantResults[0] == PackageManager.PERMISSION_GRANTED
                        || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    /* User checks permission. */

                } else {
                    Toast.makeText(ImageSeedTakePhotoActivity.this, "Permission is denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //return; // delete.
        }
    }


}

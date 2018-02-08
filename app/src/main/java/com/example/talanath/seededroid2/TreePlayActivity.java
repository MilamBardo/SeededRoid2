package com.example.talanath.seededroid2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import 	android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import ExceptionHandler.ExceptionHandler;

import db.MySQLiteHelper;
import db.SeedenContract;
import models.ImageSeed;
import models.Tree;
import models.TreeScene;

public class TreePlayActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    MySQLiteHelper helper;

    android.widget.TextView txtSubtitles;
    android.widget.ImageView imgDisplay;

    String treeIdString;

    TreeScene scene;
    int milliseconddelay = 0;
    int index = 0;
    int maxidx = 0;
    boolean scenerunning = true;
    List<String> pages;

    TextToSpeech tospeech;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {
            setContentView(R.layout.activity_tree_play);

            helper = new MySQLiteHelper(this);

            Intent intent = getIntent();
            treeIdString = intent.getStringExtra(Intent.EXTRA_TEXT);

            txtSubtitles = (android.widget.TextView) findViewById(R.id.txtSubtitles);
            imgDisplay = (android.widget.ImageView) findViewById(R.id.imgDisplay);
            //Handler handler = new Handler();
            //handler.postDelayed(runnable, 100);

            tospeech = new TextToSpeech(this, this);
//            tospeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    if(status != TextToSpeech.ERROR) {
//                        tospeech.setLanguage(Locale.UK);
//                    }
//                }
//            });
//            if (treeIdString != null && (!treeIdString.trim().isEmpty())) {
//                long treeId = Long.parseLong(treeIdString);
//                Tree tree = helper.GetTreeByID(treeId);
//
//                List<TreeScene> treescenes = tree.getTreeScenes();
//
//                Tree[] trees = new Tree[1];
//                trees[0] = tree;
//                //AsyncTask<Tree, TreeScenePage, Long> asyncScene = new AsyncRunScene().execute(tree);
//                new AsyncRunScene().execute(tree);
//            }
        } catch (Exception ex) {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tospeech.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    tospeech.speak("I am a dog",TextToSpeech.QUEUE_FLUSH,null,null);
//                } else {
//                    tospeech.speak("I am a dog", TextToSpeech.QUEUE_FLUSH, null);
//                }
                if (treeIdString != null && (!treeIdString.trim().isEmpty())) {
                    long treeId = Long.parseLong(treeIdString);
                    Tree tree = null;
                    try {
                        tree = helper.GetTreeByID(treeId);
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    List<TreeScene> treescenes = tree.getTreeScenes();

                    Tree[] trees = new Tree[1];
                    trees[0] = tree;
                    //AsyncTask<Tree, TreeScenePage, Long> asyncScene = new AsyncRunScene().execute(tree);
                    new AsyncRunScene().execute(tree);
                }
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private class AsyncRunScene extends AsyncTask<Tree, TreeScenePage, Long> {
        protected Long doInBackground(Tree... roots) {
            Log.i("ASYNC", "doInBackground");
            //Iterate through scenes
            Tree tree = roots[0];
            List<TreeScene> treescenes = tree.getTreeScenes();
            for (int i = 0; i < treescenes.size(); i++) {
                TreeScene scene = treescenes.get(i);
                if (scene.getThoughtSeed() != null) {
                    if (tospeech != null)
                    {
                        tospeech.stop();
                    }
                    String th = scene.getThoughtSeed().getThought();
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {

                    }
                    else
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tospeech.speak(th,TextToSpeech.QUEUE_FLUSH,null,null);
                        } else {
                            tospeech.speak(th, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    List<String> pages = SplitTextAlgorithm(scene.getThoughtSeed().getThought());
                    Log.i("doInBackground", "pages length="+pages.size());
                    for (int j = 0; j < pages.size(); j++) {
                        Log.i("doInBackground", "pageiteration="+j);
                        TreeScenePage page = new TreeScenePage(scene.getImageSeed().getImagefilelocation(), pages.get(j) );
                        Log.i("doInBackground", "pagetext="+pages.get(j));
                        publishProgress(page);
                        int durationmillisecs = (scene.getSceneDuration() / pages.size())*1000;
                        try {
                            Thread.sleep(durationmillisecs);
                            Log.i("doInBackground", "slept in between pages");
                        }
                        catch(InterruptedException ex)
                        {
                            //IGNORE? Log?
                            Log.i("INTERRUPT", "Interruptexception in asyncRunPage");
                        }
                    }
                }
                else
                {
                    Log.i("doInBackground", "scene thoughtseed was empty");
                    //This scene is just an image
                    TreeScenePage page = new TreeScenePage(scene.getImageSeed().getImagefilelocation(), "" );
                    publishProgress(page);
                    int durationmillisecs = (scene.getSceneDuration())*1000;
                    try {
                        Thread.sleep(durationmillisecs);
                    }
                    catch(InterruptedException ex)
                    {
                        //IGNORE? Log?
                        Log.i("INTERRUPT", "Interruptexception in asyncRunPage");
                    }
                }
            }

            long finished = 303;
            return finished;
        }


        protected void onProgressUpdate(TreeScenePage... pages) {
            //setProgressPercent(progress[0]);
            TreeScenePage page = pages[0];
            File imgFile = new File(page.getImagefilelocation());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgDisplay.setImageBitmap(myBitmap);
            }
            txtSubtitles.setText(page.PageText);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
            Log.i("ASYNC", "Post execture" + Long.toString(result));
        }

        private List<String> SplitTextAlgorithm(String str) {
            //ratio 0.18 for pixel size

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenwidth = size.x;
            int screenheight = size.y;

            //ong pixelsize = Math.round(screenwidth/0.18);
            long pixelsize = 80;

            Paint paint = new Paint();
            paint.setTextSize(pixelsize);
            //paint.setFakeBoldText(true);
            //Typeface typeface = Typeface.createFromAsset(getAssets(), "Helvetica.ttf");
            Typeface typeface = Typeface.SANS_SERIF;
            paint.setTypeface(typeface);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            Rect result = new Rect();
            paint.getTextBounds(str, 0, str.length(), result);

            int textwidth = result.width();

            int linesoftexttodisplay = 2;

            int numberofsplits = (int) Math.ceil(textwidth / screenwidth / linesoftexttodisplay);

            int strcharlength = str.length();
            //String[] textarray = new String[numberofsplits];

            ArrayList<String> textarray = new ArrayList<>();

            //int increasedcharactercount = 0;
            //int splitpointreached = 0;
            int start = 0;

            int splitlength = 0;
            if (numberofsplits != 0) {
                splitlength = Math.round(strcharlength / numberofsplits);
            }
            //int stop = (strcharlength < splitlength) ? strcharlength : splitlength ;

            StringBuilder page = new StringBuilder();
            StringBuilder currentword = new StringBuilder();

            for (int i = start; (i < strcharlength); i++) {
                if (i > 0 && i == splitlength) {
                    if ( (str.charAt(i) != ' ' && str.charAt(i) != '-') ) {
                        currentword.append(str.charAt(i));
                    }
                    if (currentword.length() > 0) {
                        //midword, so pass to next screen
                        i -= currentword.length();
                        currentword.setLength(0);
                    }
                    splitlength = i + splitlength;
                    textarray.add(page.toString());
                    page.setLength(0);
                } else {
                    if (i == strcharlength - 1) {
                        currentword.append(str.charAt(i));
                        page.append(currentword);
                        textarray.add(page.toString());
                        page.setLength(0);
                    } else if (currentword.toString().trim().length() > 0 && (str.charAt(i) == ' ' || str.charAt(i) == '-')) {
                        currentword.append(str.charAt(i));
                        page.append(currentword);
                        currentword.setLength(0);
                    } else {
                        currentword.append(str.charAt(i));
                    }
                }
            }
            return textarray;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tospeech != null)
        {
            tospeech.stop();
            tospeech.shutdown();
        }
    }

    private class TreeScenePage {
        private String Imagefilelocation = null;
        private String PageText = null;

        public TreeScenePage(String imagefilelocation, String pageText) {
            Imagefilelocation = imagefilelocation;
            PageText = pageText;
        }

        public String getImagefilelocation() {
            return Imagefilelocation;
        }

        public String getPageText() {
            return PageText;
        }
    }


//        for (int j = 0; j < numberofsplits+1; j++) {
//
//            StringBuilder page = new StringBuilder();
//            StringBuilder currentword = new StringBuilder();
//            int startindexofcurrentword = 0;
//
//            /*
//            * Start to Stop
//            * Stop becomes start
//            * On Stop, if mid word, pass word to next screen by taking start position back by same number of characters
//            * Continue till all splits added.
//            * */
//            for (int i = start; (i < strcharlength) || (i < stop + 1); i++) {
//                if (currentword.toString().trim().length() > 0 && (str.charAt(i) == ' ' || str.charAt(i)=='-') )
//                {
//                    currentword.append(str.charAt(i));
//                    page.append(currentword);
//                    currentword.setLength(0);
//                }
//                else
//                {
//                    if (currentword.length() ==0)
//                    {
//                        startindexofcurrentword = i;
//                    }
//                    currentword.append(str.charAt(i));
//                }
//            }
//            start = stop;
//            stop += splitlength;
//            if (currentword.length() > 0)
//            {
//                //midword, so pass to next screen
//                start -= currentword.length();
//                stop -= currentword.length();
//                increasedcharactercount += currentword.length();
//                /* if enough characters have been pushed onto the next page
//                * then add another page. */
//                if (increasedcharactercount > splitpointreached) {
//                    numberofsplits++;
//                    splitpointreached += splitlength;
//                }
//            }
//            textarray.add(page.toString());
//        }


}


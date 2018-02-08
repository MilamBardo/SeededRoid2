package services;

/**
 * Created by Talanath on 5/16/2017.
 */
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import 	java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

public class ImageService {

    android.content.Context c;
    public ImageService(android.content.Context context) {
        c = context;
    }

    public void UploadedImage(String imagePath)
    {
            //save to storage
            
            //save to db

    }

    public void SaveImage(InputStream istream, File destination) throws Exception {
        try {
            //image to save
            if (isExternalStorageWritable()) {

                OutputStream output = new FileOutputStream(destination);
                try {
                    try {

                        int size = istream.available();
                        byte[] buffer = new byte[size];
                        istream.read(buffer);
                        istream.close();

                        FileOutputStream fos = new FileOutputStream(destination);
                        fos.write(buffer);
                        fos.close();
                    } finally {
                        output.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // handle exception, define IOException and others
                    throw e;
                }
            }
        } finally{
        istream.close();
    }

    }

//    public void SaveImage(InputStream istream, File destination) throws Exception {
//        try {
//            //image to save
//            if (isExternalStorageWritable()) {
//
//                OutputStream output = new FileOutputStream(destination);
//                try {
//                    try {
//                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
//                        int read;
//
//                        while ((read = istream.read(buffer)) != -1) {
//                            output.write(buffer, 0, read);
//                        }
//                        output.flush();
//                    } finally {
//                        output.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace(); // handle exception, define IOException and others
//                }
//            }
//        } finally{
//            istream.close();
//        }
//
//    }


    public File getImageStorageDir(String imgDirName) throws Exception{
        // Get the directory for the user's public pictures directory.
        //StorageManager manager = new St
        String packagename = c.getPackageName();
        if (packagename.equalsIgnoreCase("com.example.talanath.seededroid2")) {
//            File file = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), imgDirName);
            File file = new File(Environment.getExternalStorageDirectory() + "/"+imgDirName);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e("ERROR", "Directory not created");
                    throw new Exception("mkdirs failed on getImageStorage");
                    //throw new Exception("File not created");
                }
                else
                {
                    return file;
                }
            }
            return file;
        }
        else
        {
            return null;
        }
    }

    public File createImageImageFile(String imgDirName, String fileName) throws Exception{
        // Get the directory for the user's public pictures directory.

        File filedirectory = new File(imgDirName);
        if (!filedirectory.exists()) {
            //if (!file.createNewFile()) {
            if (!filedirectory.mkdirs()) {
                Log.e("ERROR", "Directory not created");
                throw new Exception("File not created");
            }
        }

        File file = new File(filedirectory.getAbsolutePath(), fileName);

        if (!file.exists()) {
            //if (!file.createNewFile()) {
            if (!file.createNewFile()) {
//                Log.e("ERROR", "Directory not created");
//                throw new Exception("File not created");
                Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
            }
        }
        return file;
    }

    public void copyImage(File source, File destination) throws Exception
    {
        if (source.exists()) {
            FileChannel src = new FileInputStream(source).getChannel();
            FileChannel dst = new FileOutputStream(destination).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    //needs to be public for access by test class
    public String SaveToInternalStorage(android.graphics.Bitmap bitmapImage){
        android.content.ContextWrapper cw = new android.content.ContextWrapper(this.c);
        // path to /data/data/yourapp/app_data/imageDir
        java.io.File directory = cw.getDir("imageDir", android.content.Context.MODE_PRIVATE);
        // Create imageDir
        java.io.File mypath=new java.io.File(directory,"profile.jpg");

        java.io.FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}

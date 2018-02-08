package ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import db.MySQLiteHelper;
import models.ExceptionError;

public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultHandler;
    private final Activity activity;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        activity = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        Log.e("CRASH", exception.getStackTrace().toString());
        Log.e("CRASH", errorReport.toString());

        ExceptionError error = new ExceptionError(errorReport.toString());

        MySQLiteHelper helper = new MySQLiteHelper(activity.getApplicationContext());

        error = helper.SaveExceptionError(error);

        defaultHandler.uncaughtException(thread, exception);

        //Intent intent = new Intent(activity, com.example.talanath.seededroid2.ErrorHandlerActivity.class);
        //Intent intent = new Intent(myContext, myContext.getClass());
        //intent.putExtra("error", errorReport.toString());
        //activity.startActivity(intent);

        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(10);
    }

    public void HandleExceptionGracefully(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);


        Log.e("CRASH", exception.getStackTrace().toString());
        Log.e("CRASH", errorReport.toString());

        ExceptionError error = new ExceptionError(errorReport.toString());

        MySQLiteHelper helper = new MySQLiteHelper(activity.getApplicationContext());

        error = helper.SaveExceptionError(error);

        helper.close();

        Intent intent = new Intent(activity, com.example.talanath.seededroid2.ErrorHandlerActivity.class);
        //Intent intent = new Intent(activity, activity.getClass());
        long errorid = error.get_ID();
        intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(errorid));
        activity.startActivity(intent);

        //defaultHandler.uncaughtException(thread, exception);

        //return error;
    }

}


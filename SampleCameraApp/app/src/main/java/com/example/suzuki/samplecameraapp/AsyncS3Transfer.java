package com.example.suzuki.samplecameraapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

/**
 * Created by suzuki on 17/07/23.
 */

public class AsyncS3Transfer extends AsyncTask<Uri.Builder, Void, String> {
    private Activity mainActivity;
    private AmazonS3 s3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    /*public AsyncHttpRequest(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }*/
    public AsyncS3Transfer(Context context){
        if (s3Client == null) {
            s3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            s3Client.setRegion(Region.getRegion(Regions.fromName(Constants.BUCKET_REGION)));
        }
    }
    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    Constants.COGNITO_POOL_ID,
                    Regions.fromName(Constants.COGNITO_POOL_REGION));
        }
        return sCredProvider;
    }

    // ここが非同期で処理される部分
    @Override
    protected String doInBackground(Uri.Builder... builder) {
        String existingBucketName  = Constants.BUCKET_NAME;//"*** Provide-Your-Existing-BucketName ***";
        String filePath            = Constants.SAMPLE_FILE;//"*** Provide-File-Path ***";

        File file = new File(filePath);
        String keyName             = Constants.FOLDER_PATH + file.getName();//"*** Provide-Key-Name ***";

        s3Client.putObject(new PutObjectRequest(existingBucketName, keyName, file));
        return null;
    }

    //非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        //
    }
}

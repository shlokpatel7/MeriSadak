package com.example.abhinav.merisadak2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddFeedActivity extends AppCompatActivity {
    private static String isRoad;
    ImageView imageView;
    Button postBtn;
    File file;
    private static final int SELECT_PICTURE = 1;
    static String TAG = "TAG";
    Uri uri;
    Button tickBtn;
    boolean picUploaded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        imageView = findViewById(R.id.image_add);
        imageView.setClickable(true);
        tickBtn=findViewById(R.id.tick_button);
        postBtn = findViewById(R.id.post_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImage();
            }
        });

        tickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getRealPathFromUri(getApplicationContext(), uri));
                Toast.makeText(AddFeedActivity.this, "Image upload : " + uploadImage(file), Toast.LENGTH_SHORT).show();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picUploaded){
                    Toast.makeText(AddFeedActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddFeedActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(AddFeedActivity.this, "Verify the picture", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public String uploadImage(File file) {

        try {

            Log.e(TAG, "Image Path: " + file.getPath());
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            final RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "profile.png", RequestBody.create(MEDIA_TYPE_PNG, file))
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.43.209:2001/leaf")
                    .post(req)
                    .build();

            Log.e(TAG, "Response before");
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String resp = response.body().string();
                    Log.e(TAG, "onResponse: " + resp);
                    if (resp.equals("Road")) {
                        AddFeedActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddFeedActivity.this, "This is a " + resp, Toast.LENGTH_LONG).show();
                                tickBtn.setBackgroundColor(Color.parseColor("#008000"));
                                picUploaded=true;
                            }
                        });
                    }else{
                        picUploaded=false;
                        GetImage();
                        AddFeedActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddFeedActivity.this, "Select a proper road image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            });
            Log.e(TAG, "Response After");

            return "DONE";


        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getMessage());
        }
        return null;
    }

    void GetImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
}

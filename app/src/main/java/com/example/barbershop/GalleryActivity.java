package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershop.adapters.GalleryAdapter;
import com.example.barbershop.items.GalleryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/images/getImages.php";
    private GridView gridView;
    private GalleryAdapter adapter;
    private List<GalleryItem> galleryItemList;
    private ImageView backBtn;
    private int shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        shopID = getIntent().getExtras().getInt("barbershopID");
        SQL_URL += "?barbershopID=" + shopID;

        galleryItemList = new ArrayList<>();

        backBtn = findViewById(R.id.gallery_backButton);
        backBtn.setOnClickListener(backBtnListener);

        gridView = findViewById(R.id.galleryGridView);

        loadImages();
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private void loadImages(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("php", response);
                try {
                    JSONArray images = new JSONArray(response);
                    for (int i = 0 ; i < images.length() ; i++){
                        JSONObject ImageObject = images.getJSONObject(i);
                        int imageID = ImageObject.getInt("ImageID");
                        String image = ImageObject.getString("Image");
                        GalleryItem galleryItem = new GalleryItem(imageID,image);
                        galleryItemList.add(galleryItem);
                    }
                    adapter = new GalleryAdapter(getApplicationContext(),galleryItemList);
                    gridView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Log.d("php", "onErrorResponse: " + error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
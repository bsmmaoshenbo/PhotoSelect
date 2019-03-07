package com.dijkstra.photoselect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dijkstra.photoselect.model.PhotoDetailInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PicPickUpUtil.showPickUpDialog(null, 10, new PicPickUpUtil.PicPickCallBack() {
            @Override
            public void onPickPicFromAlbum(ArrayList<PhotoDetailInfo> pictures) {
                Toast.makeText(MainActivity.this, "这里是选择的图片", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTakePhotoResult(PhotoDetailInfo picture) {

            }
        });
    }
}

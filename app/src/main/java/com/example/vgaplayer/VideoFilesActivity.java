package com.example.vgaplayer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoFilesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<MediaFiles> videoFilesArrayList = new ArrayList<>();
    VideoFilesAdapter videoFilesAdapter;
    String folder_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_files);
        folder_name = getIntent().getStringExtra("folderName");
        getSupportActionBar().setTitle(folder_name);
        recyclerView = findViewById(R.id.videos_rv);
        showVideoFiles();





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showVideoFiles() {
        videoFilesArrayList = fetchMedia(folder_name);
        videoFilesAdapter = new VideoFilesAdapter(videoFilesArrayList, this);
        recyclerView.setAdapter(videoFilesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false));
        videoFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<MediaFiles> fetchMedia(String folderName) {
        ArrayList<MediaFiles> videoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " Like?";
        String[] selectionArg = new String[]{"%" + folderName + "%"};
        Cursor cursor = getContentResolver().query(uri, null,
                selection, selectionArg, null);
        if(cursor!=null && cursor.moveToNext()){
            do {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dataAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                MediaFiles mediaFiles = new MediaFiles(id, title, displayName, size, duration, path, dataAdded);

                videoFiles.add(mediaFiles);

            } while (cursor.moveToNext());
        }
        return videoFiles;
    }
}
package com.example.mymusicplayer;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    MusicService mService;
    boolean mBound = false;

    private SongsAdapter songsAdapter;
    private ArrayList<Song> songs;
    private RecyclerView recyclerView;
    private FloatingActionButton addBtn;

    private ImageView dialog_cover_iv;
    private File photoPath;
    private Uri photoPathUri;

    public static final int CAMERA_REQUEST = 1;
    public static final int CAMERA_PICK_FROM_GALLERY = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Log.d("state", "on create main now");


        addBtn = findViewById(R.id.add_Btn);

        recyclerView = findViewById(R.id.recycler_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        File fileSongs = getFileStreamPath("songs.dat");
        if (fileSongs.exists()) {
            readSongsFromFile();
        } else {
            initFirstRunSongs();
        }


        songsAdapter = new SongsAdapter(this, songs);
        recyclerView.setAdapter(songsAdapter);

    }


    @Override
    protected void onStart() {
//        Intent intent = new Intent(this,MusicService.class);
//        bindService(intent,connection, Context.BIND_AUTO_CREATE);

        songsAdapter.setListener(new SongsAdapter.MySongListener() {
            @Override
            public void OnSongClicked(View view, int position) {

                RelativeLayout layout = view.findViewById(R.id.content_layout);

                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                Intent service = new Intent(MainActivity.this, MusicService.class);
                Song song = songs.get(position);
                service.putExtra("position", position);
                service.putExtra("songs_list", songs);
                service.putExtra("command", "new_instance");

                intent.putExtra("position", position);
                intent.putExtra("songs_list", songs);

                startService(service);
                Pair pair = new Pair<View, String>(view.findViewById(R.id.song_cover_iv), "coverTrans");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pair);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pos_drag = viewHolder.getAdapterPosition();
                int pos_target = target.getAdapterPosition();
                Collections.swap(songs, pos_drag, pos_target);
                songsAdapter.notifyItemMoved(pos_drag, pos_target);

                saveSongsToFile();
                Intent service = new Intent(MainActivity.this, MusicService.class);
                service.putExtra("songs_list", songs);
                service.putExtra("command", "songs_update");
                startService(service);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Song song = (Song) songs.get(viewHolder.getAdapterPosition());
                final int lastPos = viewHolder.getAdapterPosition();
                songs.remove(viewHolder.getAdapterPosition());
                songsAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar.make(recyclerView, "Song Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                songs.add(lastPos, song);
                                songsAdapter.notifyItemInserted(lastPos);
                            }
                        }).show();
                saveSongsToFile();
                Intent service = new Intent(MainActivity.this, MusicService.class);
                service.putExtra("songs_list", songs);
                service.putExtra("command", "songs_update");
                startService(service);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Log.d("state", "on start main now");
        super.onStart();
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d("state", "inside service connected!");
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            mService.setAdapter(songsAdapter);

            unbindService(connection);


        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("state", "inside service disConnected!");
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
//        unbindService(connection);
        mBound = false;

        Log.d("state", "on stop main now");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("state", "on destroy main now");
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoPath = new File(Environment.getExternalStorageDirectory(), "song" + (songs != null ? songs.size() : 0) + ".jpg");
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoPath));
                startActivityForResult(takePicture, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAMERA_PICK_FROM_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPicture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPicture, CAMERA_PICK_FROM_GALLERY);
            } else {
                Toast.makeText(this, "External storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            dialog_cover_iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            dialog_cover_iv.setVisibility(View.VISIBLE);
            dialog_cover_iv.setTag(String.valueOf(Uri.fromFile(photoPath)));
            dialog_cover_iv.setImageBitmap(BitmapFactory.decodeFile(photoPath.getAbsolutePath()));
        }
        if (requestCode == CAMERA_PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            dialog_cover_iv.setVisibility(View.VISIBLE);
            dialog_cover_iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            dialog_cover_iv.setTag(data.getData());
            Glide.with(this).load(data.getData()).into(dialog_cover_iv);
        }
    }

    private void readSongsFromFile() {
        try {
            FileInputStream fileInputStream = openFileInput("songs.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            songs = (ArrayList<Song>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSongsToFile() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("songs.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(songs);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initFirstRunSongs() {
        songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://www.mboxdrive.com/The%20Weeknd%20-%20Blinding%20Lights.mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://www.mboxdrive.com/The%20Weeknd%20-%20Blinding%20Lights.mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://www.mboxdrive.com/The%20Weeknd%20-%20Blinding%20Lights.mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://www.mboxdrive.com/The%20Weeknd%20-%20Blinding%20Lights.mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        saveSongsToFile();
    }



    public void showAlertDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_Dialog_Alert);
        View view = getLayoutInflater().inflate(R.layout.add_song_dialog, null);

        Button cancel = view.findViewById(R.id.cancel_button_dialog);
        Button add = view.findViewById(R.id.add_btn_dialog);
        ImageButton addPhoto = view.findViewById(R.id.add_photo_dialog);
        ImageButton pickPhoto = view.findViewById(R.id.add_photo_alternate_dialog);
        final TextInputEditText songName_et = view.findViewById(R.id.song_name_dialog);
        final TextInputEditText songAuthor_et = view.findViewById(R.id.song_author_dialog);
        final TextInputEditText songLink_et = view.findViewById(R.id.song_link_dialog);
        dialog_cover_iv = view.findViewById(R.id.cover_iv_dialog);

        photoPathUri = null;
        photoPath = null;

        final AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(view);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(params);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songName = songName_et.getText().toString();
                String songAuthor = songAuthor_et.getText().toString();
                String songLink = songLink_et.getText().toString();
//                Song newSong ;
                songs.add(new Song(songName, songAuthor, String.valueOf(dialog_cover_iv.getTag()), songLink));
                songsAdapter.notifyItemInserted(songs.size() - 1);

                Intent service = new Intent(MainActivity.this, MusicService.class);
                service.putExtra("songs_list", songs);
                service.putExtra("command", "songs_update");
                startService(service);

                saveSongsToFile();
                dialog.dismiss();

            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    } else {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoPath = new File(Environment.getExternalStorageDirectory(), "song" + (songs != null ? songs.size() : 0) + ".jpg");
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoPath));
                        startActivityForResult(takePicture, CAMERA_REQUEST);
                    }
                }
            }

        });

        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PICK_FROM_GALLERY);
                    } else {
                        Intent pickPicture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPicture, CAMERA_PICK_FROM_GALLERY);
                    }
                }
            }
        });
    }


}
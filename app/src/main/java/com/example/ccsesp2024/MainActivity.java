package com.example.ccsesp2024;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addButton = findViewById(R.id.addPostButton);

        LinearLayout postsLayout = findViewById(R.id.postsLayout);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), imageURI -> {
                    final Context CONTEXT = getApplicationContext();
                    final int ICON_SIZE = 125;

                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (imageURI != null) {
                        Log.d("PhotoPicker", "Selected URI: " + imageURI);
                        ImageView newPost = new ImageView(CONTEXT);
                        newPost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        newPost.setImageURI(imageURI);
                        newPost.setAdjustViewBounds(true);
                        newPost.setMaxWidth(2000);
                        newPost.setMaxHeight(2000);

                        LinearLayout buttonLayout = new LinearLayout(CONTEXT);
                        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                        buttonLayout.setTop(newPost.getBottom());
                        buttonLayout.setPadding(0, 0, 0, 200);

                        ImageButton likeButton = new ImageButton(CONTEXT);
                        likeButton.setImageResource(R.drawable.heart);
                        likeButton.setColorFilter(Color.WHITE);
                        likeButton.setBackgroundColor(Color.TRANSPARENT);
                        likeButton.setAdjustViewBounds(true);
                        likeButton.setMaxWidth(ICON_SIZE);
                        likeButton.setMaxHeight(ICON_SIZE);
                        likeButton.setClickable(true);
                        likeButton.setOnClickListener(view -> {
                            ImageButton thisButton = (ImageButton) view;
                            Log.d("", String.format("button state: %s", thisButton.isActivated()));

                            if (thisButton.isActivated()) {
                                likeButton.setColorFilter(Color.WHITE);
                                likeButton.setImageResource(R.drawable.heart);
                                thisButton.setActivated(false);
                            } else {
                                likeButton.setImageResource(R.drawable.heart_filled);
                                likeButton.setColorFilter(Color.RED);
                                thisButton.setActivated(true);
                            }
                        });

                        ImageButton shareButton = new ImageButton(CONTEXT);
                        shareButton.setImageResource(android.R.drawable.ic_menu_share);
                        shareButton.setColorFilter(Color.WHITE);
                        shareButton.setBackgroundColor(Color.TRANSPARENT);
                        shareButton.setAdjustViewBounds(true);
                        shareButton.setMaxWidth(ICON_SIZE);
                        shareButton.setMaxHeight(ICON_SIZE);
                        shareButton.setOnClickListener(v -> {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageURI);
                            shareIntent.setType("image/png");
                            startActivity(Intent.createChooser(shareIntent, null));
                        });

                        buttonLayout.addView(likeButton);
                        buttonLayout.addView(shareButton);
                        likeButton.getLayoutParams().width = ICON_SIZE;
                        shareButton.getLayoutParams().width = ICON_SIZE;

                        postsLayout.addView(newPost);
                        postsLayout.addView(buttonLayout);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        addButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSettings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

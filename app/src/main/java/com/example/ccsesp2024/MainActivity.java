package com.example.ccsesp2024;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button settingsButton = findViewById(R.id.settingsButton);
        Button homeButton = findViewById(R.id.homeButton); // for funsies ig
        Button addButton = findViewById(R.id.addButton);

        LinearLayout postsLayout = findViewById(R.id.postsLayout);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    final Context CONTEXT = getApplicationContext();
                    final int ICON_SIZE = 125;

                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        ImageView newPost = new ImageView(CONTEXT);
                        newPost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        newPost.setImageURI(uri);
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
                        likeButton.setAdjustViewBounds(true);
                        likeButton.setMaxWidth(ICON_SIZE);
                        likeButton.setMaxHeight(ICON_SIZE);
                        likeButton.setBackgroundColor(Color.TRANSPARENT);
                        likeButton.setClickable(true);
                        likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ImageButton thisButton = (ImageButton) view;
                                Log.d("", String.format("button state: %s", thisButton.isActivated()));

                                if (thisButton.isActivated()) {
                                    likeButton.setImageResource(R.drawable.heart_filled);
                                    likeButton.setColorFilter(Color.RED);
                                } else {
                                    likeButton.setImageResource(R.drawable.heart);
                                    likeButton.setColorFilter(Color.WHITE);
                                }
                            }
                        });

                        ImageButton shareButton = new ImageButton(CONTEXT);
                        shareButton.setImageResource(android.R.drawable.ic_menu_share);
                        shareButton.setAdjustViewBounds(true);
                        shareButton.setMaxWidth(ICON_SIZE);
                        shareButton.setMaxHeight(ICON_SIZE);

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
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }
}
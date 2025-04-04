package my.edu.utar.individualassignment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

/**
 * This activity handles the main menu of the application.
 * It plays background music, allows the user to toggle mute, and provides buttons to navigate to
 * other activities.
 * A GIF is displayed on the main screen, and sound effects are triggered upon user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private MediaPlayer bgMediaPlayer;
    private MediaPlayer clickMediaPlayer;
    private boolean isMuted = false; // Boolean flag to track if the music is muted
    private ImageButton muteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ImageView and load the GIF using Glide library
        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.babyshark)
                .override(300, 300) // Set width & height in pixels
                .into(gifImageView);

        // Initialize the mute button
        muteButton = findViewById(R.id.btnMute);

        // Play background music
        playBackgroundMusic(R.raw.babyshark);
    }

    /**
     * Starts playing background music in a loop.
     * If there is an existing MediaPlayer, it is released before creating a new one.
     *
     * @param soundResource The resource ID of the sound to play.
     */
    private void playBackgroundMusic(int soundResource) {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.release(); // Release existing media player
        }
        bgMediaPlayer = MediaPlayer.create(this, soundResource);
        bgMediaPlayer.setLooping(true); // Ensure music loops indefinitely
        bgMediaPlayer.start();
    }

    /**
     * Plays a short click sound when triggered by the user interacting with the UI.
     * Releases the MediaPlayer after the sound finishes playing to free resources.
     */
    private void playClickSound() {
        if (clickMediaPlayer != null) {
            clickMediaPlayer.release();
        }
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click);
        clickMediaPlayer.start();

        // Release click sound after it finishes playing
        clickMediaPlayer.setOnCompletionListener(mp -> {
            clickMediaPlayer.release();
            clickMediaPlayer = null;
        });
    }

    /**
     * Toggles between muting and unmuting the background music.
     * When muted, the volume is set to 0, and when unmuted, it is set to full volume.
     * The button image is also updated accordingly.
     *
     * @param view The view that triggered this method (the mute/unmute button).
     */
    public void toggleMute(View view) {
        if (bgMediaPlayer != null) {
            if (isMuted) {
                bgMediaPlayer.setVolume(1.0f, 1.0f); // Full volume
                muteButton.setImageResource(R.drawable.unmute); // Change to unmute image
            } else {
                bgMediaPlayer.setVolume(0.0f, 0.0f); // Mute volume
                muteButton.setImageResource(R.drawable.mute); // Change to mute image
            }
            isMuted = !isMuted;
        }
    }

    /**
     * Starts the Compare Numbers activity when the user clicks the "Compare Numbers" button.
     * Plays a click sound effect when the button is pressed.
     *
     * @param view The view that triggered this method (the "Compare Numbers" button).
     */
    public void openCompareNumbers(View view) {
        Intent intent = new Intent(this, CompareNumbersActivity.class);
        playClickSound();
        startActivity(intent);
    }

    /**
     * Starts the Order Numbers activity when the user clicks the "Order Numbers" button.
     * Plays a click sound effect when the button is pressed.
     *
     * @param view The view that triggered this method (the "Order Numbers" button).
     */
    public void openOrderNumbers(View view) {
        Intent intent = new Intent(this, OrderNumbersActivity.class);
        playClickSound();
        startActivity(intent);
    }

    /**
     * Starts the Compose Numbers activity when the user clicks the "Compose Numbers" button.
     * Plays a click sound effect when the button is pressed.
     *
     * @param view The view that triggered this method (the "Compose Numbers" button).
     */
    public void openComposeNumbers(View view) {
        Intent intent = new Intent(this, ComposeNumbersActivity.class);
        playClickSound();
        startActivity(intent);
    }


    //Releases both MediaPlayer objects when the activity is destroyed to free up resources.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release both media players when activity is destroyed
        if (bgMediaPlayer != null) {
            bgMediaPlayer.release();
            bgMediaPlayer = null;
        }
        if (clickMediaPlayer != null) {
            clickMediaPlayer.release();
            clickMediaPlayer = null;
        }
    }

    //Ensure background music is paused when user minimizes app
    @Override
    protected void onPause() {
        super.onPause();
        if (bgMediaPlayer != null && bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.pause();
        }
    }

    //Ensure background music is resumed when user reopens app
    @Override
    protected void onResume() {
        super.onResume();
        if (bgMediaPlayer != null && !bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.start();
        }
    }
}

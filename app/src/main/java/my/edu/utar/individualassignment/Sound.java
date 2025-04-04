package my.edu.utar.individualassignment;

import android.media.MediaPlayer;
import android.content.Context;

/**
 * The Sound class provides utility methods for playing audio resources in an Android application.
 */
public class Sound {

    private static MediaPlayer mediaPlayer;

    /**
     * Plays a sound from the given raw resource ID.
     *
     * @param context       The application context used to access resources.
     * @param soundResource The resource ID of the sound to be played (e.g., R.raw.sound_file).
     */
    public static void playSound(Context context, int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release previous MediaPlayer instance
        }
        mediaPlayer = MediaPlayer.create(context, soundResource);
        mediaPlayer.start();
    }

    // Method to release resources
    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

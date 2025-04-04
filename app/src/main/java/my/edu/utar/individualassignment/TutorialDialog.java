package my.edu.utar.individualassignment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;
/**
 * The TutorialDialog class displays a custom dialog containing tutorial videos
 * to guide users through application features.
 *
 */
public class TutorialDialog {

    /**
     * Shows a tutorial dialog containing two instructional videos.
     *
     * @param context The context in which the dialog should be displayed.
     * @param videoResId1 Resource ID of the first tutorial video.
     * @param videoResId2 Resource ID of the second tutorial video.
     */
    public static void show(Context context, int videoResId1, int videoResId2) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_tutorial);
        dialog.setCancelable(false);


        // Adjust dialog size based on screen dimensions
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(dialog.getWindow().getAttributes());

            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }

            params.width = (int) (displayMetrics.widthPixels * 0.9);
            params.height = (int) (displayMetrics.heightPixels * 0.6);

            dialog.getWindow().setAttributes(params);
        }

        // Page containers
        LinearLayout page1 = dialog.findViewById(R.id.page1);
        LinearLayout page2 = dialog.findViewById(R.id.page2);

        // Page 1
        VideoView video1 = dialog.findViewById(R.id.videoView1);
        Button nextButton = dialog.findViewById(R.id.btnNext);
        Button skipButton = dialog.findViewById(R.id.btnSkip);

        // Page 2
        VideoView video2 = dialog.findViewById(R.id.videoView2);
        Button closeButton = dialog.findViewById(R.id.btnClose);

        // Set up video 1
        String videoPath1 = "android.resource://" + context.getPackageName() + "/" + videoResId1;
        video1.setVideoURI(Uri.parse(videoPath1));
        video1.start();
        video1.setOnCompletionListener(mp -> video1.start());

        // Set up video 2
        String videoPath2 = "android.resource://" + context.getPackageName() + "/" + videoResId2;
        video2.setVideoURI(Uri.parse(videoPath2));
        video2.setOnCompletionListener(mp -> video2.start());


        // Handle next button
        nextButton.setOnClickListener(v -> {
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
            video1.setVisibility(View.INVISIBLE);
            video1.stopPlayback();
            video2.start();
        });

        //Handle skip button
        skipButton.setOnClickListener(v -> {
            video1.setVisibility(View.INVISIBLE);
            video1.stopPlayback();
            dialog.dismiss();
        });

        //Handle close button
        closeButton.setOnClickListener(v -> {
            video2.pause();
            dialog.dismiss();
        });

        dialog.show();
    }
}

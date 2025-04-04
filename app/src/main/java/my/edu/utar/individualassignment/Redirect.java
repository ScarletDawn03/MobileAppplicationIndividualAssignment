    package my.edu.utar.individualassignment;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Handler;

    /**
     * The Redirect class provides utility methods for handling delayed redirection
     * between activities in an Android application.
     */
    public class Redirect {

        private static final Handler handler = new Handler(); // Shared handler to prevent memory leaks


        /**
         * Redirects from the current activity to the specified target activity after a given delay.
         *
         * @param activity       The current Activity context from which redirection is happening.
         * @param targetActivity The target Activity class to redirect to.
         * @param delayMillis    The delay in milliseconds before the redirection occurs.
         */
        public static void redirectToHomeWithDelay(Activity activity, Class<?> targetActivity, int delayMillis) {
            handler.postDelayed(() -> {
                Intent intent = new Intent(activity, targetActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }, delayMillis);
        }

        // Call this in onDestroy() of any activity using this to prevent memory leaks
        public static void cancelPendingRedirects() {
            handler.removeCallbacksAndMessages(null);
        }
    }

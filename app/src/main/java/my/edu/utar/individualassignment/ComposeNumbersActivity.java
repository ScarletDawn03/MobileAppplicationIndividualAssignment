package my.edu.utar.individualassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import java.util.Random;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * The ComposeNumbersActivity class provides an interactive activity where users are tasked
 * with entering two numbers that sum up to a randomly generated target number.
 */
 public class ComposeNumbersActivity extends AppCompatActivity {

    // UI components
    private EditText leftInput, rightInput;
    private TextView middleCircle, result;
    private Button check, next;

    //Game state variables
    private int target;
    private TextView textLevel, textRightAnswered;

    //Game progression variables
    int level=1;
    int great=0;
    private final int maxLevel = 10; // Max levels allowed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);
        TutorialDialog.show(this, R.raw.compose_tutorial, R.raw.htp_compose);

        // Initialize Scoreboard
        textLevel=findViewById(R.id.textQuestionNumber);
        textRightAnswered=findViewById(R.id.textRightAnswered);

        // Initialize Contents
        leftInput = findViewById(R.id.leftInput);
        rightInput = findViewById(R.id.rightInput);
        middleCircle = findViewById(R.id.middleCircle);

        // Initialize Checking/Navigation
        check = findViewById(R.id.check);
        next = findViewById(R.id.next);
        result = findViewById(R.id.result);

        //Initialize Game UI
        updateUI();
        generateNumbers();

        //On User Interaction
        check.setOnClickListener(v -> checkAnswer());
        next.setOnClickListener(v -> nextQuestion());

        // Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    // Method to show correct combinations as Toast (valid pairs that sum to the target)
    private void showCombinationsToast() {
        StringBuilder sb = new StringBuilder();
        sb.append("Correct Answers:");
        // Loop through all pairs where both numbers are between 0 and the target (inclusive)
        for (int i = 0; i <= target; i++) {
            int j = target - i; // Calculate the complementary number to make the sum equal to target
            sb.append(i).append("&").append(j);
            if (i < target) {
                sb.append(",");
            }
        }
        // Display the combinations in a Toast message
        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    // Method to generate a random target number between 1 and 10
    private void generateNumbers() {
        Random random = new Random();
        next.setVisibility(View.GONE);
        target = random.nextInt(10) + 1;
        middleCircle.setText(String.valueOf(target));
        result.setText("");      // Clear the result message
        leftInput.setText("");   // Clear left input field
        rightInput.setText("");  // Clear right input field
    }

    /**
     * Checks if the user's answer is correct.
     * The user can select whether the first number is greater than or less than the second.
     * Displays a "Correct!" message if the answer is correct, or a "Try Again!" message if incorrect.
     */
    private void checkAnswer() {
        try {
            String leftText = leftInput.getText().toString(); // Get the left input text
            String rightText = rightInput.getText().toString(); // Get the right input text

            // Check if either input field is empty
            if (leftText.isEmpty() || rightText.isEmpty()) {
                result.setText("Enter valid numbers!");
                Sound.playSound(this,R.raw.incorrect);
                return;
            }

            int leftNum = Integer.parseInt(leftText);
            int rightNum = Integer.parseInt(rightText);

            if (leftNum + rightNum == target) {
                result.setText("Correct!");
                great++;
                Sound.playSound(this,R.raw.correct);
                showCombinationsToast();
                next.setVisibility(View.VISIBLE);
                check.setVisibility(View.GONE);
            } else {
                result.setText("Try Again!");
                Sound.playSound(this,R.raw.incorrect);
            }

        } catch (NumberFormatException e) {
            result.setText("Enter valid numbers!");
            Sound.playSound(this,R.raw.incorrect);
        }
    }

    /**
     * Moves to the next question or ends the game if the max level is reached.
     * Updates the UI to reflect the new level and score.
     */
    private void nextQuestion() {
        if (level < maxLevel) {
            level++;
            updateUI();
            generateNumbers();
            check.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Congratulations! You've reached the end!", Toast.LENGTH_LONG).show();
            updateUI();
            next.setOnClickListener(v -> redirectToHomeWithDelay());
        }
    }

    /**
     * Redirects the user to the home screen with a delay.
     */
    private void redirectToHomeWithDelay() {
        Redirect.redirectToHomeWithDelay(this, MainActivity.class, 1000);
    }

    /**
     * Updates the UI to display the current level and score.
     */
    private void updateUI() {
        textLevel.setText("Level: " + level + " / " + maxLevel);
        textRightAnswered.setText("Score: " + great + " / " + maxLevel);
    }

    /**
     * Releases any resources when the activity is destroyed.
     * This includes stopping sound playback and canceling any pending redirects.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sound.release();
        Redirect.cancelPendingRedirects();
    }

    /**
     * Handles action bar item selection.
     * If the home button is selected, it finishes the activity and returns to the previous screen.
     *
     * @param item The selected menu item.
     * @return true if the item was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

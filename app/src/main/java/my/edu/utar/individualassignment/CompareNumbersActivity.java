package my.edu.utar.individualassignment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import java.util.Random;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity that handles comparing two randomly generated numbers in a math game.
 * Players select whether the first number is greater than or less than the second number.
 */

public class CompareNumbersActivity extends AppCompatActivity {

    //Game Variables
    private TextView number1, number2, result;
    int level=1;
    int great=0;
    private final int maxLevel = 10; // Max levels allowed


    //UI Elements
    private Button greaterThan, lessThan, next;
    private int num1, num2;

    private ImageView img1, img2;

    private TextView textLevel, textRightAnswered;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);
        TutorialDialog.show(this, R.raw.compare_tutorial, R.raw.htp_compare);

        //Initialize UI Elements
        textLevel=findViewById(R.id.textQuestionNumber);
        textRightAnswered=findViewById(R.id.textRightAnswered);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        img1=findViewById(R.id.num1_img);
        img2=findViewById(R.id.num2_img);
        greaterThan = findViewById(R.id.greater_than);
        lessThan = findViewById(R.id.less_than);
        next = findViewById(R.id.next);
        result = findViewById(R.id.result);

        //Initialize Game UI
        updateUI();
        generateNumbers();

        //On User Interaction
        greaterThan.setOnClickListener(v -> checkAnswer(">"));
        lessThan.setOnClickListener(v -> checkAnswer("<"));
        next.setOnClickListener(v -> nextQuestion());

        // Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Generates two distinct random numbers between 0 and 49 for the user to compare.
     * Sets the images based on whether the numbers are odd or even.
     */
    private void generateNumbers() {
        Random random = new Random();
        next.setVisibility(View.GONE); // Hide next button until answered
        num1 = random.nextInt(50); //Generate the first number

        //Generate second number different from the first
        do {
            num2 = random.nextInt(50);
        }while(num1==num2);

        //Update UI to display numbers
        number1.setText(String.valueOf(num1));
        number2.setText(String.valueOf(num2));
        result.setText("");

        // Set images based on conditions (e.g., odd/even numbers, specific values)
        if (num1 % 2 == 0) {
            img1.setImageResource(R.drawable.frog);
        } else {
            img1.setImageResource(R.drawable.fish);
        }

        if (num2 % 2 == 0) {
            img2.setImageResource(R.drawable.turtle);
        } else {
            img2.setImageResource(R.drawable.dolphin);
        }
    }

    /**
     * Checks if the user's answer is correct.
     * The user can select whether the first number is greater than or less than the second.
     * Displays a "Correct!" message if the answer is correct, or a "Try Again!" message if incorrect.
     *
     * @param symbol The comparison symbol ("<" or ">") selected by the user based on string input.
     */
    private void checkAnswer(String symbol) {
        if ((symbol.equals(">") && num1 > num2) || (symbol.equals("<") && num1 < num2)) {
            result.setText("Correct!");
            great++;
            Sound.playSound(this, R.raw.correct);
            greaterThan.setVisibility(View.GONE); // Hides the more than button
            lessThan.setVisibility(View.GONE); // Hides the less than button
            next.setVisibility(View.VISIBLE); // Show the next button
        } else {
            result.setText("Try Again!");
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
            greaterThan.setVisibility(View.VISIBLE);
            lessThan.setVisibility(View.VISIBLE);
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


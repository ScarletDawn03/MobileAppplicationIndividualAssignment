package my.edu.utar.individualassignment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * The OrderNumbersActivity class provides an interactive activity where users are tasked
 * with arranging three randomly generated numbers in either ascending or descending order.
 * The user can drag the numbers into a drop container and check if they have arranged them correctly.
 */
public class OrderNumbersActivity extends AppCompatActivity {

    // UI components
    private TextView number1, number2, number3, resultText;
    private ArrayList<Integer> numbers = new ArrayList<>();
    private LinearLayout dropContainer, numberContainer;
    private Button checkButton, next;
    private Random random = new Random();
    private boolean isAscendingOrder = true; // Default to ascending order


    //Game state variables
    private TextView textLevel, textRightAnswered;


    //Game progression variables
    int level=1;
    int great=0;
    private final int maxLevel = 10; // Max levels allowed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);
        TutorialDialog.show(this,R.raw.order_tutorial, R.raw.htp_order);

        // Initialize Scoreboard
        textLevel=findViewById(R.id.textQuestionNumber);
        textRightAnswered=findViewById(R.id.textRightAnswered);

        // Initialize Contents
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        dropContainer = findViewById(R.id.drop_container);
        numberContainer = findViewById(R.id.number_container);

        // Set Color for Contents
        setRandomColors(number1);
        setRandomColors(number2);
        setRandomColors(number3);

        // Initialize Checking/Navigation
        checkButton = findViewById(R.id.check);
        next = findViewById(R.id.next);
        resultText = findViewById(R.id.result);

        //Initialize Game UI
        updateUI();
        generateNumbers();
        setDragAndDropListeners();
        updateOrderInstruction();

        //On User Interaction
        checkButton.setOnClickListener(v -> checkOrder());
        next.setOnClickListener(v -> nextQuestion());

        // Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Generates three random numbers, shuffles them, and sets them in the number views.
     * This ensures the numbers are mixed and ready for user interaction.
     */
    private void generateNumbers() {
        Random rand = new Random();
        next.setVisibility(View.GONE); // Initially hidden

        numbers.clear();
        numbers.add(rand.nextInt(100)); // Generate numbers between 0-99
        numbers.add(rand.nextInt(100));
        numbers.add(rand.nextInt(100));
        Collections.shuffle(numbers);

        number1.setText(String.valueOf(numbers.get(0)));
        number2.setText(String.valueOf(numbers.get(1)));
        number3.setText(String.valueOf(numbers.get(2)));

        setDraggable(number1);
        setDraggable(number2);
        setDraggable(number3);
    }

    /**
     * Updates the instructional text that shows whether the user should arrange the numbers
     * in ascending or descending order. The word "ascending" or "descending" is highlighted.
     */
    private void updateOrderInstruction() {
        TextView orderInstruction = findViewById(R.id.order_instruction);
        String instructionText;
        String wordToBold;

        if (isAscendingOrder) {
            instructionText = "Arrange numbers in ASCENDING order by dragging the numbers into the box";
            wordToBold = "ASCENDING";
        } else {
            instructionText = "Arrange numbers in DESCENDING order by dragging the numbers into the box";
            wordToBold = "DESCENDING";
        }

        SpannableString spannableString = new SpannableString(instructionText);
        int startIndex = instructionText.indexOf(wordToBold);
        int endIndex = startIndex + wordToBold.length();

        spannableString.setSpan(
                new StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        orderInstruction.setText(spannableString);
    }

    /**
     * Sets the given TextView as draggable, allowing the user to drag the number into the drop container.
     *
     * @param textView The TextView containing the number to be made draggable.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setDraggable(TextView textView) {
        textView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { // Start drag when touched
                ClipData clipData = ClipData.newPlainText("number", ((TextView) v).getText().toString());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, shadowBuilder, v, 0);
                v.performClick(); // Call performClick to ensure accessibility services can register the click action
                return true; // Indicate the event is handled
            }
            return false; // Allow normal touch behavior
        });
    }

    private final int[] BACKGROUND_COLORS = {
            Color.parseColor("#FFD3D3"), // Light red
            Color.parseColor("#D3FFD3"), // Light green
            Color.parseColor("#D3D3FF"), // Light blue
            Color.parseColor("#FFFFD3"), // Light yellow
            Color.parseColor("#FFD3FF")  // Light purple
    };

    /**
     * Sets a random background color for the given TextView to make the numbers visually distinct.
     *
     * @param textView The TextView whose background color will be set.
     */
    private void setRandomColors(TextView textView) {
        Random random = new Random();
        int bgColor = BACKGROUND_COLORS[random.nextInt(BACKGROUND_COLORS.length)];
        textView.setTextColor(Color.BLACK); // All these colors work with black text

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.draggable_background);
        if (drawable != null) {
            drawable.setColorFilter(bgColor, PorterDuff.Mode.SRC_ATOP);
            textView.setBackground(drawable);
        }
    }

    /**
     * Sets the drag-and-drop listeners for the drop container.
     * Handles the start, drop, and end actions of drag-and-drop operations.
     */
    private void setDragAndDropListeners() {
        dropContainer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Allow dragging only if the ClipData contains plain text
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

                case DragEvent.ACTION_DROP:
                    // Get the dragged view
                    View draggedView = (View) event.getLocalState();
                    if (draggedView != null) {
                        // Remove the view from its original parent
                        ViewGroup parent = (ViewGroup) draggedView.getParent();
                        if (parent != null) {
                            parent.removeView(draggedView);
                        }

                        // Add the view to the drop container
                        dropContainer.addView(draggedView);
                        draggedView.setVisibility(View.VISIBLE); // Ensure the view is visible
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    // Handle any cleanup if needed
                    View droppedView = (View) event.getLocalState();
                    if (droppedView != null) {
                        droppedView.setVisibility(View.VISIBLE); // Ensure the view is visible
                    }
                    return true;
            }
            return false;
        });
    }

    /**
     * Checks if the numbers in the drop container are in the correct order (ascending or descending).
     * It compares the numbers with the correct sorted order and provides feedback to the user.
     */
    private void checkOrder() {
        ArrayList<Integer> orderedNumbers = new ArrayList<>();
        for (int i = 0; i < dropContainer.getChildCount(); i++) {
            View child = dropContainer.getChildAt(i);
            if (child instanceof TextView) {
                orderedNumbers.add(Integer.parseInt(((TextView) child).getText().toString()));
            }
        }

        ArrayList<Integer> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);

        boolean isCorrect = false;

        if (isAscendingOrder) {
            isCorrect = orderedNumbers.equals(sortedNumbers);
            resultText.setText(isCorrect ? "Correct Order (Ascending)!" : "Try Again!");
        } else {
            Collections.sort(sortedNumbers, Collections.reverseOrder());
            isCorrect = orderedNumbers.equals(sortedNumbers);
            resultText.setText(isCorrect ? "Correct Order (Descending)!" : "Try Again!");
        }

        if (isCorrect) {
            Sound.playSound(this,R.raw.correct);
            great++;
            checkButton.setVisibility(View.GONE); // Hide check button
            next.setVisibility(View.VISIBLE);    // Show next button
        } else {
            Sound.playSound(this,R.raw.incorrect);
            // Keep check button visible for retry
        }

        textRightAnswered.setText("Score: " + great + " / " + maxLevel);
    }

    /**
     * Moves to the next question by generating new numbers, resetting the drop container, and updating the UI.
     * If the user has completed all levels, they will be shown a congratulations message.
     */
    private void nextQuestion() {
        if (level < maxLevel) {
            level++;
            updateUI();
            next();
        } else {
            Toast.makeText(this, "Congratulations! You've reached the end!", Toast.LENGTH_LONG).show();
            updateUI();
            next.setOnClickListener(v -> redirectToHomeWithDelay());
        }
    }

    /**
     * Resets the UI for the next question, generates new random numbers, and updates the instruction.
     */
    private void next() {
        dropContainer.removeAllViews();
        numberContainer.removeAllViews();
        numberContainer.addView(number1);
        numberContainer.addView(number2);
        numberContainer.addView(number3);
        generateNumbers();

        setRandomColors(number1);
        setRandomColors(number2);
        setRandomColors(number3);

        resultText.setText("");
        next.setVisibility(View.GONE);
        checkButton.setVisibility(View.VISIBLE);

        // Randomly choose between ascending and descending
        isAscendingOrder = random.nextBoolean();
        updateOrderInstruction();
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
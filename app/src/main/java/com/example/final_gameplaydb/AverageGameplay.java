package com.example.final_gameplaydb;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_gameplaydb.Model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AverageGameplay extends AppCompatActivity implements View.OnClickListener {
    TextView question;
    TextView timerNum;
    TextView questionCount;
    String answer = "";
    int scoreNum;
    Button first, second, third, fourth;
    List<Question> questionList;
    int questionNum;
    CountDownTimer countDown;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    boolean quizEnded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        question = findViewById(R.id.Question);
        questionCount = findViewById(R.id.QuestionNum);
        timerNum = findViewById(R.id.TimerNum);


        first = findViewById(R.id.FirstChoice);
        second = findViewById(R.id.SecondChoice);
        third = findViewById(R.id.ThirdChoice);
        fourth = findViewById(R.id.FourthChoice);

        first.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);
        fourth.setOnClickListener(this);

        getQuestionsList();


        countDown = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerNum.setText(String.valueOf(millisUntilFinished / 1000));
                if (millisUntilFinished <= 0) {
                    countDown.cancel();
                }
            }
            @Override
            public void onFinish() {
                if(!quizEnded) {
                    quizEnded = true;
                    Intent intent = new Intent(AverageGameplay.this, ScoreActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.start();
    }

    private void getQuestionsList() {
        questionList = new ArrayList<>();
        String selectedDifficulty = "Average";

        firestore.collection("Difficulties").document(selectedDifficulty).collection("questions")
                .get().addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot questions = task.getResult();

                        for(QueryDocumentSnapshot doc : questions){
                            Log.d("FirestoreData", doc.getData().toString());
                            questionList.add(new Question(
                                    doc.getString("Question"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getString("Answer")));
                        }

                        Collections.shuffle(questionList);
                        setQuestion();

                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        task.getException().printStackTrace();
                        Toast.makeText(AverageGameplay.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setQuestion() {

        //timerNum.setText(String.valueOf(60));
        if (!questionList.isEmpty()) { // Check if questionList is not empty
            question.setText(questionList.get(0).getQuestion());
            first.setText(questionList.get(0).getOptionA());
            second.setText(questionList.get(0).getOptionB());
            third.setText(questionList.get(0).getOptionC());
            fourth.setText(questionList.get(0).getOptionD());

            questionCount.setText(String.valueOf(1));

            questionNum = 0;
        } else {
            // Handle the case when questionList is empty (e.g., show an error message or log)
            Log.e("AverageGameplay", "Question list is empty");
            // You might want to return from the method or take appropriate action.
        }

    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        String selectChoice = "";

        if (v.getId() == R.id.FirstChoice) {
            selectChoice = "A";
        } else if (v.getId() == R.id.SecondChoice) {
            selectChoice = "B";
        } else if (v.getId() == R.id.ThirdChoice) {
            selectChoice = "C";
        } else if (v.getId() == R.id.FourthChoice) {
            selectChoice = "D";
        }
        checkAnswer(selectChoice, v);
    }

    private void checkAnswer(String selectChoice, View view) {
        Button selectedButton = (Button) view;

        if (selectChoice.trim().equalsIgnoreCase(selectedButton.getText().toString().trim())) {
            // right answer
            selectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            scoreNum++;
        } else {
            // wrong answer
            selectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            // Set the background tint of the correct answer to green
            /**switch (questionList.get(questionNum).getCorrectAns()) {
             case "A":
             first.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
             break;
             case "B":
             second.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
             break;
             case "C":
             third.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
             break;
             case "D":
             fourth.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
             break;
             }**/
        }

        // Delay for 1000 milliseconds before changing the question
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedButton.setEnabled(true);

                // Reset background tint of all buttons before changing the question
                selectedButton.setBackgroundTintList(null);
                first.setBackgroundTintList(null);
                second.setBackgroundTintList(null);
                third.setBackgroundTintList(null);
                fourth.setBackgroundTintList(null);

                answer = questionList.get(0).getCorrectAns();

                changeQuestion();
            }
        }, 0500);
    }

    private void changeQuestion() {
        if (questionNum < questionList.size() - 1) {
            questionNum++;

            playAnim(question, 0, 0);
            playAnim(first, 0, 1);
            playAnim(second, 0, 2);
            playAnim(third, 0, 3);
            playAnim(fourth, 0, 4);

            questionCount.setText(String.valueOf(questionNum + 1));

            //timerNum.setText(String.valueOf(60));
        } else {
            // go to score activity
            Intent intent = new Intent(AverageGameplay.this, ScoreActivity.class);
            intent.putExtra("SCORE", scoreNum); // Pass the score here
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0){
                            switch (viewNum){
                                case 0:
                                    ((TextView)view).setText(questionList.get(questionNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(questionNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(questionNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(questionNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(questionNum).getOptionD());
                                    break;
                            }
                            /**if (viewNum == 0){
                             ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03A9F4")));
                             }**/

                            playAnim(view,1,viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
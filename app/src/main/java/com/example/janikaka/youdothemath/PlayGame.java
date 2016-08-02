package com.example.janikaka.youdothemath;
import android.app.Activity;
import java.util.Random;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by janikaka on 2.8.2016.
 */
public class PlayGame extends Activity implements OnClickListener{
    private int level = 0, answer = 0, operator = 0, operand1 = 0, operand2 = 0;
    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2, DIVIDE_OPERATOR = 3;
    private String[] operators = {"+", "-", "x", "/"};
    private int[][] levelMin = {
            {1, 11, 21},
            {1, 5, 10},
            {2, 5, 10},
            {2, 3, 5}};
    private int[][] levelMax = {
            {10, 25, 50},
            {10, 20, 30},
            {5, 10, 15},
            {10, 50, 100}};
    private Random random;
    private TextView question, answerTxt, scoreTxt;
    private ImageView response;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, enterBtn, clearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);

        question =  (TextView)findViewById(R.id.question);
        answerTxt = (TextView)findViewById(R.id.answer);
        response =  (ImageView)findViewById(R.id.response);
        scoreTxt =  (TextView)findViewById(R.id.score);

        response.setVisibility(View.INVISIBLE);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int passedLevel = extras.getInt("level", -1);
            if(passedLevel>=0) level = passedLevel;
        }

        random = new Random();
        chooseQuestion();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.enter){
            //enter button
            //get answer
            String answerContent = answerTxt.getText().toString();
            //check we have an answer
            if(!answerContent.endsWith("?")){
                //get number
                int enteredAnswer = Integer.parseInt(answerContent.substring(2));
                //get score
                int exScore = getScore();
                //check answer
                if(enteredAnswer==answer){
                    //correct
                    scoreTxt.setText("Score: "+(exScore+1));
                    response.setImageResource(R.drawable.tick);
                    response.setVisibility(View.VISIBLE);
                }
                else{
                    //incorrect
                    scoreTxt.setText("Score: 0");
                    response.setImageResource(R.drawable.cross);
                    response.setVisibility(View.VISIBLE);
                }
                chooseQuestion();
            }
        }
        else if(view.getId()==R.id.clear){
            //clear button
            answerTxt.setText("= ?");
        }
        else {
            //number button
            response.setVisibility(View.INVISIBLE);
            //get number from tag
            int enteredNum = Integer.parseInt(view.getTag().toString());
            //either first or subsequent digit
            if(answerTxt.getText().toString().endsWith("?"))
                answerTxt.setText("= "+enteredNum);
            else
                answerTxt.append(""+enteredNum);
        }
    }

    private int getScore(){
        String scoreStr = scoreTxt.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    //method generates questions
    private void chooseQuestion(){
        //reset answer text
        answerTxt.setText("= ?");
        //choose operator
        operator = random.nextInt(operators.length);
        //choose operands
        operand1 = getOperand();
        operand2 = getOperand();

        //checks for operators
        if(operator==SUBTRACT_OPERATOR){
            //no negative answers
            while(operand2>operand1){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }
        else if(operator==DIVIDE_OPERATOR){
            //whole numbers only
            while((((double)operand1/(double)operand2)%1 > 0)
                    || (operand1==operand2)){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }

        //calculate answer
        switch(operator){
            case ADD_OPERATOR:
                answer = operand1+operand2;
                break;
            case SUBTRACT_OPERATOR:
                answer = operand1-operand2;
                break;
            case MULTIPLY_OPERATOR:
                answer = operand1*operand2;
                break;
            case DIVIDE_OPERATOR:
                answer = operand1/operand2;
                break;
            default:
                break;
        }

        //show question
        question.setText(operand1+" "+operators[operator]+" "+operand2);
    }

    //method generates operands
    private int getOperand(){
        return random.nextInt(levelMax[operator][level] - levelMin[operator][level] + 1)
                + levelMin[operator][level];
    }
}
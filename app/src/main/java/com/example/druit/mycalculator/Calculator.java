package com.example.druit.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Calculator extends AppCompatActivity {

    private int[] numericButtons = {R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.buttonZero};
    private int[] operators = {R.id.buttonAdd,R.id.buttonDiv,R.id.buttonMulty,R.id.buttonEnt,R.id.buttonKom,R.id.buttonSub,R.id.btnPar1,R.id.btnPar2,R.id.buttonRise,R.id.buttonExp,R.id.buttonLn,R.id.buttonLog};
    private TextView textView3;

    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;
    private boolean check=false; //check parenteses
    private boolean equl=false;
    char[] txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        this.textView3 = (TextView) findViewById(R.id.textView3);

        SetNumericOnClickListener();
        setOperatorOnClickListener();
    }
    private void SetNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just append/set the text of clicked button
                Button button = (Button) v;
                if (stateError) {
                    // If current state is Error, replace the error message
                    textView3.setText(button.getText());
                    stateError = false;
                } else if (equl==true) {
                    // If not, already there is a valid expression so append to it
                    textView3.setText(button.getText());
                } else{
                    textView3.append(button.getText());
                }
                // Set the flag
                lastNumeric = true;
            }
        };
        // Assign the listener to all the numeric buttons
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }



    private void setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the current state is Error do not append the operator
                // If the last input is number only, append the operator
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    textView3.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;    // Reset the DOT flag
                    equl=false;
                }
            }
        };
        for (int id: operators){
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.buttonKom).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (lastNumeric && !stateError && !lastDot) {
                    textView3.append(".");
                    lastNumeric = false;
                    lastDot = true;
                    equl=false;
                }
            }
        });
        for (int id : operators) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.buttonKom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    textView3.append(".");
                    lastNumeric = false;
                    lastDot = true;
                    equl=false;
                }
            }
        });
        findViewById(R.id.btnPar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check==false) {
                    textView3.append("(");
                    check=true;
                    equl=false;

                }

            }
        });
        findViewById(R.id.btnPar2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check==true) {
                    textView3.append(")");
                    check=false;
                    equl=false;

                }

            }
        });
        findViewById(R.id.buttonExp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView3.append("exp");
                equl=false;

            }

        });
        findViewById(R.id.buttonLn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                textView3.append("log");
                equl=false;

            }

        });
        findViewById(R.id.buttonLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView3.append("log10");
                equl=false;

            }

        });
        findViewById(R.id.buttonRise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView3.append("^");
                equl=false;

            }

        });


        findViewById(R.id.buttonC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView3.setText("");  // Clear the screen
                // Reset all the states and flags
                lastNumeric = false;
                stateError = false;
                lastDot = false;
                equl=false;
                check=false;
            }
        });
        findViewById(R.id.buttondel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = textView3.getText().toString();
                if (txt.length()>0){
                    txt=txt.substring(0,txt.length()-1);
                    textView3.setText(txt);
                }
                equl=false;
            }
        });
        findViewById(R.id.buttonEnt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnt();
            }
        });
    }

    private void onEnt() {
        if (lastNumeric && !stateError) {
            // Read the expression
            /* String str = textView3.getText().toString();
            char[] charArray = str.toCharArray();
           for (char ch: charArray){
                textView3.setText(ch);
            }

           for (int i=0;i<textView3.length();i++){
                    //if(txt[i] == "0"|| txt[i]=="1" || txt[i] == "2"|| txt[i]=="3" || txt[i] == "4" || txt[i]=="5" || txt[i] == "6" || txt[i]=="7" || txt[i] == "8"|| txt[i]=="9"){

                    //}

            }*/
            String txt = textView3.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                textView3.setText(Double.toString(result));
                lastDot = true; // Result contains a dot
            } catch (ArithmeticException ex) {
                // Display an error message
                textView3.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
            equl=true;
        }
    }

}




package com.manbou.appli.practicecalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    TextView textView;
    TextView editText;
    int recentOperator = R.id.button_equal;
    boolean operatorFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        editText = findViewById(R.id.edittext);

        findViewById(R.id.clear).setOnClickListener(clearListener);
        findViewById(R.id.button_1).setOnClickListener(numberListener);
        findViewById(R.id.button_2).setOnClickListener(numberListener);
        findViewById(R.id.button_3).setOnClickListener(numberListener);
        findViewById(R.id.button_4).setOnClickListener(numberListener);
        findViewById(R.id.button_5).setOnClickListener(numberListener);
        findViewById(R.id.button_6).setOnClickListener(numberListener);
        findViewById(R.id.button_7).setOnClickListener(numberListener);
        findViewById(R.id.button_8).setOnClickListener(numberListener);
        findViewById(R.id.button_9).setOnClickListener(numberListener);
        findViewById(R.id.button_0).setOnClickListener(numberListener);
        findViewById(R.id.button_dot).setOnClickListener(numberListener);

        findViewById(R.id.button_add).setOnClickListener(operatorListener);
        findViewById(R.id.button_subtract).setOnClickListener(operatorListener);
        findViewById(R.id.button_multiply).setOnClickListener(operatorListener);
        findViewById(R.id.button_divide).setOnClickListener(operatorListener);
        findViewById(R.id.button_equal).setOnClickListener(operatorListener);
    }

    View.OnClickListener numberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!editText.getText().toString().equals("Error")) {
                Button button = (Button) view;

                if (operatorFlag) {
                    editText.setText(String.format("%s%s","0", button.getText()));
                } else if (editText.getText().toString().replace(",","").replace(".","").length() < 15
                            && !(editText.getText().toString().contains(".") && button.getId() == R.id.button_dot)) {
                    editText.append(button.getText());
                }

                if (editText.getText().toString().contains(".")) {
                    String Integer = editText.getText().toString().substring(0, editText.getText().toString().indexOf(".")).replaceAll(",", "");
                    String Decimal = editText.getText().toString().substring(editText.getText().toString().indexOf(".") + 1);
                    editText.setText(String.format("%s%s%s", new DecimalFormat("#,##0").format(Double.parseDouble(Integer)),".", Decimal));
                } else {
                    editText.setText(new DecimalFormat("#,##0").format(Double.parseDouble(editText.getText().toString().replaceAll(",", ""))));
                }
                operatorFlag = false;
            }
        }
    };

    double result;

    View.OnClickListener operatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!editText.getText().toString().equals("Error")) {
                Button operatorButton = (Button) view;
                double value = Double.parseDouble(editText.getText().toString().replace(",", ""));

                    if (recentOperator == R.id.button_equal) {
                        result = value;
                    } else if (!operatorFlag || operatorButton.getId() == R.id.button_equal) {

                        if (recentOperator == R.id.button_divide && value == 0) {
                            editText.setText(String.format("%s", "Error"));
                        } else {
                            result = calc(recentOperator, result, value);

                            if (result < -999999999999999d || result > 999999999999999d) {  //丸めが行われない最大桁数
                                editText.setText(String.format("%s", "Error"));
                            }
                        }
                    }
                if (!editText.getText().toString().equals("Error")) {
                    editText.setText(new DecimalFormat(",##0.##############").format(result));
                }
                recentOperator = operatorButton.getId();
                textView.setText(operatorButton.getText());
                operatorFlag = true;
            }
        }
    };

    double calc(int operator, double value1, double value2) {
        BigDecimal v1 = new BigDecimal(Double.toString(value1));
        BigDecimal v2 = new BigDecimal(Double.toString(value2));
        MathContext mc = new MathContext(15, RoundingMode.HALF_UP);
        switch (operator) {
            case R.id.button_add:
                return Double.parseDouble(v1.add(v2).round(mc).toString());
            case R.id.button_subtract:
                return Double.parseDouble(v1.subtract(v2).round(mc).toString());
            case R.id.button_multiply:
                return Double.parseDouble(v1.multiply(v2).round(mc).toString());
            case R.id.button_divide:
                return Double.parseDouble(v1.divide(v2, 15, BigDecimal.ROUND_HALF_UP).round(mc).toString());
            default:
                return value1;
        }
    }

    View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textView.setText("");
            editText.setText("0");
            recentOperator = R.id.button_equal;
            operatorFlag = false;
        }
    };
}
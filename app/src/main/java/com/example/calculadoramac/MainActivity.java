package com.example.calculadoramac;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TextView calcs;
    Double result = 0.0;
    // Flags
    boolean operation = false; // tengo un operador activo?
    boolean decimal = false; // he puesto un decimal?
    boolean sign = false; // he puesto un signo al primero?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Selectors
        calcs = findViewById(R.id.calcs);
    }

    public void avoidZeroInit(String num) {
        if (calcs.getText().toString().equals("0")) {
            calcs.setText(num); // si es 0 lo cambio por el numero
        } else {
            calcs.setText(calcs.getText() + num); // si no es 0 lo concateno
        }
    }
    public void btnZero(View view) { avoidZeroInit("0"); operation = false; }
    public void btnOne(View view) { avoidZeroInit("1"); operation = false; }
    public void btnTwo(View view) { avoidZeroInit("2"); operation = false; }
    public void btnThree(View view) { avoidZeroInit("3"); operation = false; }
    public void btnFour(View view) { avoidZeroInit("4"); operation = false; }
    public void btnFive(View view) { avoidZeroInit("5"); operation = false; }
    public void btnSix(View view) { avoidZeroInit("6"); operation = false; }
    public void btnSeven(View view) { avoidZeroInit("7"); operation = false; }
    public void btnEight(View view) { avoidZeroInit("8"); operation = false; }
    public void btnNine(View view) { avoidZeroInit("9"); operation = false; }

    public void btnClean(View view) {
        calcs.setText("0");
        result = 0.0;
        operation = false;
        decimal = false;
    }

    public void btnComma(View view) {
        if (!decimal && !operation) {
            calcs.setText(calcs.getText() + ",");
            decimal = true;
        }
    }
    public void btnSign(View view) {
        if (sign) return;
        String txt = calcs.getText().toString();
        if (txt.charAt(0) == '-') {
            txt = txt.substring(1);
        } else {
            txt = "-" + txt;
        }
        calcs.setText(txt);
    }
    private void setOperation(String operator) {
        String txt = calcs.getText().toString();
        Character last = txt.charAt(txt.length() -1);
        if ( Character.isDigit(last) ) {
            calcs.setText(calcs.getText() + operator);
        } else if ( !Character.isDigit(last) ) {
            txt = txt.substring(0, txt.length() -1) + operator;
            calcs.setText(txt);
        }
        operation = true;
        decimal = false;
        sign = true;
    }
    public void btnPlus(View view) {
        setOperation("+");
    }
    public void btnMinus(View view) {
        setOperation("-");
    }
    public void btnMultiply(View view) {
        setOperation("x");
    }
    public void btnDivide(View view) {
        setOperation("/");
    }
    public void btnPercent(View view) {
        // implementar porcentaje...
    }
    public void btnEqual(View view) {
        String txt = calcs.getText().toString();
        txt = txt.replace(",", ".");
        // array de numeros
        List<String> arrayNumbers = Arrays.stream(txt.split("[+\\-x/]"))
                .map(num -> num.isEmpty() ? "0" : num)
                .collect(Collectors.toList());
        List<String> arrayOperators = new ArrayList<>(Arrays.asList(txt.split("\\d+(\\.\\d+)?")));
        arrayOperators.removeIf(String::isEmpty);

        Double num1, num2;
        result = 0.0;
        if (sign) {
            result += Double.parseDouble(arrayNumbers.get(0));
        } else {
            result -= Double.parseDouble(arrayNumbers.get(0));
            arrayOperators.remove(0);
        }
        int j = 0;
        for (int i = 1; i < arrayNumbers.size(); i++) {
            switch (arrayOperators.get(j++)) {
                case "+":
                    result += Double.parseDouble(arrayNumbers.get(i));
                    break;
                case "-":
                    result -= Double.parseDouble(arrayNumbers.get(i));
                    break;
                case "x":
                    result *= Double.parseDouble(arrayNumbers.get(i));
                    break;
                case "/":
                    result /= Double.parseDouble(arrayNumbers.get(i));
                    break;
                default:
                    break;
            }
        }
        /// resets finales
        calcs.setText(result.toString().replace(".", ","));
        operation = true;
        decimal = false;
        sign = false;
    }

}
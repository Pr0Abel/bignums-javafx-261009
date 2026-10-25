package com.example.bignumsjavafx261009;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    public TextField display;

    private DamnBigNumber a = DamnBigNumber.DBN_ZERO;
    private char operation = 0;     // 0 = nincs függő művelet
    private boolean newEntry = true; // a következő számjegy új számot kezd

    public void handleDigitClick(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        String value = source.getText();

        if (newEntry || display.getText().equals("0")) {
            display.setText(value);
        } else {
            display.setText(display.getText() + value);
        }
        newEntry = false;
    }

    public void handleAddClick(ActionEvent actionEvent) {
        setOperator('+');
    }

    public void handleSubtractClick(ActionEvent actionEvent) {
        setOperator('-');
    }

    public void handleMultiplyClick(ActionEvent actionEvent) {
        setOperator('*');
    }

    public void handleDivideClick(ActionEvent actionEvent) {
        setOperator('/');
    }

    public void handleRemainderClick(ActionEvent actionEvent) {
        setOperator('%');
    }

    public void handleEqualsClick(ActionEvent actionEvent) {
        if (operation == 0) {
            return;
        }
        calculate();
        operation = 0;
        newEntry = true;
    }

    public void handleClearClick(ActionEvent actionEvent) {
        a = DamnBigNumber.DBN_ZERO;
        operation = 0;
        newEntry = true;
        display.setText("0");
    }

    private void setOperator(char op) {
        if (operation != 0 && !newEntry) {
            // láncolt műveletek: 1 + 2 + 3
            if (!calculate()) {
                return;
            }
        } else {
            a = current();
        }
        operation = op;
        newEntry = true;
    }

    /** A kijelzőn lévő szám; hibaüzenet esetén 0. */
    private DamnBigNumber current() {
        try {
            return new DamnBigNumber(display.getText());
        } catch (NumberFormatException e) {
            return DamnBigNumber.DBN_ZERO;
        }
    }

    /** Elvégzi a függő műveletet a-val és a kijelzőn lévő számmal. @return false hiba esetén */
    private boolean calculate() {
        DamnBigNumber b = current();
        try {
            DamnBigNumber c;
            switch (operation) {
                case '+':
                    c = DamnBigNumbers.add(a, b);
                    break;
                case '-':
                    c = DamnBigNumbers.subtract(a, b);
                    break;
                case '*':
                    c = DamnBigNumbers.multiply(a, b);
                    break;
                case '/':
                    c = DamnBigNumbers.divide(a, b);
                    break;
                case '%':
                    c = DamnBigNumbers.remainder(a, b);
                    break;
                default:
                    return true;
            }
            a = c;
            display.setText(c.getNumStr());
            return true;
        } catch (ArithmeticException e) {
            // 0-val osztás vagy negatív különbség (csak természetes számok)
            display.setText("Hiba");
            a = DamnBigNumber.DBN_ZERO;
            operation = 0;
            newEntry = true;
            return false;
        }
    }
}
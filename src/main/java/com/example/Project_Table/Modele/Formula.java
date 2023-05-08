package com.example.Project_Table.Modele;

import java.util.ArrayDeque;
import java.util.Deque;

public class Formula {

    public Formula(){
    }

    public static double excelFormula(String input) {
        Deque<Double> numStack = new ArrayDeque<>();
        Deque<Character> opStack = new ArrayDeque<>();

        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            /*Bloc pour nombre complexe*/
            if (Character.isDigit(c) || c == '.') {//vérification si c'est un type digital
                int j = i + 1;
                while (j < input.length() && (Character.isDigit(input.charAt(j)) || input.charAt(j) == '.')) {
                    j++;
                }

                double number = Double.parseDouble(input.substring(i, j));
                numStack.push(number);
                i = j;

            } else if (c == '(') {//gesion des parenthèses ouvrantes
                opStack.push(c);
                i++;
            } else if (c == ')') {//gestion des parenthèses fermantes
                while (opStack.peek() != '(') {
                    double b = numStack.pop();
                    double a = numStack.pop();
                    char op = opStack.pop();
                    numStack.push(performOperation(a, b, op));
                }
                opStack.pop(); // Remove le '('
                i++;
            } else if (isOperator(c)) {
                while (!opStack.isEmpty() && precedence(c) <= precedence(opStack.peek())) {
                    double b = numStack.pop();
                    double a = numStack.pop();
                    char op = opStack.pop();
                    numStack.push(performOperation(a, b, op));
                }
                opStack.push(c);
                i++;
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        while (!opStack.isEmpty()) {
            double b = numStack.pop();
            double a = numStack.pop();
            char op = opStack.pop();
            numStack.push(performOperation(a, b, op));
        }

        return numStack.pop();
    }

    private static boolean isOperator(char c) {//vérifier si le char est un caractère d'operation
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private static double performOperation(double a, double b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }
}

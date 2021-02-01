package com.epam.brest;

public class Main {

    public static void main(String[] args) {
        // Distance
        // Price per km
        // Weight
        // Price per kg
        // Result = d * pr1 + w * pr2

        double d = -10;
        double pr1 = 20.05;
        double w = 30;
        double pr2 = 30.5;

        double result = d*pr1+w*pr2;
        System.out.println("Result: " + result);
    }

}

package com.example.numbersdetector;


public class NumberDetector {

    static boolean isPrime(int num){
        if(num < 2) return false;
        double n = Math.sqrt(num);
        for(int i = 2; i<=n;i++){
            if(num%i==0)return false;
        }
        return true;
    }

    static boolean isSquare(int num1){
        int n = (int) Math.sqrt(num1);
        int num2 =  n*n;
        return num1 > 0 && num1 == num2;
    }
    static boolean isTriangular(int num1){
        int n = (int) ((-1 + Math.sqrt(1 + 8*num1))/2);
        int num2 =  (n*(n+1)/2);
        return num1 > 0 && num1 == num2;
    }

}

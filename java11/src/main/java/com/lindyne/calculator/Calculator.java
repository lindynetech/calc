package com.lindyne.calculator;
import org.springframework.stereotype.Service;

/**
* Main Calculator Class
*/
@Service
public class Calculator {
  int sum(int a, int b) {
    return a + b;
  }
}
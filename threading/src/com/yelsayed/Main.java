package com.yelsayed;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

  private static int fibonacci(int n) {
    if (n < 2) {
      return n;
    }
    return fibonacci(n - 1) + fibonacci(n - 2);
  }

  public static void main(String[] args) throws InterruptedException {
    Instant start = Instant.now();

    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 50; ++i) {
      executor.submit(() -> fibonacci(40));
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.MINUTES);

    System.out.println("done in: " + Duration.between(start, Instant.now()).toMillis());
  }
}

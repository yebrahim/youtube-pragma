// Synchronization

import java.util.concurrent.Semaphore;

public class Main {
  static int count = 0;
  static Semaphore semaphore = new Semaphore(1);

  // thread safe
  private static void runCounter() {
    for (int i = 0; i < 100000; ++i) {
      try {
        semaphore.acquire();
        count++;
        semaphore.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(Main::runCounter);
    Thread t2 = new Thread(Main::runCounter);
    t1.start();
    t2.start();
    t1.join();
    t2.join();

    System.out.printf("count=%d", count);
  }
}

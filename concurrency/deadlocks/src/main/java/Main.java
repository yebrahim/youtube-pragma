import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
  private static class Account {
    Account(final String name) {
      this.name = name;
    }

    private String name = "";
    public int balance = 100;
    public ReentrantLock lock = new ReentrantLock();

    public String getName() {
      return name;
    }
  }

  private static void transfer(final Account from, final Account to, final int amount) {
    try {
      while(true) {
        if (!from.lock.tryLock(100, TimeUnit.MILLISECONDS)) continue;
        System.out.printf("Grabbed %s lock\n", from.getName());
        System.out.flush();

        if (!to.lock.tryLock(100, TimeUnit.MILLISECONDS)) {
          System.out.printf("Failed to grab %s, will retry..\n", to.getName());
          System.out.flush();
          from.lock.unlock();
          continue;
        }
        System.out.printf("Grabbed %s lock\n", to.getName());
        System.out.flush();

        from.balance -= amount;
        to.balance += amount;

        System.out.printf("Transfer %s->%s complete\n", from.getName(), to.getName());
        System.out.flush();
        break;
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      from.lock.unlock();
      to.lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    final Account account1 = new Account("A");
    final Account account2 = new Account("B");

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 10; ++i) transfer(account1, account2, 10);
    });
    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 10; ++i) transfer(account2, account1, 3);
    });
    t1.start();
    t2.start();

    t1.join();
    t2.join();

    System.out.printf("Success. Account1: %d, account2: %d", account1.balance, account2.balance);
  }
}

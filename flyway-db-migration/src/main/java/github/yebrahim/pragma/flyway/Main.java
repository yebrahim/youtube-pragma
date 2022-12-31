package github.yebrahim.pragma.flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.exception.FlywayValidateException;

public class Main {

  static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
  static final String DB_USER = "y";
  static final String DB_PASS = "postgres";
  static final Flyway flyway = Flyway.configure().dataSource(DB_URL, DB_USER, DB_PASS).load();
  static final Connection conn;

  static {
    try {
      conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    try {
      flyway.migrate();
    } catch (FlywayValidateException e) {
      System.err.println("Error running migrations: " + e);
      System.exit(1);
    }

    while (true) {
      System.out.println("Input:");
      final String line = new Scanner(System.in).nextLine();
      final String[] words = line.split("\\s");

      if (words.length < 3) {
        System.err.println("Please provide three words");
        continue;
      }

      try {
        conn.createStatement()
            .execute(
                String.format(
                    "INSERT INTO person (id, name, phone_number, modified_timestamp) VALUES ('%s', '%s', '%s', now())",
                    words[0], words[1], words[2]));
        System.out.println("Inserted. Give me more!");
      } catch (SQLException e) {
        System.err.println("Error inserting row: " + e);
      }
    }
  }
}

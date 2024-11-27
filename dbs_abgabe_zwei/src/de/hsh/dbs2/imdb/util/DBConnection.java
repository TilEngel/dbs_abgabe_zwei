package de.hsh.dbs2.imdb.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static Connection conn;

	private static final String URL = "jdbc:oracle:thin:@localhost:15211/db01";
	private static final String USER = "5zq-s8e-u1";
	private static final String PASSWORD = "testit2_";

	static {
		connect();
	}

	private static void connect() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn.setAutoCommit(false);
			System.out.println("Connect durchgeführt ....");
		} catch (SQLException e) {
			System.err.println("Error while connecting to database");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Connection getConnection() {
		try {
			// Prüfe, ob die Verbindung gültig ist (z. B. durch ein Timeout von 2 Sekunden)
			if (conn == null || conn.isClosed() || !conn.isValid(2)) {
				System.out.println("Verbindung ungültig, erneuter Verbindungsaufbau...");
				connect();
			}
		} catch (SQLException e) {
			System.err.println("Fehler bei der Überprüfung der Verbindung");
			e.printStackTrace();
		}
		return conn;
	}
}

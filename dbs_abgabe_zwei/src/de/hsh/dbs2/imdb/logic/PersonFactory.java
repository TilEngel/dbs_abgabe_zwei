package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonFactory {

    /** Liefert die Personen ID eine gesuchten Namens zurück */
    public static Long getPersonID(String name) throws Exception {
        Long personID = null;
        String sql = "SELECT personID FROM person WHERE name LIKE ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    personID = rs.getLong("personID");
                }
            }
        } catch (Exception e) {
            throw new Exception("PersonFactory: Fehler beim Abfragen der personID von " + name + e.getMessage());
        }
        return personID;
    }
    /** Liefert eine Liste von Personen wobei die Groß-/Kleinschreibung hier keine Rolle spielt */
    public static List<String> getPersonList(String text) throws Exception {
        String sql = "SELECT * FROM person WHERE LOWER(name) LIKE ?";
        List<String> persons = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + text.toLowerCase() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        persons.add(rs.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("PersonFactory: Kein Film mit diesem Titel gefunden: "+e.getMessage());
        }
        return persons;
    }
    /** Liefert den Namen einer Personen zurück, mit einer Personen ID */
    public static String getPersonName(Long personID) throws Exception {
        String name = "";
        String sql = "SELECT name FROM person WHERE personID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, personID);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    name = rs.getString("Name");
                }
            }
        } catch (Exception e) {
            throw new Exception("PersonFactory: Kein Name zur personID: " + personID + ". " + e.getMessage());
        }
        return name;
    }

}

package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    private Long personID;
    private String name;
    private Character sex;

    public Long getPersonID() {
        return personID;
    }
    public void setPersonID(Long personID) {
        this.personID = personID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public char getSex() {
        return sex;
    }
    public void setSex(Character sex) {
        if (sex != 'F' && sex != 'M' && sex != null) {
            throw new IllegalArgumentException("Sex has to be M, F or null");
        }
        this.sex = sex;
    }


    /** Fügt eine Person ein */
    public void insert() throws Exception{
        generatePersonID();
        String sql = "INSERT INTO person (personID, name, sex) VALUES (?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, personID);
                stmt.setString(2, name);
                stmt.setString(3, String.valueOf(sex));
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            throw new Exception("Person: Fehler beim Einfügen der Person mit der personID: " + personID + ", dem Namen: " + name + " und dem Geschlecht: " + sex + ": " + e.getMessage());
        }
    }
    /** Generiert eine Personen ID auf den nächsthöheren Wert */
    private void generatePersonID() throws Exception {
        String sql = "SELECT MAX(personID) FROM person";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                personID = rs.getLong(1)+1;
            }
        } catch (Exception e) {
            throw new Exception("Person: Fehler beim Generieren einer PersonID: " + personID + " : "+e.getMessage());
        }
    }
    /** Löscht eine Person */
    public void delete() throws Exception {
        String sql = "DELETE FROM person WHERE personID = ?";
        try {
            Connection con = DBConnection.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setLong(1, personID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception("Person: Fehler beim Löschen der Person mit der personID " + personID + " : " + e.getMessage());
        }
    }
    /** Gibt an, ob eine Person mit diesen Namen existiert */
    public boolean personExists() throws Exception {
        boolean exists = false;
        String sql = "SELECT name FROM person WHERE name = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Person: Fehler beim Prüfen, ob die Person mit dem Namen " + name + " existiert: " + e.getMessage());
        }
        return exists;
    }
}

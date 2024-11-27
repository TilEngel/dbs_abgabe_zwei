package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieCharacter {
    private Long movCharID;
    private String character;
    private String alias;
    private int position;
    private Long movieID;
    private Long personID;
    private String successString;

    public Long getMovCharID() {
        return movCharID;
    }

    public String getCharacter() {
        return character;
    }

    public String getAlias() {
        return alias;
    }

    public int getPosition() {
        return position;
    }

    public Long getMovieID() {
        return movieID;
    }

    public Long getPersonID() {
        return personID;
    }

    public String getSuccessString() {
        return successString;
    }

    public void setMovCharID(Long movCharID) {
        this.movCharID = movCharID;
        updateSuccessString();
    }

    public void setCharacter(String character) {
        this.character = character;
        updateSuccessString();
    }

    public void setAlias(String alias) {
        this.alias = alias;
        updateSuccessString();
    }

    public void setPosition(int position) {
        this.position = position;
        updateSuccessString();
    }

    public void setMovieID(Long movieID) {
        this.movieID = movieID;
        updateSuccessString();
    }

    public void setPersonID(Long personID) {
        this.personID = personID;
        updateSuccessString();
    }

    public void setSuccessString(String successString) {
        this.successString = successString;
        updateSuccessString();
    }

    private void updateSuccessString() {
        successString = " von Zeile in movieCharacter mit movCharID:" + movCharID + ", character: " + character  +
                ", alias: " + alias + ", position: " + position + " und MovieID: " + movieID + " erfolgreich.";
    }

    public MovieCharacter(Long movCharID, String character, String alias, int position, Long movieID, Long personID) {
        this.movCharID = movCharID;
        this.character = character;
        this.alias = alias;
        this.position = position;
        this.movieID = movieID;
        this.personID = personID;
    }

    public MovieCharacter() {

    }


    public void insert() {
        try {
            String sql = "INSERT INTO moviecharacter (movCharID, character, alias, position, movieID, personID) VALUES (?, ?, ?, ?, ?, ?)";
            Connection conn = DBConnection.getConnection();
            generatePosition();
            generateMovCharId();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movCharID);
                stmt.setString(2, character);
                stmt.setString(3, alias);
                stmt.setInt(4, position);
                stmt.setLong(5, movieID);
                stmt.setLong(6, personID);
                stmt.executeUpdate();

                System.out.println("Einfügen " + successString);
            } catch (Exception e) {
                System.out.println("Fehler beim Einfügen von movieCharacter mit movCharID: " + movCharID + ", character: " + character +
                        ", alias: " + alias +   ", position: " + position + " und MovieID: " + movieID + ".");
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Einfügen des Charakters: "+e.getMessage());
        }



    }

    private void generateMovCharId() throws SQLException {
        String sql = "SELECT MAX(MovCharID) FROM movieCharacter";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    movCharID = rs.getLong(1)+1;
                }
                updateSuccessString();
            }

        }
    }
    private void generatePosition() throws SQLException {
        String sql = "SELECT MAX(Position) FROM movieCharacter";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    position = rs.getInt(1)+1;
                }
                updateSuccessString();
            }
        }
    }

    public void delete() {
        try {
            String sql = "DELETE FROM moviecharacter WHERE movieID = ?";
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movieID);
                stmt.executeUpdate();
                conn.commit();
                System.out.println("Löschen " + successString);
            }

        } catch (Exception e){
            System.out.println("Fehler beim Löschen von movieCharacter mit movCharID: " + movCharID + ", character: " + character +
                    ", alias: " + alias +   ", position: " + position + " und MovieID: " + movieID + ".");
            System.out.println(e.getMessage());
        }
    }

    public void update() {
        try {
            String sql = "UPDATE moviecharacter SET movCharID = ?, character = ?, alias = ?, position = ?, movieID = ?, personID = ? " +
                    "WHERE movCharID = ?";
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movCharID);
                stmt.setString(2, character);
                stmt.setString(3, alias);
                stmt.setInt(4, position);
                stmt.setLong(5, movieID);
                stmt.setLong(6, personID);
                stmt.setLong(7, movCharID);
                stmt.executeUpdate();
                // conn.commit();
                System.out.println("Updaten " + successString);
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Updaten von movieCharacter mit movCharID: " + movCharID + ", character: " + character +
                    ", alias: " + alias +   ", position: " + position + ", MovieID: " + movieID + ".");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "movCharID: " + movCharID + ", character: " + character + ", alias: " + alias + ", position: " + position +
                ", movieID: " + movieID + ", personID: " + personID + ".";
    }
}

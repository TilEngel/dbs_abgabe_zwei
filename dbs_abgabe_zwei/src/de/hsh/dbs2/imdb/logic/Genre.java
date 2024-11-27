package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Genre {
    private String genre;
    private Long genreID;

    public Genre() {

    }

    public Genre(String Genre, Long genreID) {
        this.genre = Genre;
        this.genreID = genreID;
    }

    public Genre(String Genre) {
        this.genre = Genre;
    }

    //Getter und Setter
    public String getGenre() {return genre;}
    public Long getGenreID() {return genreID;}
    public void setGenre(String Genre) {this.genre = Genre;}
    public void setGenreID(Long GenreID) {this.genreID = GenreID;}

    /** Fügt einen Datensatz in die Datenbank ein, sofern keine ID gesetzt ist*/
    public void insert() throws Exception {
        if (this.genreID != null) {
            throw new Exception("ID ist bereits gesetzt. Einfügen nicht möglich!");
        }

        // SQL für die Berechnung der nächsten GenreID und Einfügen des neuen Genres
        String getIdSql = "SELECT MAX(GenreID) FROM Genre";
        String insertSql = "INSERT INTO Genre (GenreID, Genre) VALUES (?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                // Ausführen und GenreID berechnen
                try (ResultSet rs = getIdStmt.executeQuery()) {
                    if (rs.next()) {
                        this.genreID = rs.getLong(1)+ 1;
                    }
                }
                insertStmt.setLong(1, this.genreID);
                insertStmt.setString(2, genre);
                // Insert ausführen
                insertStmt.executeUpdate();
                System.out.println("Genre mit ID " + this.genreID + " erfolgreich eingefügt.");

            }
        } catch (Exception e) {
            throw new Exception("Genre: Einfügen des Genres fehlgeschlagen: " + e.getMessage());
        }
    }

    /** Aktualisiert den Wert für Genre */
    public void update() throws Exception {
        if (this.genreID == null) {
            throw new Exception("ID wurde nicht gesetzt!");
        }
        String sql = "UPDATE Movie SET genre = ? WHERE GenreID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, genre);
                stmt.setLong(2, genreID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception("Genre: Aktualisieren des Genres fehlgeschlagen: " + e.getMessage());
        }


    }
    /** Löscht das Genre aus der Datenbank */
    public void delete() throws Exception {
        if (this.genreID == null) {
            throw new Exception("Kann nicht gelöscht werden, da es nicht existiert.");
        }
        String sql = "DELETE FROM Genre WHERE GenreID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, genreID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception("Genre: Löschen des Genres fehlgeschlagen: " + e.getMessage());
        }
    }
}

package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreFactory {
    /**
     * Sucht ein Genre mit einer gegebenen ID und gibt die gesamten Daten zum Genre zurück */
    public static Genre findByGenreId(long id) throws Exception {
        String sql = "SELECT * FROM Genre WHERE GenreID = ?";
        try  {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return getResults(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("GenreFactory: Fehler beim Abfragen des Genres mid ID " + id + ". " + e.getMessage());
        }
        return null;
    }


    /** Für getGenreList im GenreManager für den Fall, dass der String leer ist *Deprecated*? */
    public static List<Genre> findAll() throws Exception {
        String sql = "SELECT * FROM Genre";
        List<Genre> genres = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        genres.add(getResults(rs));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Finden aller Genres: " + e.getMessage());
        }
        return genres;
    }
    /** Sucht die ID eines Genres */
    public static Long findByGenre(String genreName) throws Exception {
        String sql = "SELECT * FROM Genre WHERE Genre = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, genreName);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    return rs.getLong("GenreID");
                }
            }
        } catch (Exception e) {
            throw new Exception("GenreFactory: Fehler beim Abfragen der GenreID " + genreName + ". " + e.getMessage());
        }
    }


    public static List<String> getGenres() throws Exception {
        String sql = "SELECT * FROM Genre";
        List<String> genres = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                            genres.add(rs.getString("genre"));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("GenreFactory: Fehler beim Abfragen der Genres. " + e.getMessage());
        }
        return genres;
    }

    /** Hilfsklasse, um Redundanz bei findByTitle und findAll zu vermeiden */
    private static Genre getResults(ResultSet rs) throws SQLException {
        Long id = rs.getLong("GenreID");
        String title = rs.getString("Genre");
        return new Genre(title, id);
    }
}

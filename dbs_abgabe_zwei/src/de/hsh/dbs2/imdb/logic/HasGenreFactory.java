package de.hsh.dbs2.imdb.logic;
import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class HasGenreFactory {
    public static void deleteMovieGenres(Long movieID) throws Exception {
        HasGenre hg = new HasGenre(movieID);
        hg.delete();
    }

    /** Liefert die Genres zu einem Film zurück */
    public static Set<String> findById(Long movieID) throws Exception {
        String sql = "SELECT * FROM hasGenre WHERE movieID = ?";
        Set<String> hasGenres = new HashSet<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movieID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        long genreID = rs.getLong("genreID");
                        hasGenres.add(GenreFactory.findByGenreId(genreID).getGenre());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("HasGenreFactory: Fehler beim Auslesen der HasGenres von movieID: " + movieID + "." + e.getMessage());
        }
        return hasGenres;
    }
}

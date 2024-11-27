package de.hsh.dbs2.imdb.logic;
import de.hsh.dbs2.imdb.util.DBConnection;

import java.util.*;
import java.sql.*;

public class MovieFactory {

    /** 4.4
     * Sucht einen Film mit einer gegebenen ID und gibt die gesamten Daten zum Film mit der angegebenen ID zurück */
    public static Movie findById(long id) throws Exception {
        String sql = "SELECT * FROM Movie WHERE MovieID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return getResults(rs);
                    } else {
                        throw new Exception("Film mit der ID: " + id + " nicht vorhanden.");
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("MovieFactory: Film mit der ID: " + id + " nicht gefunden: " + e.getMessage());
        }
    }
    /** Für getMovieList im MovieManager für den Fall, dass der String leer ist*/
    public static List<Movie> findAll() throws Exception {
        String sql = "SELECT * FROM Movie ORDER BY movieID";
        List<Movie> movies = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(getResults(rs));
                }
            }
        } catch (Exception e) {
            throw new Exception("MovieFactory: Fehler beim Suchen aller Filme: " + e.getMessage());
        }
        return movies;
    }
    /** Sucht alle Filme, die den angegebenen Titel enthalten, ungeachtet auf Groß-/Kleinschreibung */
    public static List<Movie> findByTitle(String title) throws Exception {
        String sql = "SELECT * FROM Movie WHERE LOWER(Title) LIKE ? ORDER BY movieID";
        List<Movie> movies = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + title.toLowerCase() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        movies.add(getResults(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("MovieFactory: Fehler beim Suchen des Films mit dem Titel: "+ title + " : " + e.getMessage());
        }
        return movies;
    }

    /** Eine dynamische deleteMovie Methode, die einen Film und die dazugehörigen Charaktere und Genres löscht*/
    public static void deleteById(long movieId) throws Exception {
        try {
            HasGenreFactory.deleteMovieGenres(movieId);
            MovieCharacterFactory.deleteByMovieID(movieId);
            Movie movie = MovieFactory.findById(movieId);
            if (movie != null) {
                movie.delete();
            }
        } catch (Exception e) {
            throw new Exception("Movie Factory: Fehler beim Löschen des Films: " + e.getMessage());
        }
    }
    /** Hilfsklasse, um Redundanz bei findByTitle und findAll zu vermeiden */
    private static Movie getResults(ResultSet rs) throws SQLException {
        Long id = rs.getLong("MovieID");
        String title = rs.getString("Title");
        int year = rs.getInt("Year");
        char type = rs.getString("Type").charAt(0);
        return new Movie(id, title, year, type);
    }



}

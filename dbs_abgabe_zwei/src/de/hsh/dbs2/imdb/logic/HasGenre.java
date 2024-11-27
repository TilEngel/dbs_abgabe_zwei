package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HasGenre {
    private final Long MovieID;
    private Long GenreID = null;

    public Long getMovieID() {
        return MovieID;
    }

    public Long getGenreID() {
        return GenreID;
    }

    public void setGenreID(Long genreID) {
        GenreID = genreID;
    }


    public HasGenre(Long movieID, Long genreID) {
        this.MovieID = movieID;
        this.GenreID = genreID;
    }

    public HasGenre(Long movieID) {
        this.MovieID = movieID;
    }

    /** Fügt Genre in einen Film ein*/
    public void insert() throws Exception {
        String sql = "INSERT INTO hasgenre (MovieID, GenreID) VALUES (?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, MovieID);
                stmt.setLong(2, GenreID);
                stmt.executeUpdate();
                System.out.println("Einfügen von Zeile in hasGenre mit MovieID: " + MovieID + " und GenreID: " + GenreID + " erfolgreich.");
            }
        } catch (Exception e) {
            throw new Exception("HasGenre: Fehler beim Einfügen von hasGenre mit MovieID: " + MovieID + " und GenreID: " + GenreID + ". " + e.getMessage());
        }
    }

    /**
     * Löscht entweder eine Reihe des Films oder den gesamnten Film
     */
    public void delete() throws Exception {
            String sql = "DELETE FROM hasgenre WHERE MovieID = ?";
            try {
                Connection conn = DBConnection.getConnection();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, MovieID);
                    stmt.executeUpdate();
                }

            } catch (Exception e) {
                throw new Exception("HasGenre: Fehler beim Löschen von Film in hasGenre mit MovieID:" + MovieID + ". " + e.getMessage());
            }

    }
    /** Eine toString Methode für HasGenre */
    @Override
    public String toString() {
        return "HasGenre: MovieID: " + MovieID + " GenreID: " + GenreID + ".";
    }
}


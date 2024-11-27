package de.hsh.dbs2.imdb.logic;
import de.hsh.dbs2.imdb.util.DBConnection;

import java.lang.invoke.WrongMethodTypeException;
import java.sql.*;
import java.util.List;

/** 4.3 Active Record Klasse für Movie*/
public class Movie {
    private Long movieID; // Kann null sein, wenn nicht gesetzt
    private String title;
    private int year;
    private char type;

    /** Überladung von Konstruktoren */
    public Movie() {

    }
    public Movie(String title, int year, char type) {
        this.title = title;
        this.year = year;
        this.type = type;
    }
    public Movie(Long id, String title, int year, char type) {
        this.movieID = id;
        this.title = title;
        this.year = year;
        this.type = type;
    }

    //Setter und Getter
    public Long getMovieID() {
        return movieID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public char getType() {
        return type;
    }
    public void setType(char type) {
        this.type = type;
    }

    /** Fügt einen Datensatz in die Datenbank ein, sofern keine ID gesetzt ist*/
    public void insert() throws Exception {
        if (this.movieID != null) {
            throw new Exception("ID ist bereits gesetzt. Einfügen nicht möglich!");
        }

        String getIdSql = "SELECT MAX(MovieID) FROM Movie";
        String insertSql = "INSERT INTO Movie (MovieID, Title, Year, Type) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();

            try (PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                // Hole die nächste Sequenznummer
                try (ResultSet rs = getIdStmt.executeQuery()) {
                    if (rs.next()) {
                        this.movieID = rs.getLong(1)+1;// Speichere die nächst höhere ID
                    } else {
                        throw new Exception("Es konnte keine Sequenznummer generiert werden!");
                    }
                }

                insertStmt.setLong(1, this.movieID); // Verwende die vorher generierte ID
                insertStmt.setString(2, title);
                insertStmt.setInt(3, year);
                insertStmt.setString(4, String.valueOf(type));
                insertStmt.executeUpdate();
                System.out.println("Film mit der ID: " + this.movieID + " wurde eingefügt.");
            }
        } catch (Exception e) {
            throw new Exception("Movie: Einfügen neuer Werte ist fehlgeschlagen: " + e.getMessage());
        }
    }

    /** Aktualisiert die Werte für Title, Year und Type der Datenbank für den Film */
    public void update() throws Exception {
        if (this.movieID == null) {
            throw new Exception("ID wurde nicht gesetzt!");
        }
        String sql = "UPDATE Movie SET Title = ?, Year = ?, Type = ? WHERE MovieID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setInt(2, year);
                stmt.setString(3, String.valueOf(type));
                stmt.setLong(4, movieID);
                stmt.executeUpdate();
                System.out.println("Film mit der ID: "+ this.movieID + " wurde geändert.");
                System.out.println("Film Name lautet: "+this.title);
            }
        } catch (Exception e) {
            throw new Exception("Movie: Aktualisieren des Films fehlgeschlagen: " + e.getMessage());
        }


    }
    /** Löscht den Film aus der Datenbank */
    public void delete() throws Exception {
        if (this.movieID == null) {
            throw new Exception("Kann nicht gelöscht werden, da es nicht existiert.");
        }
        String sql = "DELETE FROM Movie WHERE MovieID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movieID);
                stmt.executeUpdate();
                System.out.println("Film mit der ID: "+ this.movieID + " wurde gelöscht.");
            }
        } catch (Exception e) {
            throw new Exception("Movie: Löschen des Films fehlgeschlagen: " + e.getMessage());
        }
    }




    /** 4.5 Testmethode für Movie*/
    public static void testMovie() {
        try {
            // Neues Movie-Objekt erstellen und speichern
            Movie movie = new Movie("Joker3", 2024, 'C');
            movie.insert();
            System.out.println("Film hinzugefügt mit ID: " + movie.getMovieID() + " Titel: " + movie.getTitle() + " Jahr: " + movie.getYear());

            // Movie-Objekt aktualisieren
            movie.setTitle("Joker33 (Updated)");
            movie.update();
            System.out.println("Filmtitel heißt jetzt: " + movie.getTitle());

            // Movie nach ID suchen
            Movie foundMovie = MovieFactory.findById(movie.getMovieID());
            System.out.println("Film gefunden: " + foundMovie.getTitle());

            // Movie(s) nach Titel suchen
            List<Movie> movies = MovieFactory.findByTitle("Joker2");
            System.out.println("Filme gefunden mit dem Namen 'Joker2': " + movies.size());

            // Movie löschen
            movie.delete();
            System.out.println("Film mit ID gelöscht: " + movie.getMovieID());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

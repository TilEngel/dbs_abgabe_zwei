package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;
import de.hsh.dbs2.imdb.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieCharacterFactory {
    public static void main(String[] args) throws Exception {
        List<MovieCharacter> mc = MovieCharacterFactory.findByPersonId(30L);
        for (MovieCharacter mv : mc) {
            System.out.println(mv);
        }
        MovieCharacterFactory.deleteByMovieID(12L);
    }
    /** Gibt eine Liste von MovieCharacter zurück, die mittels der entsprechenden Film ID gescuht werden */
    public static List<MovieCharacter> findByMovieId(Long movieID) throws Exception {
        String sql = "SELECT * FROM moviecharacter WHERE movieID = ? ORDER BY position";
        List<MovieCharacter> movieCharacters = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, movieID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        movieCharacters.add(getResults(rs));
                    }
                }
            }

        } catch (Exception e) {
            throw new Exception("MovieCharacterFactory: Fehler beim Auslesen der MovieCharacters mit movieID: " + movieID + ". " +  e.getMessage());
        }
        return movieCharacters;
    }
    /** Liefert eine Liste von CharacterDTOS zum Anzeigen in der UI zurück */
    public static List<CharacterDTO> getCharDTOByMovieID(Long movieID) throws Exception {
        List<MovieCharacter> mcs = findByMovieId(movieID);
        List<CharacterDTO> cds = new ArrayList<>();
        for (MovieCharacter mc : mcs) {
            CharacterDTO cd = new CharacterDTO();
            cd.setCharacter(mc.getCharacter());
            cd.setAlias(mc.getAlias());
            cd.setPlayer(PersonFactory.getPersonName(mc.getPersonID()));
            cds.add(cd);
        }
        return cds;
    }
    /** Liefert eine Liste von MovieCharacter, die über die Personen ID geuscht wird */
    public static List<MovieCharacter> findByPersonId(Long personID) throws Exception {
        String sql = "SELECT * FROM moviecharacter WHERE personID = ?";
        List<MovieCharacter> movieCharacters = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, personID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        movieCharacters.add(getResults(rs));
                    }
                }
            }

        } catch (Exception e) {
            throw new Exception("MovieCharacterFactory: Fehler beim Auslesen der MovieCharacters mit movieID: " + personID + ". " + e.getMessage());

        }
        return movieCharacters;
    }

    /** Löscht einen MovieCharacter aus einem Film mit der gegebenen Film ID */
    public static void deleteByMovieID(Long movieID) {
        MovieCharacter mc = new MovieCharacter();
        mc.setMovieID(movieID);
        mc.delete();
    }

    /** Eine Hilfsklasse, um Redundanz zu vermeiden gibt einen MovieCharacter mit seinen zugehörigen Daten zurück */
    private static MovieCharacter getResults(ResultSet rs) throws SQLException {
        Long movCharID = rs.getLong("movCharID");
        String character = rs.getString("character");
        String alias = rs.getString("alias");
        int position = rs.getInt("position");
        Long movieID = rs.getLong("movieID");
        Long personID = rs.getLong("personID");
        return new MovieCharacter(movCharID, character, alias, position, movieID, personID);
    }
}

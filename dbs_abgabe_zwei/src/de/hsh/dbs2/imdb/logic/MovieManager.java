package de.hsh.dbs2.imdb.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.logic.dto.*;
import de.hsh.dbs2.imdb.util.DBConnection;

public class MovieManager {

	/**
	 * Ermittelt alle Filme, deren Filmtitel den Suchstring enthaelt.
	 * Wenn der String leer ist, sollen alle Filme zurueckgegeben werden.
	 * Der Suchstring soll ohne Ruecksicht auf Gross/Kleinschreibung verarbeitet werden.
	 * @param search Suchstring. 
	 * @return Liste aller passenden Filme als MovieDTO
	 * @throws Exception
	 */
	public List<MovieDTO> getMovieList(String search) throws Exception {
		List<MovieDTO> movies = new ArrayList<>();

		//Prüfung, ob der Suchstring leer ist
		if (search == null || search.isEmpty()) {
			List<Movie> allMovies = new MovieFactory().findAll();
			for (Movie movie : allMovies) {
				movies.add(convertToMovieDTO(movie));
			}
		} else {
			List<Movie> movieList = new MovieFactory().findByTitle(search);
			for (Movie movie : movieList) {
				movies.add(convertToMovieDTO(movie));
			}
		}
		return movies;
	}

	/**
	 * Speichert die uebergebene Version des Films neu in der Datenbank oder aktualisiert den
	 * existierenden Film.
	 * Dazu werden die Daten des Films selbst (Titel, Jahr, Typ) beruecksichtigt,
	 * aber auch alle Genres, die dem Film zugeordnet sind und die Liste der Charaktere
	 * auf den neuen Stand gebracht.
	 * @param movieDTO Film-Objekt mit Genres und Charakteren.
	 * @throws Exception
	 */

	public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			Movie movie;
			/* Herausfinden, ob es sich hierbei um ein Update oder Insert handelt */
			if (movieDTO.getId() != null) {
				// Update: da eine ID besteht
				movie = MovieFactory.findById(movieDTO.getId());
				if (movie == null) {
					throw new Exception("MovieManager: Film mit der ID " + movieDTO.getId() + " nicht gefunden.");
				}
			} else {
				//Insert, neuen Film anlegen
				movie = new Movie();
			}
			// Werte vom DTO auf den Film selbst übertragen
			movie.setTitle(movieDTO.getTitle());
			movie.setYear(movieDTO.getYear());
			movie.setType(movieDTO.getType().charAt(0));

			//Überprüfung welche Operation zutrifft
			if (movie.getMovieID() == null) {
				movie.insert();
				conn.commit();
			} else {
				movie.update();
				conn.commit();
			}

			//Genres aktualisieren (erst löschen, dann laden)
			HasGenreFactory.deleteMovieGenres(movie.getMovieID());
			for (String genreName : movieDTO.getGenres()) {
				HasGenre hasGenre = new HasGenre(movie.getMovieID(),GenreFactory.findByGenre(genreName));
				hasGenre.insert();
			}
			// Charakter löschen und laden
			MovieCharacterFactory.deleteByMovieID(movie.getMovieID());
			for (CharacterDTO characterDTO : movieDTO.getCharacters()) {
				MovieCharacter character = new MovieCharacter();
				character.setMovieID(movie.getMovieID());
				character.setPersonID(PersonFactory.getPersonID(characterDTO.getPlayer()));
				character.setCharacter(characterDTO.getCharacter());
				character.setAlias(characterDTO.getAlias());
				character.setMovieID(movie.getMovieID());
				character.insert();
				System.out.println(character);
			}
			// Nur wenn es erfolgreich war
			conn.commit();
			System.out.println("Transaktion erfolgreich!");

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			throw new Exception("Die Transaktion ist fehlgeschlagen: " + e.getMessage());
		}
	}

	/**
	 * Loescht einen Film aus der Datenbank. Es werden auch alle abhaengigen Objekte geloescht,
	 * d.h. alle Charaktere und alle Genre-Zuordnungen.
	 * @param movieid
	 * @throws Exception
	 */

	public void deleteMovie(long movieid) throws Exception {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			MovieFactory.deleteById(movieid);
			HasGenreFactory.deleteMovieGenres(movieid);
			MovieCharacterFactory.deleteByMovieID(movieid);
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			throw new Exception("MovieManager: Fehler beim Löschen des Films mit der ID " + movieid + " -" + e.getMessage());
		}
	}

	/**
	 * Liefert die Daten eines einzelnen Movies zurück
	 * @param movieId
	 * @return
	 * @throws Exception
	 */

	public MovieDTO getMovie(long movieId) throws Exception {

		Movie movie = new MovieFactory().findById(movieId);
		if (movie == null) {
			throw new Exception("MovieManager: Film mit ID " + movieId + " nicht gefunden.");
		}

		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(movie.getMovieID());
		movieDTO.setTitle(movie.getTitle());
		movieDTO.setYear(movie.getYear());


		movieDTO.setGenres(HasGenreFactory.findById(movie.getMovieID()));

		movieDTO.setCharacters(MovieCharacterFactory.getCharDTOByMovieID(movie.getMovieID()));

		return movieDTO;
	}
	/** Hilfsmethode für Performance: Umwandeln von Movies in DTO Objekte anstatt immer neue MovieDTO Objekte anzulegen */
	private MovieDTO convertToMovieDTO (Movie movie) {
		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(movie.getMovieID());
		movieDTO.setTitle(movie.getTitle());
		movieDTO.setYear(movie.getYear());
		movieDTO.setType(Character.toString(movie.getType()));
		return movieDTO;
	}


}

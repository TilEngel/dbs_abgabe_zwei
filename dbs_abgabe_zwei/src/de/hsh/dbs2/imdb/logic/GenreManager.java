package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;
import de.hsh.dbs2.imdb.util.DBConnection;
import java.sql.*;

public class GenreManager {

	/**
	 * Ermittelt eine vollstaendige Liste aller in der Datenbank abgelegten Genres
	 * Die Genres werden alphabetisch sortiert zurueckgeliefert.
	 * @return Alle Genre-Namen als String-Liste
	 * @throws Exception
	 */

	public List<String> getGenres() throws Exception {
		return GenreFactory.getGenres();
	}

}

package de.hsh.dbs2.imdb.logic;

import java.util.List;

public class PersonManager {

	/**
	 * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
	 * @param text Suchstring
	 * @return Liste mit passenden Personennamen, die in der Datenbank eingetragen sind.
	 * @throws Exception
	 */
	public List<String> getPersonList(String text) throws Exception {
		return PersonFactory.getPersonList(text);
	}
}

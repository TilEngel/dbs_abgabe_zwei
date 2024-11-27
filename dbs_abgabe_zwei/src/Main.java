import java.sql.*;

/**
 * Wird für die Aufgaben 4.1 und 4.2 verwendet
 */
public class Main {
    private static Connection conn;
    public static void main(String[] args) {
        try{
            conn = DriverManager.getConnection("jdbc:oracle:thin:@<host>:<port>/db01", "Name", "SuperGeheimesPasswort");
            aufgabe4Punkt1(-1);
        } catch (SQLException e){
            System.out.println("Message: "+e.getMessage());
        }
    }
    // AUFGABE 4.1
    public static void aufgabe4Punkt1(int id){
        try{
            if(id == -1){
                String sql = "SELECT title FROM moviedb2.movie WHERE id BETWEEN 600000 AND 600100";
                Statement stmt = conn.prepareStatement(sql);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while(rs.next()) {
                        System.out.println(rs.getString(0));
                        rs.next();
                    }
                }
            }else {
                String sql = "SELECT title FROM moviedb2.movie WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(rs.getString(0));
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Fehler: "+ e.getMessage());
        }
    }
}
//AUFGABE 4.2
class A4Punkt2 {
    static Connection conn;
    public A4Punkt2(Connection conn) {
        this.conn = conn;
        aufgabe4d();
        aufgabe4c();
    }
    //Aufgabe 4.2 c)

    /**
     * Führt Aufgabe 4c aus -> Erstellt die Tabellen mit den entsprechenden Attributen und Constraints.
     * Hier werden String-Arrays mit den Attributen gefüllt und dann die Tatsächliche tabelleErstellen() Methode ausgeführt
     */
    public static void aufgabe4c(){
        //Attribute der Tabellen
        String[] movie = {"MovieID ","INTEGER ", "Title ", "VARCHAR(30) NOT NULL","Year ", "INTEGER NOT NULL", "Type ","CHAR(1)"};
        String[] movieCharacter = {"MovCharID","Integer", "Character", "VARCHAR(30) NOT NULL", "ALIAS","VARCHAR(30)", "POSITION", "INTEGER", "MID", "INTEGER"};
        String[] person = {"PersonID", "INTEGER", "Name", "VARCHAR(30) NOT NULL", "SEX", "CHAR(1)"};
        String[] genre = {"GenreID", "INTEGER", "Genre", "VARCHAR(20)"};
        String[] movieGenre = { "MID", "INTEGER", "GID", "INTEGER"};
        String[] plays = {"MovCharID", "INTEGER", "PersonID", "INTEGER"};
        //Funktionsaufrufe
        tabelleErstellen4c("Movie",movie, "PRIMARY KEY (MovieID)");
        tabelleErstellen4c("MovieCharacter",movieCharacter, "PRIMARY KEY (MovCharID), FOREIGN KEY (MID) REFERENCES Movie(MovieID),CONSTRAINT u_Character UNIQUE (Character)");
        tabelleErstellen4c("Person",person, "PRIMARY KEY (PersonID)");
        tabelleErstellen4c("Genre",genre, "PRIMARY KEY (GenreID), CONSTRAINT u_genre UNIQUE (Genre)");
        tabelleErstellen4c("MovieGenre",movieGenre, "PRIMARY KEY (MID, GID), FOREIGN KEY (MID) REFERENCES Movie(MovieID), FOREIGN KEY (GID) REFERENCES Genre(GenreID)");
        tabelleErstellen4c("Plays",plays, "PRIMARY KEY (MovCharID, PersonID), FOREIGN KEY (MovCharID) REFERENCES MovieCharacter(MovCharID), FOREIGN KEY (PersonID) REFERENCES Person(PersonID)");
    }
    /**
     * Erstellt ein SQL Statement, das prepared und ausgeführt wird. Der String wird im Vorhinein zusammengebaut
     * @param name Name der Tabelle
     * @param arr Array mit Attributen
     * @param constraints String, in dem die Constraints stehen
     */
    public static void tabelleErstellen4c(String name,String[] arr, String constraints) {
        //Erstellung des Strings
        String sql = "CREATE TABLE "+name+"(";
        for(int i= 0; i< arr.length; i++){
            sql += arr[i]+ " ";
            if(i%2 != 0){ // Die verschiedenen Attribute werden durch Kommas getrennt
                sql += ",";
            }
        }
        sql += constraints;
        sql += ")";
        //Ausführung des Statements
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Die Operation war erfolgreich und " + affectedRows + " Zeilen wurden betroffen.");
            } else {
                System.out.println("Keine Zeilen wurden betroffen.");
            }
        }catch (SQLException e){
            System.out.println("Message: "+e.getMessage());
        }
        tabelleFuellen4c();
    }

    /**
     * Füllt die Tabellen mit Datensätzen.
     * Hierbei ist wichtig, dass die Tabellen Plays und MovieGenre zuletzt gefüllt werden, da ihre
     * Werte auf denen der anderen Tabellen beruhen
     */
    public static void tabelleFuellen4c() {
        String[] sql = new String[24];
        sql[0] = "INSERT INTO Movie VALUES(10,'Star Wars', 1977,'C')";
        sql[1] = "INSERT INTO Movie VALUES(11,'Kebab63 The Movie',2024,'F')";
        sql[2] = "INSERT INTO Movie VALUES(12, 'BattleShips', 2024,'C')";
        sql[3] = "INSERT INTO MovieCharacter VALUES(20,'Luke Skywalker',NULL,1,10)";
        sql[4] = "INSERT INTO MovieCharacter VALUES(21,'Leia Organa','Prinzessin Leia',2,10)";
        sql[5] = "INSERT INTO MovieCharacter VALUES(22,'Johnny Chau','Johann Tschüss',1,11)";
        sql[6] = "INSERT INTO MovieCharacter VALUES(23,'Lucas Aleman','Sammy Deutsch',2,11)";
        sql[7] = "INSERT INTO MovieCharacter VALUES(24,'Thomas Charles', 'Captain C', 1,12)";
        sql[8] = "INSERT INTO MovieCharacter VALUES(25,'Natalie UBoot',NULL, 2,12)";
        sql[9] = "INSERT INTO Person VALUES(30, 'Mark Hamil','M')";
        sql[10] = "INSERT INTO Person VALUES(31,'Carrie Fisher','F')";
        sql[11] = "INSERT INTO Person VALUES(32,'Till Schweiger','M')";
        sql[12] = "INSERT INTO Genre VALUES(40,'Sci-Fi')";
        sql[13] = "INSERT INTO Genre VALUES(41,'Action')";
        sql[14] = "INSERT INTO Genre VALUES(42,'Romanze')";
        sql[15] = "INSERT INTO MovieGenre VALUES(10,40)";
        sql[16] = "INSERT INTO MovieGenre VALUES(11,41)";
        sql[17] = "INSERT INTO MovieGenre VALUES(12,41)";
        sql[18] = "INSERT INTO Plays VALUES(20,30)";
        sql[19] = "INSERT INTO Plays VALUES(21,31)";
        sql[20] = "INSERT INTO Plays VALUES(22,30)";
        sql[21] = "INSERT INTO Plays VALUES(23,32)";
        sql[22] = "INSERT INTO Plays VALUES(24,30)";
        sql[23] = "INSERT INTO Plays VALUES(25,31)";
        for (int i = 0; i<sql.length; i++) {
            try {
                Statement stmt=conn.createStatement();
                int affectedRows = stmt.executeUpdate(sql[i]);
                if (affectedRows > 0) {
                    System.out.println("Die Operation war erfolgreich und " + affectedRows + " Zeilen wurden betroffen.");
                } else {
                    System.out.println("Keine Zeilen wurden betroffen.");
                }
            }catch (SQLException e){
                System.out.println("Fehler beim Einfügen der Werte: "+e.getMessage());
            }

        }
    }
    //Aufgabe 4.2d)
    /**
     * Ruft je das Löschen der Tabellen auf
     * Wichtig: Wegen der Fremdschlüssel müssen Plays und MovieGenre zuerst gelöscht werden
     */
    public static void aufgabe4d(){
        loeschen4d("Plays");
        loeschen4d("MovieGenre");
        loeschen4d("MovieCharacter");
        loeschen4d("Genre");
        loeschen4d("Person");
        loeschen4d("Movie");
    }

    /**
     * Übernimmt das eigentliche löschen via "DROP TABLE()"
     * @param table Tabelle die gelöscht werden soll
     */
    public static void loeschen4d(String table) {
        String sql = "DROP TABLE " + table;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println(table + " gelöscht");
        }catch (SQLException e){
            System.out.println("Fehler beim Löschen: "+ e.getMessage());
        }
    }
}


package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.game.Game;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Database {
    @Getter private final List<Game> games = new ArrayList<>();

    /**
     * Construct an empty virtual in-memory database. None of the state updates will be synchronized with any
     * physical permanent database. All data will be lost if this object is terminated.
     */
    public Database() {
    }

    /**
     * Connect to a remote or local active MySQL database server. All entries of all tables from the given schema are
     * immediately loaded into memory. If the database contains huge information, this constructor might take a while.
     * Example for local machine:
     *  host = "localhost"
     *  port = "12345"
     *  databaseName = "MyDB57"
     *  username = "dbAdmin"
     *  password = "mySecureDatabasePassword72"
     *  schema = "ajukraanid"
     * @param host The URL or IP address of the remote (or local) machine hosting the database server.
     * @param port The port to which the database accepts connections.
     * @param databaseName The name of the SQL database in the server.
     * @param username The name of a database user which has access to create, read and update entries.
     * @param password The password of that user.
     * @param schema The schema to bind this object to, all tables of the schema with their entries are loaded.
     */
    public Database(String host, int port, String databaseName, String username, String password, String schema) {
        this(); // TODO Implement.
    }
}

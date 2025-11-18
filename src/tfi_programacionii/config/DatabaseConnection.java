package tfi_programacionii.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "database.properties";
    private static Properties properties = new Properties();
    
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("ERROR: No se pudo encontrar el archivo " + PROPERTIES_FILE + " en el classpath.");
                throw new IOException("Archivo de propiedades no encontrado.");
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de propiedades de la base de datos: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Retorna una nueva conexion a la base de datos 
     *
     * @return Una conexion activa a la base de datos
     * @throws SQLException Si ocurre un error al establecer la conexion
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        
        if (url == null || user == null) {
            throw new SQLException("Faltan propiedades de conexion (db.url, db.user) en " + PROPERTIES_FILE);
        }
        
        return DriverManager.getConnection(url, user, password);
    }
}

package co.edu.escuelaing.arep.sparkr;

import java.sql.Connection;
import java.sql.DriverManager;

// import org.postgresql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ricar8o
 * @version 1.0
 */
public class SparkRController {
    
    private String host = "ec2-52-20-248-222.compute-1.amazonaws.com";
    private String port = "5432";
    private String dbName = "dd4tvn6a080b1s";
    private String user  = "brvqrihdjwuaav";
    private String password = "04ae4f64d8af11b91e8288e47188e3f2d06f6063478150994b50ca052ec3aeee";

    private Statement statement;
    private Connection connection;

    public SparkRController() {
        this.tryConnect();
        this.updateFuntions();
    }
    /**
     *  Metodo para crear la conexion a la base de datos.
     */
    public void test() {
        try {
            statement = this.connection.createStatement();
            System.out.printf("%-30.30s  %-30.30s%n", "Titulo", "Autor");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.libros");
            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("titulo"), resultSet.getString("autor"));
            }
 
        }catch (SQLException e) {
            System.out.println("Connection failure.");
            // e.printStackTrace();
        }
    }

    public void tryConnect(){
        String url = "";
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex.getMessage());
            }
            url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            // Database connect
            // Conectamos con la base de datos
            this.connection = DriverManager.getConnection(url,user, password);           
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) { 
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
        }
    }

    private void updateFuntions() {
    }
    
}

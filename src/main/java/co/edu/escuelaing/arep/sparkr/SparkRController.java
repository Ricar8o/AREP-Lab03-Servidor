package co.edu.escuelaing.arep.sparkr;

import java.sql.Connection;
import java.sql.DriverManager;

// import org.postgresql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Constructor
     */
    public SparkRController() {
        this.tryConnect();
        this.updateFuntionsToSparkR();
    }
    /**
     * Metodo para probar la conexion a la base de datos.
     */
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
    /**
     * Metodo que carga las funciones a SparkR
     */
    private void updateFuntionsToSparkR() {
        SparkR.get("/books", (req,res) -> getBooksInfo(req,res));
        SparkR.get("/booksByAuthor", (req,res) -> getBooksByAuthor(req,res));
        SparkR.get("/booksByYear", (req,res) -> getBooksByYear(req,res));
    }
    
    /**
     * Consultar toda la informacion de los libros.
     * @return
     */
    public String getBooksInfo(String req, String res){
        String select ="SELECT * FROM public.libros";
        return makeSelect(select);
    }

    /**
     * Consultar toda la informacion de los libros ordenados por autor.
     * @return
     */
    public String getBooksByAuthor(String req, String res){
        String select ="SELECT autor, codigo, titulo, año FROM public.libros ORDER BY autor";
        return makeSelect(select);
    }

    /**
     * Consultar toda la informacion de los libros ordenados por año de publicacion.
     * @return
     */
    public String getBooksByYear(String req, String res){
        String select ="SELECT año, codigo, titulo, autor FROM public.libros ORDER BY año";
        return makeSelect(select);
    }

    /**
     * Metodo para hacer una consulta a la base de datos.
     * @param select Consulta 
     * @return respuesta de la consulta.
     */
    public String makeSelect(String select) {
        try {
            statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            ResultSetMetaData rMetaData = resultSet.getMetaData();
            List<String> nombres = new ArrayList<String>();
            int numCol = rMetaData.getColumnCount();
            String consulta = "";
            for(int i = 1; i<= numCol;i++){
                nombres.add(rMetaData.getColumnName(i));
                // consulta += rMetaData.getColumnName(i) + ", ";
            }
            // consulta += "\n";
            while (resultSet.next()) {
                String linea = "";
                for(int i = 0; i<numCol-1;i++){
                    linea +=  resultSet.getString(nombres.get(i)) + ", ";
                }
                linea += resultSet.getString(nombres.get(numCol-1)) ;
                consulta += linea + "\n";
            }
            return consulta;
 
        }catch (SQLException e) {
            System.out.println("Connection failure.");
            return "Consulta Fallida";
        }
    }
    
}

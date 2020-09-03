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

    public SparkRController() {
        this.tryConnect();
        this.updateFuntionsToSparkR();
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

    private void updateFuntionsToSparkR() {
        SparkR.get("/books", (req,res) -> getBooksInfo(req,res));
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
     * Metodo para a la base de datos.
     * 
     * @return
     */
    public String makeSelect(String select) {
        try {
            statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            ResultSetMetaData rMetaData = resultSet.getMetaData();
            List<String> nombres = new ArrayList<String>();
            int numCol = rMetaData.getColumnCount();
            for(int i = 1; i<= numCol;i++){
                nombres.add(rMetaData.getColumnName(i));
            }
            String consulta = "";
            while (resultSet.next()) {
                String linea = "{";
                for(int i = 0; i<numCol-1;i++){
                    linea += nombres.get(i) + ": " + resultSet.getString(nombres.get(i)) + ", ";
                }
                linea += nombres.get(numCol-1) + ": " + resultSet.getString(nombres.get(numCol-1)) ;
                consulta += linea + "}";
            }
            consulta += "";
            System.out.println(consulta);
            return consulta;
 
        }catch (SQLException e) {
            System.out.println("Connection failure.");
            // e.printStackTrace();
            return "Consulta Fallida";
        }
    }
    
}

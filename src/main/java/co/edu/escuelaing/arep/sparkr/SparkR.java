package co.edu.escuelaing.arep.sparkr;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SparkR {

    private static Map<String,BiFunction<String, String, String>> direcciones = new HashMap<String,BiFunction<String, String, String>>();

    public static void get(String path, BiFunction<String, String, String> function){
        // if servidor web esta apagado, arrancarlo
        // almacenar path y funcion en registro de funciones
        direcciones.put(path, function);

        System.out.println(direcciones.keySet());
        
        
    }
}
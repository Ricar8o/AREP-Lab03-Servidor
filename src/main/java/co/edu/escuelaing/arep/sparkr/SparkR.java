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

    public static String get(String path){
        String[] partes = path.split("\\/");
        String ruta = "";
        String argumento = partes[partes.length-1];
        for(int i = 1 ; i<partes.length-1; i++){
            ruta += "/" + partes[i];
        }
        // System.out.println("ruta: " + ruta + "  Arg = "+ argumento);
        BiFunction<String, String, String> function  = direcciones.get(ruta);
        String response = function.apply(argumento, null);
        return response;
    }
    
}
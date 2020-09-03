package co.edu.escuelaing.arep.sparkr;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
/**
 * @author Ricar8o 
 * @version 1.0
 */
public class SparkR {

    private static Map<String,BiFunction<String, String, String>> direcciones = new HashMap<String,BiFunction<String, String, String>>();
    
    /**
     * Medoto que guarda la funcion y la asocia con la ruta.
     * @param path Direccion de la ruta.
     * @param function Funcion que se ejecutara con la ruta.
     */
    public static void get(String path, BiFunction<String, String, String> function){
        direcciones.put(path, function);
        // System.out.println(direcciones.keySet());
        
    }

    /**
     * Metodo para obtener la respuesta de una funcion, a partir de la ruta.
     */
    public static String get(String path){
        String[] partes = path.split("\\/");
        String ruta = "";
        String argumento = partes[partes.length-1];
        for(int i = 1 ; i<partes.length-1; i++){
            ruta += "/" + partes[i];
        }
        String response = null;
        BiFunction<String, String, String> function = null;
        if(validate(ruta, argumento)){
            if (direcciones.containsKey(ruta)){
                function  = direcciones.get(ruta);
                response = function.apply(argumento, "");
            }else if (direcciones.containsKey(ruta+"/"+argumento)){
                function  = direcciones.get(ruta+"/"+argumento);
                response = function.apply("", "");
            }
            // System.out.println("ruta: " + ruta + "  Arg = "+ argumento);
        }
        
        return response;
    }  

    /**
     * Metodo que valida si la la ruta solicitada es valida.
     * @param ruta Ruta a evaluar.
     * @param argumento Parte final de la ruta a evaluar.
     * @return true si solo hay una funcion que corresponga a la petición.
     */
    private static boolean validate(String ruta, String argumento) {
        int cont = 0;
        if (direcciones.containsKey(ruta)){
            cont +=1;
        }if (direcciones.containsKey(ruta+"/"+argumento)){
            cont +=1;
        }
        boolean resp = false;
        if (cont==1){
            resp = true;
        }     
        return resp; 
    }
}
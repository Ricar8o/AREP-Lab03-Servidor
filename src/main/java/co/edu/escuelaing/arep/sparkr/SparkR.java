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
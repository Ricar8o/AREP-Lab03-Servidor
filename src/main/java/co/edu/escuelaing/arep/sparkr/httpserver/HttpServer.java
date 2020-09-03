package co.edu.escuelaing.arep.sparkr.httpserver;

import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.escuelaing.arep.sparkr.SparkR;

import java.io.*;
/**
 * @author Ricar8o
 * @version 1.1
 * Servidor web basico para el framework
 */
public class HttpServer {
    private int port = 36000;
    private boolean running = false;
    private String[] imagenes = {"jpg","png","gif","svg" };
    private String[] textos = {"html","js","css","txt" };
    /**
     * Constructor
     */
    public HttpServer(){
        this.port = getPort();
    }
    /**
     * Constructor
     * @param port puerto por el que desea iniciar el servidor.
     */
    public HttpServer(int port){
        this.port = port;
    }
    /**
     * Metodo para iniciar el servidor si se encuentra apagado.
     */
    public void start() {
        try{
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port);
                System.exit(1);
            }
            running= true;
            while (running) {
                Socket clientSocket = null;
                try {
                    System.out.println("Listo para recibir ...");
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.err.println("Accept failed.");
                    System.exit(1);
                }
                processRequest(clientSocket);
                
                clientSocket.close();
            }
            serverSocket.close();
        }catch(IOException e){
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Metodo que se encarga de procesar las peticiones.
     * @param clientSocket Socket de conexion con el cliente
     * @throws IOException En el caso de que se presente un error con el socket.
     */
    public void processRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        Map<String,String> request = new HashMap<String,String>();
        boolean requestReady = false;
        while ((inputLine = in.readLine()) != null) {
            if(!requestReady){
                request.put("requestLine", inputLine);
                requestReady =true;
            }else{
                String[] linea = inputLine.split(":");
                if(linea.length > 1){
                    request.put(linea[0], linea[1]);
                }
            }

            if (!in.ready()) {
                break;
            }
        }
        OutputStream out = clientSocket.getOutputStream();
        generateResponse(out, request);
        in.close();
    }

    /**
     * Metodo que se encarga de generar la respuesta a la peticion.
     * @param out OutputStream del socket del cliente
     * @param request informacion de la peticion del cliente
     * @throws IOException En el caso de que no se pueda obtener el recurso
     */
    public void generateResponse(OutputStream out, Map<String, String> request) throws IOException {
        String outputLine;
        outputLine = generateResponseNotFound();
        boolean notFound = true;
        if(request.get("requestLine") != null){
            String[] requestLine = request.get("requestLine").split(" ");
            PrintWriter printWriter = new PrintWriter(out,true);
            if(requestLine[1].contains(".")){
                byte contenido[] = getResource(requestLine[1]);
                if(contenido != null ){
                    notFound = false;
                    outputLine = "HTTP/1.1 200 OK\r\n" + generateTypeHeader(requestLine[1]);
                    printWriter.println(outputLine);
                    out.write(contenido);
                    
                }
            }else {
                try {
                    URI uri = new URI(requestLine[1]);
                    if (uri.getPath().startsWith("/api")){
                        getSparkRResponse(uri.getPath().substring(4), printWriter);
                        notFound = false;
                    }
                } catch (URISyntaxException e) {
                    Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
                } catch (NullPointerException e){
                    notFound = true;
                }
            }
        }
        if (notFound){
            PrintWriter printWriter = new PrintWriter(out,true);
            printWriter.println(outputLine);
        }
        out.close();
    }

    /**
     * Contruye una respuesta de error por recurso no encontrado.
     * @return Cuerpo HTTP de la respuesta
     */
    private String generateResponseNotFound() {
        String out = "HTTP/1.1 404 NOT FOUND\r\n" 
                    + "Content-Type: text/html\r\n" + "\r\n" 
                    + "<!DOCTYPE html>\n"
                    + "<html>\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n" + "<title>404</title>\n"
                    + "</head>\n" 
                    + "<body>\n" 
                    + "<h1>404 Not Found</h1>\n" 
                    + "</body>\n" 
                    + "</html>\n";
        return out;
    }
    /**
     * Genera la parte del tipo del encabezado segun el tipo de recurso.
     * @param req Extension del recurso
     * @return Encabezado HTTP.
     */
    private String generateTypeHeader(String req) {
        
        String header = "";
        String[] lista = req.split("\\.");
        String tipo = lista[lista.length-1];
        if( Arrays.asList(imagenes).contains(tipo) ){
             header += "Content-Type: image/" + tipo +"\r\n" + "\r";
        }else if (Arrays.asList(textos).contains(tipo)){
             header += "Content-Type: text/" + tipo +"\r\n" + "\r\n";
        }
        
        return header;
    }

    /**
     * Metodo que obtiene un recurso almacenado.
     * @param path Nombre del recurso.
     * @return Contenido como un arreglo de bytes.
     * @throws IOException Si encuentra problemas leyendo el recurso.
     */
    private byte[] getResource(String path) throws IOException {
        String dir = "src/main/resources" +  path;
        File f = new File(dir);
        byte contenido[] = new byte[(int)f.length()];
        if (f.exists() && f.isFile() ){
            FileInputStream ficheroStream = new FileInputStream(f);
            ficheroStream.read(contenido);
            ficheroStream.close();
        }else{
            contenido = null;
        }
        return contenido;

    }

    /**
     * This method reads the default port as specified by the PORT variable in the
     * environment.
     *
     * Heroku provides the port automatically so you need this to run the project on
     * Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
        return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    private void getSparkRResponse(String path, PrintWriter out) {

        String header = "HTTP/1.1 200 OK\r\n"
        + "Content-Type: text/html\r\n"
        + "\r\n";
        
        String response = SparkR.get(path);
        if (response != null){
            out.println(header + response);
        }else{
            throw new NullPointerException();
        }

    }
}
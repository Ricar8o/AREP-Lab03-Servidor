package co.edu.escuelaing.arep.sparkr;

import java.io.IOException;

import co.edu.escuelaing.arep.sparkr.httpserver.HttpServer;

public class SparkRWebServer {
    public static void main(String[] args) throws IOException {
        SparkRController sparkRController = new SparkRController();
        sparkRController.tryConnect();
        SparkR.get("/hello", (req,res) -> imprhello(req,res));
        HttpServer server = new HttpServer();
        server.start();
    }

    private static String imprhello(String req, String res) {
        return  "Hello " + req + "!";
    }

}
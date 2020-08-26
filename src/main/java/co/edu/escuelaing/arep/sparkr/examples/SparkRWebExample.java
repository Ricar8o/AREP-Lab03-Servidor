package co.edu.escuelaing.arep.sparkr.examples;

import java.io.IOException;

import co.edu.escuelaing.arep.sparkr.SparkR;
import co.edu.escuelaing.arep.sparkr.httpserver.HttpServer;

public class SparkRWebExample {
    public static void main(String[] args) throws IOException {
        SparkR.get("/hello", (req,res) -> imprhello(req,res));
        HttpServer server = new HttpServer(80);
        server.start();
    }

    private static String imprhello(String req, String res) {
        return  req + " & " +  res;
    }
}
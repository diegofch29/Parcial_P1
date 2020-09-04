package edu.escuelaing.arep.ServidorWeb;


import edu.escuelaing.arep.ServidorWeb.Funciones.FuncTrigonometrica;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * 
 * @author dnielben
 */

public class HttpServer {

    private int port = 36000;
    private boolean running = false;
    FuncTrigonometrica func;

    public HttpServer() {
    }

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        func = new FuncTrigonometrica();
        try {
            if (System.getenv("PORT") != null) {
                port = Integer.parseInt(System.getenv("PORT"));
            }
            else{
                port = 4567;
            } //returns default port if heroku-port isn't set (i.e. on localhost)
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port);
                System.exit(1);
            }

            running = true;
            while (running) {
                try {
                    Socket clientSocket = null;
                    try {
                        System.out.println("Listo para recibir en puerto 36000 ...");
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                        System.exit(1);
                    }

                    processRequest(clientSocket);

                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        Map<String, String> request = new HashMap<>();
        boolean requestLineReady = false;
        while ((inputLine = in.readLine()) != null) {
            if (!requestLineReady) {
                request.put("requestLine", inputLine);
                requestLineReady = true;
            } else {
                String[] entry = createEntry(inputLine);
                if (entry.length > 1) {
                    request.put(entry[0], entry[1]);
                }
            }
            if (!in.ready()) {
                break;
            }
        }
        Request req = new Request(request.get("requestLine"));

        System.out.println("RequestLine: " + req);

        createResponse(req, new PrintWriter(
                clientSocket.getOutputStream(), true), clientSocket);
        in.close();
    }

    private String[] createEntry(String rawEntry) {
        if (rawEntry!=null){
            return rawEntry.split(":");
        }
        return null;
    }

    private void createResponse(Request req, PrintWriter out,Socket socket) {
        URI theuri = req.getTheuri();
        String[] values = theuri.getPath().replace("/", "").split("&");
        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
        out.print(header);
        System.out.println(values[1]+ " " + values[0]);
        func = new FuncTrigonometrica();
        out.print(func.function(values[0], values[1]));
        System.out.println(func.function(values[0], values[1]).get("sin"));
        out.close();
        
    }
    

}
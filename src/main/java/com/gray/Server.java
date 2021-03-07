package com.gray;

import com.gray.datasources.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    public final static int serverPort = 6000;
    private List<BaseSource> dataSources = new ArrayList<>();

    /**
     * Initialises data sources through dependency injection of
     * and class implementing `BaseSource`
     * @param dSources
     */
    @Inject
    void initDSources(@Any Instance<BaseSource> dSources){
        for (BaseSource dSource: dSources){
            dataSources.add(dSource);
        }
    }

    /**
     * Entry point for server. Handles the dependency injection and starts the server
     * @param args CLI Arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SeContainer container = null;

        SeContainerInitializer containerInitializer = SeContainerInitializer.newInstance();
        container = containerInitializer.initialize();

        Server server = container.select(Server.class).get();
        server.startServer();
    }

    /**
     * Starts the server on the specified port. Accepts text queries as
     * search queries and returns an ObjectStream of a ServerResultList[] to the client
     * @throws IOException
     */
    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(serverPort);
        while(true){
            Socket socket = server.accept();
            System.out.println("Accepted conn");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeObject(dataSources.size());
            String searchQuery = (String)in.readUTF();

            System.out.println(searchQuery);
//            ServerResultList[] outputs = new ServerResultList[dataSources.size()];
            for (int i = 0; i < dataSources.size(); i++){
                DataSourceResult[] dSourceResults = dataSources.get(i).searchFor(searchQuery,2);
                ServerResultList svRes =  new ServerResultList();
                svRes.setdSourceTitle(dataSources.get(i).getSourceName());
                svRes.setResults(dSourceResults);
                out.writeObject(svRes);
            }
            in.close();
            socket.close();
        }
    }
}

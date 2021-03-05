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

    @Inject
    void initDSources(@Any Instance<BaseSource> dSources){
        for (BaseSource dSource: dSources){
            dataSources.add(dSource);
        }
    }


    public static void main(String[] args) throws IOException {
        SeContainer container = null;

        SeContainerInitializer containerInitializer = SeContainerInitializer.newInstance();
        container = containerInitializer.initialize();

        Server server = container.select(Server.class).get();
        server.startServer();
    }

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(serverPort);
        while(true){
            Socket socket = server.accept();
            System.out.println("Accepted conn");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            String searchQuery = (String)in.readUTF();

            System.out.println(searchQuery);
            ServerResultList[] outputs = new ServerResultList[dataSources.size()];
            for (int i = 0; i < dataSources.size(); i++){
                DataSourceResult[] dSourceResults = dataSources.get(i).searchFor(searchQuery,2);
                outputs[i] = new ServerResultList();
                outputs[i].setdSourceTitle(dataSources.get(i).getSourceName());
                outputs[i].setResults(dSourceResults);
            }
            out.writeObject(outputs);
            in.close();
            socket.close();
        }
    }
}

package com.gray;

import com.gray.datasources.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public final static int serverPort = 6000;
    private BaseSource[] dataSources;
    public static void main(String[] args) throws IOException {
        Server sv = new Server();
        sv.startServer();
    }

    public void startServer() throws IOException {
        //        Initialise data sources
        dataSources = new BaseSource[]{new GithubRepos(), new LocalWiki()};
        ServerSocket server = new ServerSocket(serverPort);
        while(true){
            Socket socket = server.accept();
            System.out.println("Accepted conn");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            String searchQuery = (String)in.readUTF();

            System.out.println(searchQuery);
            ServerResultList[] outputs = new ServerResultList[dataSources.length];
            for (int i = 0; i < dataSources.length; i++){
                DataSourceResult[] dSourceResults = dataSources[i].searchFor(searchQuery,2);
                outputs[i] = new ServerResultList();
                outputs[i].setdSourceTitle(dataSources[i].getSourceName());
                outputs[i].setResults(dSourceResults);
            }
            out.writeObject(outputs);
            in.close();
            socket.close();
        }
    }
}

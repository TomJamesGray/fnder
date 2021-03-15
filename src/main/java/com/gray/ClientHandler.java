package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import com.gray.datasources.ServerResultList;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private String searchQuery;
    private List<BaseSource> dataSources = new ArrayList<>();

    public ClientHandler(Socket clientSocket, List<BaseSource> dataSources){
        this.clientSocket = clientSocket;
        this.dataSources = dataSources;
    }
    @Override
    public void run() {
        ObjectOutputStream out = null;
        DataInputStream in = null;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            out.writeObject(dataSources.size());
            searchQuery = (String) in.readUTF();
        } catch(IOException e){
            e.printStackTrace();
        }

//            ServerResultList[] outputs = new ServerResultList[dataSources.size()];
        for (int i = 0; i < dataSources.size(); i++){
//                TODO Sort out order of execution of data sources. I.e make wikipediaSearch
//                last as it's slow
            DataSourceResult[] dSourceResults = dataSources.get(i).searchFor(searchQuery,2);
            ServerResultList svRes =  new ServerResultList();
            svRes.setdSourceTitle(dataSources.get(i).getSourceName());
            svRes.setResults(dSourceResults);
            try {
                out.writeObject(svRes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

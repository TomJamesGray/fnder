package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.GithubRepos;
import com.gray.datasources.LocalWiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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
        dataSources = new BaseSource[]{new GithubRepos()};
        ServerSocket server = new ServerSocket(serverPort);
        while(true){
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String msg = in.readLine();
            System.out.println(msg);
            if (msg.startsWith("search:")){
                System.out.println("search for " + msg);
            }
            in.close();
            socket.close();
        }
    }
}

package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.GithubRepos;
import com.gray.datasources.LocalWiki;
import com.gray.datasources.URLResult;

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
        dataSources = new BaseSource[]{new GithubRepos()};
        ServerSocket server = new ServerSocket(serverPort);
        while(true){
            Socket socket = server.accept();
            System.out.println("Accepted conn");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String inputStr = (String)in.readUTF();

            System.out.println(inputStr);
//            out.println("hello from server");
            out.writeObject(new URLResult("test","http://www.google.com"));
            in.close();
            socket.close();
        }
    }
}

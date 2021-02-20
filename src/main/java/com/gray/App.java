package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import com.gray.datasources.LocalWiki;
import javafx.application.Application;
import javafx.stage.Stage;
import org.graalvm.compiler.nodeinfo.InputType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class App
{
    /**
     * Starts server that listens for message on port to draw the GUI
     * @param port Port number to listen on
     * @throws IOException
     */
    public static void startServer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        while(true){
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = in.readLine();
            System.out.println(msg);
            in.close();
            socket.close();
        }
    }
    public static void main( String[] args ) throws Exception {
        BaseSource[] dataSources = {new LocalWiki()};
        startGUI();


/*        while(true){
            String query = getInput("Search query: ");
            for (BaseSource dSource : dataSources){
                DataSourceResult[] results = dSource.searchFor(query,2);
                System.out.println(dSource.getSourceName() + ":");
                for (int i = 0; i < results.length; i++){
                    System.out.println(i+1 + ") " + results[i]);
                }
                System.out.println();
            }

        }*/
    }

    public static void startGUI(){
        Application.launch(GUIApp.class, new String[]{});
    }

    public static String getInput(String prompt){
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        String val = input.nextLine();
        return(val);
    }
}

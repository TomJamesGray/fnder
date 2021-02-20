package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.LocalWiki;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class App extends Application {
    private SearchController searchController;
    private BaseSource[] dataSources;
    public final boolean background = false;
    public final int serverPort = 6000;
    /**
     * Starts server that listens for message on port to draw the GUI
     * @param port Port number to listen on
     * @throws IOException
     */
    public boolean listenForStart(int port) throws Exception {
        ServerSocket server = new ServerSocket(port);
        while(true){
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = in.readLine();
            System.out.println(msg);
            if (msg.equals("START")){
                return(true);
            }
            in.close();
            socket.close();
        }
    }
    private void initialiseDataSources() throws IOException {
        dataSources = new BaseSource[]{new LocalWiki()};
    }
    public static void main( String[] args ) throws Exception {
        launch();
    }

    public void init() throws Exception {
        initialiseDataSources();
        System.out.println(this.dataSources);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(100);
        stage.setWidth(500);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/searchInterface.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        searchController = loader.getController();
        searchController.setDataSources(dataSources);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        if(background) {
            if (listenForStart(serverPort)) {
                stage.show();
            }
        }
        else{
            stage.show();
        }
    }

    public static String getInput(String prompt){
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        String val = input.nextLine();
        return(val);
    }
}

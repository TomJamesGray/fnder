package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import com.gray.datasources.LocalWiki;

import java.io.IOException;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws IOException {
        BaseSource[] dataSources = {new LocalWiki()};
        LocalWiki base = new LocalWiki();

        while(true){
            String query = getInput("Search query: ");
            for (BaseSource dSource : dataSources){
                DataSourceResult[] results = dSource.searchFor(query,2);
                System.out.println(dSource.getSourceName() + ":");
                for (int i = 0; i < results.length; i++){
                    System.out.println(i+1 + ") " + results[i]);
                }
                System.out.println();
            }

        }
    }
    public static String getInput(String prompt){
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        String val = input.nextLine();
        return(val);
    }
}

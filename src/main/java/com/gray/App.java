package com.gray;

import com.gray.datasources.LocalWiki;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        LocalWiki base = new LocalWiki();
        base.searchFor("numerical",5);
    }
}

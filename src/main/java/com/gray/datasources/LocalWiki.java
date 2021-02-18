package com.gray.datasources;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LocalWiki extends BaseSource{
    public LocalWiki(){
//        Find all MD files in data directory
        final Path dataDir = Paths.get("/tmp/data");
        List<Path> mdFiles = findMdFiles(dataDir);
    }
    public List<Path> findMdFiles(Path dir){
        List<Path> mdFiles = new ArrayList<Path>();
        File folder =  new File(dir.toString());
        File[] files = folder.listFiles();
        for (File file : files){
            if (file.getName().endsWith(".md")){
                System.out.println(file.getName());
                mdFiles.add(Paths.get(file.toString()));
            }
            else if(file.isDirectory()){
                List<Path> additionalFiles = findMdFiles(Paths.get(dir.toString(),file.getName()));
                mdFiles.addAll(additionalFiles);
            }
        }
        System.out.println(mdFiles);
        return mdFiles;
    }

}

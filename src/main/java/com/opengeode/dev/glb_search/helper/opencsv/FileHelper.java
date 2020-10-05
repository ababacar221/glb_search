package com.opengeode.dev.glb_search.helper.opencsv;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Component
@Slf4j
public class CsvHelper {

    public File createDirectoryAndFile(String dir, String fil) throws IOException {
        String fd = String.format("%s/%s",dir,fil);
        File file = new File(dir);
        if (file.exists()){
            String m = String.format("Directory : %s is already exist",fd);
            log.info(m);
            file = new File(fd);
            if (!file.exists()) file.createNewFile();
        }else{
            file.mkdir();
            file = new File(fd);
            if (!file.exists()){
                file.createNewFile();
            }else {
                String m = String.format("FILE : %s is already exist",fd);
                log.info(m);
            }
        }
        return file;
    }

    public Set<String> listFilesUsingFileWalkAndVisitor(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (!Files.isDirectory(file)) {
                    fileList.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return fileList;
    }

    public Set<String> ListSubdirectories(String dir){
        File file = new File(dir);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        Set<String> fileList = new HashSet<>(Arrays.asList(directories));
        return fileList;
    }

    public Collection<File> readerFileConfig(String directory_data_config) throws IOException {
        Collection<File> fileCollection = new ArrayList<>();
        Set<String> listSubdirectories = ListSubdirectories(directory_data_config);
        if(!listSubdirectories.isEmpty()){
            for (String dir : listSubdirectories){
                if(!listFilesUsingFileWalkAndVisitor(directory_data_config+"/"+dir).isEmpty()){
                    String d = directory_data_config+"/"+dir;
                    Set<String> listFilesUsingFileWalkAndVisitor = listFilesUsingFileWalkAndVisitor(d);
                    for (String f : listFilesUsingFileWalkAndVisitor) {
                        fileCollection.add(createDirectoryAndFile(d,f));
                    }
                }
            }
        }
        return fileCollection;
    }

}

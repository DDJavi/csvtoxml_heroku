package com.borjamoll.csvtoxml_heroku.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {


    public void saveFile(MultipartFile file, String folder, String name) throws IOException {
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + name);
            Files.write(path,bytes);
        }
    }
}

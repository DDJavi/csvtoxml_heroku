package com.borjamoll.csvtoxml_heroku.services;

import com.borjamoll.csvtoxml_heroku.builder.XMLBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;

@Service
public class ConverterService {
    private final String folder ="src/main/resources/files/";
    private final String FILE_CSV="file.csv";
    private final String FILE_XML="file.xml";
    private Logger log = LoggerFactory.getLogger(getClass());

    final
    UploadFileService _uploadFileService;

    public ConverterService(UploadFileService _uploadFileService) {
        this._uploadFileService = _uploadFileService;
    }


    public void converter(MultipartFile file, HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {
        try {
            _uploadFileService.saveFile(file,folder, FILE_CSV);
            log.info("--Csv inicial guardado--");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XMLBuilder builder = new XMLBuilder();

        builder.readFile(folder+FILE_CSV);
        Document document = builder.generateRandomProductXML();


        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename="
                + FILE_XML);
        InputStream is = new FileInputStream(builder.saveDocumentAsFile(folder+FILE_XML, document, true));
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();

        File csv = new File(folder+FILE_CSV);
        csv.delete();

    }

}

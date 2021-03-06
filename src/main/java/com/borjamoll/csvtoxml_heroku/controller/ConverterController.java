package com.borjamoll.csvtoxml_heroku.controller;


import com.borjamoll.csvtoxml_heroku.services.ConverterService;
import com.borjamoll.csvtoxml_heroku.services.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ConverterController {

    private Logger log = LoggerFactory.getLogger(getClass());

    final ConverterService _converterService;

    public ConverterController(ConverterService _converterService) {

        this._converterService = _converterService;
    }

    @GetMapping("/")
    public String index(){
        return "convertor_index";
    }

    @PostMapping("converter")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, TransformerException, ParserConfigurationException {

        _converterService.converter(file, response);
    }
}
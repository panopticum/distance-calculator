package com.example.testdistancecalculator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {
    public void uploadFile(MultipartFile file) throws IOException {
        file.transferTo(new File("c:/temp/" + file.getOriginalFilename()));
    }
}

package com.dynamic.config.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dynamic.config.model.ChangeAnalysis;
import com.dynamic.config.model.UploadFileResponse;
import com.dynamic.config.service.ChangeAnalysisService;
import com.dynamic.config.service.FileStorageService;
import com.dynamic.config.util.Constants;

@RestController
@RequestMapping("/")
public class DynamicReconfigController {

    private static final Logger logger = LoggerFactory.getLogger(DynamicReconfigController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ChangeAnalysisService changeAnalysisService;
    
    @PostMapping("/fileupload/wsdl/old/")
    public UploadFileResponse uploadOldWsdlFile(@RequestParam("file") MultipartFile file) {
    	
    	String fileName= Constants.FileNames.OLD_WSDL ;
    	return fileStorageService.uploadFile(file,fileName);
    	
    }
    
    @PostMapping("/fileupload/wsdl/new/")
    public UploadFileResponse uploadNNewWsdlFile(@RequestParam("file") MultipartFile file) {
    	
    	String fileName= Constants.FileNames.NEW_WSDL ;
    	return fileStorageService.uploadFile(file,fileName);
    	
    }
    
    @PostMapping("/fileupload/testcases/old/")
    public UploadFileResponse uploadOldTestcaseFile(@RequestParam("file") MultipartFile file) {
    	
    	String fileName= Constants.FileNames.OLD_TESTCASES ;
    	return fileStorageService.uploadFile(file,fileName);
    	
    }
    
    @GetMapping("/downloadFile/testcases/new/")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
    	
    	String fileName= Constants.FileNames.NEW_TESTCASES ;
    	return fileStorageService.downloadNewTestCasesFile(fileName,request);
 
    }

    @GetMapping("/analyse_changes")
    public ResponseEntity<ChangeAnalysis> analyseWsdlChanges(HttpServletRequest request) {
 
    	ChangeAnalysis response = changeAnalysisService.runAnalysis();
    	logger.info("Analysis-Report : ", response );
    	
    	return new ResponseEntity<ChangeAnalysis>( response, HttpStatus.OK);
 
    }
    
}
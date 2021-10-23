package com.dynamic.config.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamic.config.model.ChangeAnalysis;
import com.dynamic.config.util.Constants;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.WSDLParser;

@Service
public class ChangeAnalysisService {
	

    private static final Logger logger = LoggerFactory.getLogger(ChangeAnalysisService.class);
	
	 @Autowired
	 private FileStorageService fileStorageService;


	public ChangeAnalysis runAnalysis() {
		
		ChangeAnalysis analysisReport = new ChangeAnalysis();
		

    	String oldWsdlFileName= Constants.FileNames.OLD_WSDL ;
		Path oldWsdlFilePath = fileStorageService.fileStorageLocation.resolve(oldWsdlFileName).normalize();


    	String newWsdlFileName= Constants.FileNames.NEW_WSDL ;
		Path NewWsdlFilePath = fileStorageService.fileStorageLocation.resolve(newWsdlFileName).normalize();

    	// create a new wsdl parser
    	WSDLParser parser = new WSDLParser();

    	// parse both wsdl documents
    	Definitions wsdl1 = parser.parse(oldWsdlFilePath.toString());
    	Definitions wsdl2 = parser.parse(NewWsdlFilePath.toString());
    	
    	
    	Map<String, Operation> wsdl1Operations = createOperationsMap(wsdl1.getOperations());
    	Map<String, Operation> wsdl2Operations = createOperationsMap(wsdl2.getOperations());
    	
    	
    	compareWSDLOperations(wsdl1Operations, wsdl2Operations,analysisReport);
    	
    	//check on operations level if something is deleted or added new 
    	Set<String> allTestCasesOperations = createNewTestCasesFile(analysisReport);
    	
    	double testCoveragePct = ((allTestCasesOperations.size()*100)/wsdl2Operations.keySet().size()) ;
    	
    	analysisReport.setTestCoveragePct(testCoveragePct);
    	
    	
		Set<String> tempWsdl2OperationNameSet = new HashSet<String>(wsdl2Operations.keySet());
		tempWsdl2OperationNameSet.removeAll(allTestCasesOperations);
		analysisReport.setTestCasesMissingOperations(tempWsdl2OperationNameSet);
		
    	return analysisReport;
    	
	}
	
	private Set<String> createNewTestCasesFile(ChangeAnalysis analysisReport) {
		
		Set<String> allTestCasesOperations = new HashSet<String>();
		
		String newTestCasesFileName= Constants.FileNames.NEW_TESTCASES ;
		String oldTestCasesFileName= Constants.FileNames.OLD_TESTCASES ;


		try ( BufferedReader br = Files.newBufferedReader(fileStorageService.fileStorageLocation.resolve(oldTestCasesFileName).normalize());
				FileWriter fw = new FileWriter(fileStorageService.fileStorageLocation.resolve(newTestCasesFileName).normalize().toFile());
			)
		{
			String line;
			while ((line = br.readLine()) != null) {
				String [] testCase = line.split("\\|");
				String operationName = testCase[0];
				Set<String> removedOperations = analysisReport.getOperationsRemoved();
				
				if(removedOperations.contains(operationName))
				{
					continue;
				}
				allTestCasesOperations.add(operationName);
				fw.write(line);
				fw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allTestCasesOperations;
		
	}

	private void compareWSDLOperations(Map<String, Operation> wsdl1Operations,
			Map<String, Operation> wsdl2Operations, ChangeAnalysis analysisReport) {
		
		//compare the operations if anything removed and added  newly
		Set<String> wsdl1OperationNameSet =  wsdl1Operations.keySet();
		Set<String> wsdl2OperationNameSet =  wsdl2Operations.keySet();
		
		Set<String> tempWsdl1OperationNameSet = new HashSet<String>(wsdl1OperationNameSet);
		Set<String> tempWsdl2OperationNameSet = new HashSet<String>(wsdl2OperationNameSet);
		
		tempWsdl1OperationNameSet.removeAll(wsdl2OperationNameSet);
		tempWsdl2OperationNameSet.removeAll(wsdl1OperationNameSet);
		
		analysisReport.setOperationsAdded(tempWsdl2OperationNameSet);
		analysisReport.setOperationsRemoved(tempWsdl1OperationNameSet);
		analysisReport.setTotalOperationsInUpdatedWSDL(wsdl2OperationNameSet);
		
	}

	private Map createOperationsMap(List<Operation> operations) {
		Map<String, Operation> operationsMap = operations.stream().collect(Collectors.toMap(Operation :: getName, Function.identity()));
		
		return operationsMap;
	}
}

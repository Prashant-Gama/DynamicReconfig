package com.dynamic.config.model;

import java.util.Set;

public class ChangeAnalysis {
	
	private Set<String> operationsAdded;
	private Set<String> operationsRemoved;
	private Set<String> totalOperationsInUpdatedWSDL;
	private Set<String> testCasesMissingOperations;
	private double testCoveragePct;
	
	public Set<String> getOperationsAdded() {
		return operationsAdded;
	}
	public void setOperationsAdded(Set<String> operationsAdded) {
		this.operationsAdded = operationsAdded;
	}
	public Set<String> getOperationsRemoved() {
		return operationsRemoved;
	}
	public void setOperationsRemoved(Set<String> operationsRemoved) {
		this.operationsRemoved = operationsRemoved;
	}
	public Set<String> getTotalOperationsInUpdatedWSDL() {
		return totalOperationsInUpdatedWSDL;
	}
	public void setTotalOperationsInUpdatedWSDL(Set<String> totalOperationsInUpdatedWSDL) {
		this.totalOperationsInUpdatedWSDL = totalOperationsInUpdatedWSDL;
	}
	public Set<String> getTestCasesMissingOperations() {
		return testCasesMissingOperations;
	}
	public void setTestCasesMissingOperations(Set<String> testCasesMissingOperations) {
		this.testCasesMissingOperations = testCasesMissingOperations;
	}
	public double getTestCoveragePct() {
		return testCoveragePct;
	}
	public void setTestCoveragePct(double testCoveragePct) {
		this.testCoveragePct = testCoveragePct;
	}

	
}

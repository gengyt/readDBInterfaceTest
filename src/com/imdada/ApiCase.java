package com.imdada;


public class ApiCase {

	String name;
	String caseId;
	String expected;
	String result;
	String isRun;
	String params;
	String nextCaseParams;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextCaseParams() {
		return nextCaseParams;
	}

	public void setNextCaseParams(String nextCaseParams) {
		this.nextCaseParams = nextCaseParams;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getIsRun() {
		return isRun;
	}

	public void setIsRun(String isRun) {
		this.isRun = isRun;
	}

}

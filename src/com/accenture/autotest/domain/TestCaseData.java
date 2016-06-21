package com.accenture.autotest.domain;

import java.text.DecimalFormat;

public class TestCaseData {

	private String setp;
	private Boolean struts;
	private Long timestamp;
	private Long gTimeElapsed;
	private String fileRelativePaths;
	private String errorMessage;
	
	public String getSetp() {
		return setp;
	}
	public void setSetp(String setp) {
		this.setp = setp;
	}
	public boolean getStruts() {
		return struts;
	}
	public void setStruts(Boolean struts) {
		this.struts = struts;
	}
	public Double getTimestamp() {
		//format number, e.g.1020ms --> 1.02s
		return new Double((new DecimalFormat("0.00")).format(this.timestamp/1000.0));
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getFileRelativePaths() {
		return fileRelativePaths;
	}
	public void setFileRelativePaths(String fileRelativePaths) {
		this.fileRelativePaths = fileRelativePaths;
	}
	public Long getTimeElapsed() {
		return gTimeElapsed;
	}
	public void setTimeElapsed(Long sTimeElapsed) {
		gTimeElapsed = sTimeElapsed;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	

	
	
}

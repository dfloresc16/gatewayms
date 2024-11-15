package com.pt.gatewayms.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonResponse implements Serializable {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("httpstatus")
    private Integer httpstatus;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("message")
    private String message;

    public CommonResponse(boolean success, Integer httpstatus, String errorCode, String errorMessage, String message) {
        this.success = success;
        this.httpstatus = httpstatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
    }

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the httpstatus
	 */
	public Integer getHttpstatus() {
		return httpstatus;
	}

	/**
	 * @param httpstatus the httpstatus to set
	 */
	public void setHttpstatus(Integer httpstatus) {
		this.httpstatus = httpstatus;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
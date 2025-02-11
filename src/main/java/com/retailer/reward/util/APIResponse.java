package com.retailer.reward.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse {

    private String status;
    private String message;
    private Object details;


    public APIResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResponse(Object details) {
        this.status = null;
        this.message = null;
        this.details = details;
    }


    public APIResponse(String status, String message, Object details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    
    @Override
    public String toString() {
        if (status == null && message == null) {
            return "APIResponse{details=" + details + "}";
        }
        return "APIResponse{" +
            (status != null ? "status='" + status + '\'' + ", " : "") +
            (message != null ? "message='" + message + '\'' + ", " : "") +
            "details=" + details +
            '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        APIResponse that = (APIResponse) obj;

        if (!status.equals(that.status)) return false;
        if (!message.equals(that.message)) return false;
        return details != null ? details.equals(that.details) : that.details == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }
}
package io.studio.githubdemo.retrofit.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MotoBeans on 4/7/2016.
 */
public class CommonResponse implements Serializable {
    private String status;
    private String message;
    private String success;
    private String statusCode;
    private ArrayList<String> errors;

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

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}

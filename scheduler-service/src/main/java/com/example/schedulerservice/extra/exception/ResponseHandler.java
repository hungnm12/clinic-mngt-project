package com.example.schedulerservice.extra.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResponseHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean status;
    private int code;
    private String message;
    private String description;

    private transient Object data;

    public ResponseHandler(boolean status, int code, String message, String description) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public ResponseHandler(boolean status, int code, String message, String description, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

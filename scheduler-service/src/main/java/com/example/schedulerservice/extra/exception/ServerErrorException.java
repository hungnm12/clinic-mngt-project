package com.example.schedulerservice.extra.exception;

public class ServerErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ResponseHandler responseHandler;

    public ServerErrorException(String message) {
        super(message);
        responseHandler = new ResponseHandler(false, 500, message, "");
    }

    public ResponseHandler getExceptionResponse() {
        return responseHandler;
    }
}

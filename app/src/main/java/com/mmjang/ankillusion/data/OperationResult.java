package com.mmjang.ankillusion.data;

public class OperationResult {
    private boolean success;
    private String message;
    private Object result;

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public OperationResult(boolean success, String message, Object result){
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

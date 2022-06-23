package technology.dubaileading.maccessuser.rest.request;

import technology.dubaileading.maccessuser.rest.response.ToastMessage;

public class ErrorResponse {
    private int errorCode;
    private ToastMessage message;

    public ErrorResponse(int errorCode, ToastMessage message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ToastMessage getMessage() {
        return message;
    }

    public void setMessage(ToastMessage message) {
        this.message = message;
    }
}

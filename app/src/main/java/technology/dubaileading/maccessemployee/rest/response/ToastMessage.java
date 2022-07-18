package technology.dubaileading.maccessemployee.rest.response;

public class ToastMessage {
    private String toast,log;
    private int type;

    public ToastMessage() {
    }

    public ToastMessage(String toast, String log) {
        this.toast = toast;
        this.log = log;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToast() {
        return toast;
    }

    public void setToast(String toast) {
        this.toast = toast;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}

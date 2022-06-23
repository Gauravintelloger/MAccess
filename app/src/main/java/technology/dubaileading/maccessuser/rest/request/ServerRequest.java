package technology.dubaileading.maccessuser.rest.request;

/**
 * Contract for requests to server. A
 */
public interface ServerRequest {
    /**
     * Execute this request synchronously
     */
    public void executeSync();

    /**
     * Execute this request asynchronously
     */
    public void executeAsync();

}

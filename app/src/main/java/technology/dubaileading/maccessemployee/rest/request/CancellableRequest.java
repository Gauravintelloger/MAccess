package technology.dubaileading.maccessemployee.rest.request;

/**
 * Contract from cancellable requests
 */
public interface CancellableRequest {
    /**
     * Cancel this request, if possible
     */
    public void cancel();

    /**
     * Check if this request has been cancelled
     * @return
     */
    public boolean isCancelled();

}

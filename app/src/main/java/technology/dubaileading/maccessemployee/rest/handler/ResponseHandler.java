package technology.dubaileading.maccessemployee.rest.handler;

import retrofit2.Response;

/**
 * Contract for all the response handler implementations
 *
 */
public interface ResponseHandler<T> {
    /**
     * Handle a server response
     *
     * @param response
     * @return true if the response was handled otherwise false
     */
    public boolean handle(Response<T> response);
}

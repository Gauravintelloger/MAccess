package technology.dubaileading.maccessuser.rest.request;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;


public class ServerRequestFactory {

    private Retrofit retrofit;

    public ServerRequestFactory() {
        this.retrofit = RetrofitFactory.getInstance().getRetrofit();
    }

    public <T> T obtainEndpointProxy(Class<T> clz) {
        return this.retrofit.create(clz);
    }


    public <T> HttpServerRequestBuilder newHttpRequest(Context context) {
        return new HttpServerRequestBuilder<>(context);
    }

    public static class HttpServerRequestBuilder<T> {
        private HttpServerRequest request;

        public HttpServerRequestBuilder(Context context) {
            this.request = new HttpServerRequest(context);
        }

        public HttpServerRequestBuilder withEndpoint(Call<String > call) {
            this.request.setCall(call);
            return this;
        }

        public HttpServerRequestBuilder withSuccessAndFailureCallback(SuccessCallback<T> sCallback,
                                                                      FailureCallback failureCallback) {
            this.request.setSuccessCallback(sCallback);
            this.request.setFailureCallback(failureCallback);
            return this;
        }

        public HttpServerRequestBuilder withProgressDialogue() {
            this.request.setShowProgressDialogue(true);
            return this;
        }

        public ServerRequest build() {
            return this.request;
        }

    }

}

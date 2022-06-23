package technology.dubaileading.maccessuser.rest.request;

import retrofit2.Retrofit;

public class RestCallBuilder {
    private Retrofit retrofit;

    public RestCallBuilder(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public <T> T forEndpoint(Class<T> clz) {
        return this.retrofit.create(clz);
    }
}

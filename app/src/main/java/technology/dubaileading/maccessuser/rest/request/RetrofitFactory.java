package technology.dubaileading.maccessuser.rest.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import technology.dubaileading.maccessuser.BaseApplication;
import technology.dubaileading.maccessuser.BuildConfig;
import technology.dubaileading.maccessuser.utils.AppShared;

/**
 * Singleton factory implementation to instantiate retrofit objects
 */
public class RetrofitFactory {

    //public static final String BASE_URL = "http://staging.dubaileading.technology/maccess-saas/api/public/api/";
    public static final String BASE_URL = "http://maccess-saas-api.dubaileading.technology/api/";


//    public static final String BASE_IMAGE_URL = BASE_URL+"api/images/getProductImage?path=";
//    public static final String BASE_RESTAURANT_IMAGE_URL = BASE_URL+"api/images/getRestaurantImage?path=";

    private OkHttpClient httpClient;

    private Gson gson;

    private static RetrofitFactory instance;

    private RetrofitFactory() {
        this.httpClient = newOkHttpClient();
        this.gson = newGson();
    }

    public static RetrofitFactory getInstance() {
        if (instance == null) {
            synchronized (RetrofitFactory.class) {
                instance = new RetrofitFactory();
            }
        }

        return instance;
    }

    /**
     * Get a new instance of Gson
     *
     * @return
     */
    private Gson newGson() {
        return new GsonBuilder().create();
    }

    /**
     * Get new instance of {@link Retrofit} instance. from the retrofit docs,
     * it's not clear if this Object can be shared among requests; therefore we
     * instantiate this for each request
     *
     * @return
     */
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .client(this.httpClient)
                .build();
    }

    private OkHttpClient newOkHttpClient() {
        try{

            return new OkHttpClient().newBuilder()
                    .addInterceptor(new Interceptor() {

                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            HttpUrl httpUrl = original.url();
                            HttpUrl url = httpUrl
                                    .newBuilder()
                                    //.addQueryParameter("av", String.valueOf(finalVersion))
                                    .build();

                            Request.Builder requestBuilder = original.newBuilder().url(url);

                            String token = new AppShared(BaseApplication.getInstance()).getToken();
                            if (token != null) {
                                requestBuilder.addHeader("Authorization", "Bearer "+token);
                            }

                            Request request = requestBuilder.build();

                            return chain.proceed(request);
                        }
                    })
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .addInterceptor(new Interceptor() {

                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            return response;
                        }
                    }).build();
        }catch (Exception e){
            Log.e("error",e.toString());
        }

        return null;
    }

}

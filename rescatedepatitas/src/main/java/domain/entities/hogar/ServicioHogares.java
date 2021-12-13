package domain.entities.hogar;

import configuracion.Configuracion;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServicioHogares {
    private static ServicioHogares instancia = null;
    private String urlApi;
    private retrofit2.Retrofit retrofit;
    private String token;

    public ServicioHogares() {


        urlApi = Configuracion.INSTANCE.getPropiedad("hogares.urlAPI");
        token = Configuracion.INSTANCE.getPropiedad("hogares.token");


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServicioHogares instancia() {
        if (instancia == null) {
            instancia = new ServicioHogares();
        }
        return instancia;
    }

    public HogarDTO listadoDeHogares(int offset) throws IOException {
        HogaresService hogaresService = this.retrofit.create(HogaresService.class);
        Call<HogarDTO> requestHogares = hogaresService.hogares(offset);
        //Response<String> responseHogares = requestHogares.execute();
        Response<HogarDTO> responseHogares = requestHogares.execute();
        return responseHogares.body();
    }
}

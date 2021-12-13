package domain.entities.hogar;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HogaresService {
   @GET("hogares")
   Call<HogarDTO> hogares(@Query("offset") int offset);
}

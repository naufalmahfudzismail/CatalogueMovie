package comnaufalmahfudzismail.dicoding.moviecataloguemade.API;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService
{
	@GET("movie/top_rated")
	Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

	@GET("movie/popular?")
	Call<MovieResponse> getPopularMovie(@Query("page") int page);

	@GET("search/movie")
	Call<MovieResponse> getSearchMovie(@Query("api_key") String apiKey, @Query("page") int page, @Query("query") String query);

	@GET("movie/{movie_id}")
	Call<DetailMovie> getDetailMovie(@Path("movie_id") String movie_id, @Query("api_key") String apiKey);
}

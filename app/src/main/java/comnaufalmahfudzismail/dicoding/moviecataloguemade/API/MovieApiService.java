package comnaufalmahfudzismail.dicoding.moviecataloguemade.API;


import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService
{
	@GET("movie/top_rated")
	Call<MovieResponse> getTopRatedMovies(@Query("page") int page, @Query("region") String region);

	@GET("search/movie")
	Call<MovieResponse> getSearchMovie(@Query("page") int page, @Query("query") String query , @Query("region") String region);

	@GET("movie/popular")
	Call<MovieResponse> getPopularMovie(@Query("page") int page , @Query("region") String region);

	@GET("movie/latest?")
	Call<DetailMovie> getLatestMovie(@Query("page") int page , @Query("region") String region );

	@GET("movie/{movie_id}")
	Call<DetailMovie> getDetailMovie(@Path("movie_id") String movie_id);

	@GET("movie/upcoming")
	Call<MovieResponse> getUpcomingMovie(@Query("page") int page , @Query("region") String region);

	@GET("movie/now_playing")
	Call<MovieResponse> getNowPlaying(@Query("page") int page , @Query("region") String region);
}

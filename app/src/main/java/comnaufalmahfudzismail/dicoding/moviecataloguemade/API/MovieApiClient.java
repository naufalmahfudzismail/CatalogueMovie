package comnaufalmahfudzismail.dicoding.moviecataloguemade.API;

import android.support.annotation.NonNull;

import java.io.IOException;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Region;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiClient
{
	private MovieApiService movieApiService;

	public MovieApiClient()
	{
		final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(loggingInterceptor)
				.addInterceptor(new Interceptor()
				{
					@Override
					public Response intercept(@NonNull Chain chain) throws IOException
					{
						Request original = chain.request();
						HttpUrl httpUrl = original.url()
								.newBuilder()
								.addQueryParameter("api_key", BuildConfig.API_KEY)
								.addQueryParameter("language", "en-US")
								.build();

						original = original.newBuilder()
								.url(httpUrl)
								.build();

						return chain.proceed(original);
					}
				})
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.client(client)
				.baseUrl(BuildConfig.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		movieApiService = retrofit.create(MovieApiService.class);
	}

	public MovieApiService getService()
	{
		return movieApiService;
	}
}

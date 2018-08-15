package comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse implements Parcelable
{
	@SerializedName("page")
	private int page;

	@SerializedName("results")
	private List<Movie> results = new ArrayList<>();

	@SerializedName("total_results")
	private int totalResults;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("dates")
	private Dates date;

	public MovieResponse()
	{

	}

	protected MovieResponse(Parcel in)
	{
		page = in.readInt();
		results = in.createTypedArrayList(Movie.CREATOR);
		totalResults = in.readInt();
		totalPages = in.readInt();
	}

	public static final Creator<MovieResponse> CREATOR = new Creator<MovieResponse>()
	{
		@Override
		public MovieResponse createFromParcel(Parcel in)
		{
			return new MovieResponse(in);
		}

		@Override
		public MovieResponse[] newArray(int size)
		{
			return new MovieResponse[size];
		}
	};

	public Dates getDate()
	{
		return date;
	}

	public void setDate(Dates date)
	{
		this.date = date;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public List<Movie> getResults()
	{
		return results;
	}

	public void setResults(List<Movie> results)
	{
		this.results = results;
	}

	public int getTotalResults()
	{
		return totalResults;
	}

	public void setTotalResults(int totalResults)
	{
		this.totalResults = totalResults;
	}

	public int getTotalPages()
	{
		return totalPages;
	}

	public void setTotalPages(int totalPages)
	{
		this.totalPages = totalPages;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(page);
		dest.writeTypedList(results);
		dest.writeInt(totalResults);
		dest.writeInt(totalPages);
	}
}

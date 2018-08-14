package comnaufalmahfudzismail.dicoding.moviecataloguemade.Class;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.*;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.MovieColumns.*;

public class Movie implements Parcelable, Serializable
{
	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("overview")
	private String overview;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("genre_ids")
	private List<Integer> genreIds = new ArrayList<Integer>();

	@SerializedName("id")
	private Integer id;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("title")
	private String title;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("popularity")
	private Double popularity;

	@SerializedName("vote_count")
	private Integer voteCount;

	@SerializedName("video")
	private Boolean video;

	@SerializedName("vote_average")
	private Double voteAverage;


	public Movie(String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds, Integer id,
	             String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
	             Integer voteCount, Boolean video, Double voteAverage)
	{
		this.posterPath = posterPath;
		this.adult = adult;
		this.overview = overview;
		this.releaseDate = releaseDate;
		this.genreIds = genreIds;
		this.id = id;
		this.originalTitle = originalTitle;
		this.originalLanguage = originalLanguage;
		this.title = title;
		this.backdropPath = backdropPath;
		this.popularity = popularity;
		this.voteCount = voteCount;
		this.video = video;
		this.voteAverage = voteAverage;
	}

	public Movie()
	{

	}

	public Movie(JSONObject movie) throws JSONException
	{
		this.id = movie.getInt("id");
		this.popularity = movie.getDouble("popularity");
		this.title = movie.getString("title");
		this.voteCount = movie.getInt("vote_count");
		JSONArray genres = movie.getJSONArray("genre_ids");
		for(int i = 0; i<genres.length(); i++)
		{
			int jsonObject =  genres.getInt(i);
			this.genreIds.add(jsonObject);
		}
		this.backdropPath = movie.getString("backdrop_path");
		this.posterPath = movie.getString("poster_path");
		this.overview = movie.getString("overview");
		this.releaseDate = movie.getString("release_date");
		this.voteAverage = movie.getDouble("vote_average");
		this.originalTitle = movie.getString("original_title");
		this.originalLanguage = movie.getString("original_language");
		this.video = movie.getBoolean("video");
		this.adult = movie.getBoolean("adult");

	}
	public Movie (Cursor cursor)
	{
		this.id = getColumnInt(cursor, _ID);
		this.title = getColumnString(cursor,   COLUMN_MOVIE_TITLE);
		this.backdropPath = getColumnString(cursor, COLUMN_MOVIE_BACKDROP_PATH);
		this.posterPath = getColumnString(cursor, COLUMN_MOVIE_POSTER_PATH);
		this.releaseDate = getColumnString(cursor,COLUMN_MOVIE_RELEASE_DATE);
		this.voteAverage = getColumnDouble(cursor, COLUMN_MOVIE_VOTE_AVERAGE);
		this.overview = getColumnString(cursor, COLUMN_MOVIE_OVERVIEW);
		this.voteCount = getColumnInt(cursor,  COLUMN_MOVIE_VOTE_COUNT);

	}


	protected Movie(Parcel in)
	{
		posterPath = in.readString();
		adult = in.readByte() != 0;
		overview = in.readString();
		releaseDate = in.readString();
		if (in.readByte() == 0)
		{
			id = null;
		} else
		{
			id = in.readInt();
		}
		originalTitle = in.readString();
		originalLanguage = in.readString();
		title = in.readString();
		backdropPath = in.readString();
		if (in.readByte() == 0)
		{
			popularity = null;
		} else
		{
			popularity = in.readDouble();
		}
		if (in.readByte() == 0)
		{
			voteCount = null;
		} else
		{
			voteCount = in.readInt();
		}
		byte tmpVideo = in.readByte();
		video = tmpVideo == 0 ? null : tmpVideo == 1;
		if (in.readByte() == 0)
		{
			voteAverage = null;
		} else
		{
			voteAverage = in.readDouble();
		}
	}

	public static final Creator<Movie> CREATOR = new Creator<Movie>()
	{
		@Override
		public Movie createFromParcel(Parcel in)
		{
			return new Movie(in);
		}

		@Override
		public Movie[] newArray(int size)
		{
			return new Movie[size];
		}
	};

	public String getPosterPath()
	{
		return posterPath;
	}

	public void setPosterPath(String posterPath)
	{
		this.posterPath = posterPath;
	}

	public boolean isAdult()
	{
		return adult;
	}

	public void setAdult(boolean adult)
	{
		this.adult = adult;
	}

	public String getOverview()
	{
		return overview;
	}

	public void setOverview(String overview)
	{
		this.overview = overview;
	}

	public String getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public List<Integer> getGenreIds()
	{
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds)
	{
		this.genreIds = genreIds;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getOriginalTitle()
	{
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle)
	{
		this.originalTitle = originalTitle;
	}

	public String getOriginalLanguage()
	{
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage)
	{
		this.originalLanguage = originalLanguage;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBackdropPath()
	{
		return backdropPath;
	}

	public void setBackdropPath(String backdropPath)
	{
		this.backdropPath = backdropPath;
	}

	public Double getPopularity()
	{
		return popularity;
	}

	public void setPopularity(Double popularity)
	{
		this.popularity = popularity;
	}

	public Integer getVoteCount()
	{
		return voteCount;
	}

	public void setVoteCount(Integer voteCount)
	{
		this.voteCount = voteCount;
	}

	public Boolean getVideo()
	{
		return video;
	}

	public void setVideo(Boolean video)
	{
		this.video = video;
	}

	public Double getVoteAverage()
	{
		return voteAverage;
	}

	public void setVoteAverage(Double voteAverage)
	{
		this.voteAverage = voteAverage;
	}


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(posterPath);
		dest.writeByte((byte) (adult ? 1 : 0));
		dest.writeString(overview);
		dest.writeString(releaseDate);
		if (id == null)
		{
			dest.writeByte((byte) 0);
		} else
		{
			dest.writeByte((byte) 1);
			dest.writeInt(id);
		}
		dest.writeString(originalTitle);
		dest.writeString(originalLanguage);
		dest.writeString(title);
		dest.writeString(backdropPath);
		if (popularity == null)
		{
			dest.writeByte((byte) 0);
		} else
		{
			dest.writeByte((byte) 1);
			dest.writeDouble(popularity);
		}
		if (voteCount == null)
		{
			dest.writeByte((byte) 0);
		} else
		{
			dest.writeByte((byte) 1);
			dest.writeInt(voteCount);
		}
		dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
		if (voteAverage == null)
		{
			dest.writeByte((byte) 0);
		} else
		{
			dest.writeByte((byte) 1);
			dest.writeDouble(voteAverage);
		}
	}
}

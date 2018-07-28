package com.dicoding.favoriteflashmovie;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.*;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.*;


public class MovieItem implements Parcelable
{
	private String posterPath;

	private String overview;

	public MovieItem(Cursor cursor)
	{
		this.id = getColumnInt(cursor, _ID);
		this.title = getColumnString(cursor, COLUMN_MOVIE_TITLE);
		this.backdropPath = getColumnString(cursor, COLUMN_MOVIE_BACKDROP_PATH);
		this.posterPath = getColumnString(cursor, COLUMN_MOVIE_POSTER_PATH);
		this.releaseDate = getColumnString(cursor, COLUMN_MOVIE_RELEASE_DATE);
		this.voteAverage = getColumnDouble(cursor, COLUMN_MOVIE_VOTE_AVERAGE);
		this.overview = getColumnString(cursor, COLUMN_MOVIE_OVERVIEW);
		this.voteCount = getColumnInt(cursor, COLUMN_MOVIE_VOTE_COUNT);

	}

	public MovieItem(Parcel in)
	{
		posterPath = in.readString();
		overview = in.readString();
		releaseDate = in.readString();
		if (in.readByte() == 0)

		{
			id = null;
		} else
		{
			id = in.readInt();
		}
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
		if (in.readByte() == 0)
		{
			voteAverage = null;
		} else
		{
			voteAverage = in.readDouble();
		}
	}

	public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>()
	{
		@Override
		public MovieItem createFromParcel(Parcel in)
		{
			return new MovieItem(in);
		}

		@Override
		public MovieItem[] newArray(int size)
		{
			return new MovieItem[size];
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

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
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

	public Double getVoteAverage()
	{
		return voteAverage;
	}

	public void setVoteAverage(Double voteAverage)
	{
		this.voteAverage = voteAverage;
	}

	private String releaseDate;

	private Integer id;

	private String title;

	private String backdropPath;

	private Double popularity;

	private Integer voteCount;

	private Double voteAverage;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(posterPath);
		parcel.writeString(overview);
		parcel.writeString(releaseDate);
		if (id == null)
		{
			parcel.writeByte((byte) 0);
		} else
		{
			parcel.writeByte((byte) 1);
			parcel.writeInt(id);
		}
		parcel.writeString(title);
		parcel.writeString(backdropPath);
		if (popularity == null)
		{
			parcel.writeByte((byte) 0);
		} else
		{
			parcel.writeByte((byte) 1);
			parcel.writeDouble(popularity);
		}
		if (voteCount == null)
		{
			parcel.writeByte((byte) 0);
		} else
		{
			parcel.writeByte((byte) 1);
			parcel.writeInt(voteCount);
		}
		if (voteAverage == null)
		{
			parcel.writeByte((byte) 0);
		} else
		{
			parcel.writeByte((byte) 1);
			parcel.writeDouble(voteAverage);
		}
	}
}

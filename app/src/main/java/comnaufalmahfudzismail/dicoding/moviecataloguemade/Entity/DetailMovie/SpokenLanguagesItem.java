/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 9/27/17 9:25 AM.
 */

package comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.DetailMovie;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguagesItem
{

	@SerializedName("name")
	private String name;

	@SerializedName("iso_639_1")
	private String iso6391;

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setIso6391(String iso6391)
	{
		this.iso6391 = iso6391;
	}

	public String getIso6391()
	{
		return iso6391;
	}

}
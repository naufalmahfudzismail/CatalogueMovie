/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 9/27/17 9:25 AM.
 */

package comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.DetailMovie;

import com.google.gson.annotations.SerializedName;

public class ProductionCountriesItem
{

	@SerializedName("iso_3166_1")
	private String iso31661;

	@SerializedName("name")
	private String name;

	public void setIso31661(String iso31661)
	{
		this.iso31661 = iso31661;
	}

	public String getIso31661()
	{
		return iso31661;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}
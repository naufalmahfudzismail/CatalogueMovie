package comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter
{

	private List<Fragment> mFragment = new ArrayList<>();
	private List<String> mTitles = new ArrayList<>();

	public FragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int position)
	{
		return mFragment.get(position);
	}

	@Override
	public int getCount()
	{
		return mTitles.size();
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position)
	{
		return mTitles.get(position);
	}

	public void addFragmet(Fragment fragment, String title)
	{
		mFragment.add(fragment);
		mTitles.add(title);
	}
}

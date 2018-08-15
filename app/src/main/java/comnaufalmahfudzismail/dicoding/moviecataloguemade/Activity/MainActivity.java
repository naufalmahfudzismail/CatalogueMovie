package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.FragmentAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Region;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentNowPlay;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentSearch;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentUpcoming;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class MainActivity extends AppCompatActivity implements
		NavigationView.OnNavigationItemSelectedListener
{

	@BindView(R.id.tablayout)
	TabLayout tab;

	@BindView(R.id.viewPager_id)
	ViewPager viewPager;

	@BindView(R.id.Main_toolbar)
	Toolbar toolbar;

	@BindView(R.id.nav_view)
	NavigationView nav_view;

	@BindView(R.id.drawer)
	AdvanceDrawerLayout drawer;

	private final static String TAG = "MainMenu";


	//public static boolean isID = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		nav_view.setNavigationItemSelectedListener(this);
		setSupportActionBar(toolbar);


		getSupportActionBar().setTitle(R.string.judul_app);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		nav_view.setNavigationItemSelectedListener(this);

		drawer.setViewScale(Gravity.START, 0.8f);
		drawer.setRadius(Gravity.START, 30);
		drawer.setViewElevation(Gravity.START, 20);

		setFragment();
		titleNavColor();

		//isID = Locale.getDefault().getLanguage().equals("in");

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}



	@Override
	public void onBackPressed()
	{
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		} else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.nav_fav)
		{
			goToFavorite();
		}

		if (id == R.id.action_change_settings)
		{
			Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
			startActivity(mIntent);
		}

		if (id == R.id.nav_share)
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + getString(R.string.share_description));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
		}

		if (id == R.id.region_indo)
		{
			Region.region = "ID";
			restartActivity();
		}
		if (id == R.id.region_usa)
		{
			Region.region = "US";
			restartActivity();
		}
		if (id == R.id.region_japan)
		{
			Region.region = "JP";
			restartActivity();
		}
		if (id == R.id.region_rusia)
		{
			Region.region = "RU";
			restartActivity();
		}
		if (id == R.id.region_jerman)
		{
			Region.region = "DE";
			restartActivity();
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void goToFavorite()
	{
		Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
		startActivity(intent);
	}


	private void setFragment()
	{
		FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

		fragmentAdapter.addFragmet(new FragmentSearch(), getString(R.string.searchFragment));
		fragmentAdapter.addFragmet(new FragmentNowPlay(), getString(R.string.now_playingFragment));
		fragmentAdapter.addFragmet(new FragmentUpcoming(), getString(R.string.upcomingFragment));

		viewPager.setAdapter(fragmentAdapter);
		tab.setupWithViewPager(viewPager);

	}

	private void titleNavColor()
	{
		Menu menu = nav_view.getMenu();
		MenuItem menuItem2 = menu.findItem(R.id.title_two);
		SpannableString d = new SpannableString(menuItem2.getTitle());
		d.setSpan(new TextAppearanceSpan(this, R.style.TitleColorOne), 0, d.length(), 0);
		menuItem2.setTitle(d);

		if(Region.region.equals("ID"))
		{
			getSupportActionBar().setSubtitle(getText(R.string.region) + " : " +getText(R.string.indonesia_region));
		}
		if(Region.region.equals("US"))
		{
			getSupportActionBar().setSubtitle(getText(R.string.region) + " : " + getText(R.string.united_state_region));
		}
		if(Region.region.equals("JP"))
		{
			getSupportActionBar().setSubtitle(getText(R.string.region) + " : " + getText(R.string.japan_region));
		}
		if(Region.region.equals("RU"))
		{
			getSupportActionBar().setSubtitle(getText(R.string.region) + " : " + getText(R.string.russia_region));
		}
		if(Region.region.equals("DE"))
		{
			getSupportActionBar().setSubtitle(getText(R.string.region) + " : " + getText(R.string.german_region));
		}
	}


	private void restartActivity()
	{
		Intent intent = new Intent(getIntent());
		finish();
		startActivity(intent);
	}

}

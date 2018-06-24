package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.FragmentAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.ItemClickSupport;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentNowPlay;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentSearch;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentUpcoming;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{

	@BindView(R.id.tablayout)
	TabLayout tab;

	@BindView(R.id.viewPager_id)
	ViewPager viewPager;

	@BindView(R.id.Main_toolbar)
	Toolbar toolbar;

	public static boolean isID = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(R.string.judul_app);

		FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

		fragmentAdapter.addFragmet(new FragmentSearch(), getString(R.string.searchFragment));
		fragmentAdapter.addFragmet(new FragmentNowPlay(), getString(R.string.now_playingFragment));
		fragmentAdapter.addFragmet(new FragmentUpcoming(), getString(R.string.upcomingFragment));

		viewPager.setAdapter(fragmentAdapter);
		tab.setupWithViewPager(viewPager);

		isID = Locale.getDefault().getLanguage().equals("in");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_change_settings)
		{
			Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
			startActivity(mIntent);
		}
		return super.onOptionsItemSelected(item);
	}


}

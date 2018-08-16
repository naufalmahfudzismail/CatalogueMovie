package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment.FragmentSetting;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class SettingActivity extends AppCompatActivity
{
	@BindView(R.id.toolbar_setting)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getString(R.string.action_settings));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSetting()).commit();
	}

}

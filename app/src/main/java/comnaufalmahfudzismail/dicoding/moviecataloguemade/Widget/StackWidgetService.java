package comnaufalmahfudzismail.dicoding.moviecataloguemade.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService
{
	@Override
	public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent)
	{
		return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
	}

}
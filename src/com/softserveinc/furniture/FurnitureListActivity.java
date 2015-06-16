package com.softserveinc.furniture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class FurnitureListActivity extends Activity {

	/**
	 * Task that will extract all the assets
	 */
	private AssetsExtracter mTask;

	private ListView listView;

	private List<FurnitureListItem> items = new ArrayList<FurnitureListItem>();

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_furniture_list);
		items = ((FurnitureApplication) this.getApplication()).getItemsList();
		// Enable metaio SDK log messages based on build configuration
		MetaioDebug.enableLogging(BuildConfig.DEBUG);

		listView = (ListView) findViewById(R.id.list);

		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
				R.id.furnitureListItem, items);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),  CameraActivity.class);
				intent.putExtra("modelFileName",items.get(position).getModelName());
				startActivity(intent);

			}
		});

		// extract all the assets
		mTask = new AssetsExtracter();
		mTask.execute(0);
	}

	/**
	 * This task extracts all the assets to an external or internal location to
	 * make them accessible to Metaio SDK
	 */
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				// Extract all assets except Menu. Overwrite existing files for
				// debug build only.
				final String[] ignoreList = { "Menu", "webkit", "sounds",
						"images", "webkitsec" };
				AssetsManager.extractAllAssets(getApplicationContext(), "",
						ignoreList, BuildConfig.DEBUG);
			} catch (IOException e) {
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				MetaioDebug.log(Log.ERROR,
						"Error extracting assets, closing the application...");
				finish();
			}
		}
	}
}

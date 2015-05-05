package com.softserveinc.furniture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


import com.metaio.sdk.ARELActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class FurnitureListActivity extends Activity {

	private View mProgress;

	/**
	 * Task that will extract all the assets
	 */
	private AssetsExtracter mTask;

	private ListView listView;

	private List<FurnitureListItem> items = new ArrayList<FurnitureListItem>();

	private void generateListItems() {
		FurnitureListItem furnitureItem;
		furnitureItem = new FurnitureListItem(
				R.drawable.computer_table, "Computer table", "computer_table.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.desk, "Desk", "Desk.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.sofa, "Sofa", "sofa.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.table, "Table", "table.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.wooden_bench, "Wooden bench", "wooden_bench.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.wooden_bench, "Bar table", "bar_table.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.wooden_bench, "Chair", "chair2.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.wooden_bench, "Chair", "helper_stool.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.wooden_bench, "Bed", "bed2.obj");
		items.add(furnitureItem);
		furnitureItem = new FurnitureListItem(
				R.drawable.modern_desk, "Computer desk", "computer_desk.FBX");
		items.add(furnitureItem);
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_furniture_list);
		generateListItems();
		// Enable metaio SDK log messages based on build configuration
		MetaioDebug.enableLogging(BuildConfig.DEBUG);

		mProgress = findViewById(R.id.progress);

		listView = (ListView) findViewById(R.id.list);

		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
				R.id.furnitureListItem, items);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				FurnitureListItem Selecteditem = items.get(+position);
				Toast.makeText(getApplicationContext(),
						Selecteditem.getTitle(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),  CameraActivity.class);
				intent.putExtra("modelFileName",items.get(position).getModelName());
				startActivity(intent);

			}
		});

		// extract all the assets
		mTask = new AssetsExtracter();
		mTask.execute(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.furniture_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This task extracts all the assets to an external or internal location to
	 * make them accessible to Metaio SDK
	 */
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			mProgress.setVisibility(View.VISIBLE);
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
			mProgress.setVisibility(View.GONE);

			if (result) {
				// WebSettings settings = mWebView.getSettings();

				// settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
				// settings.setJavaScriptEnabled(true);
				//
				// mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				// mWebView.setWebViewClient(new WebViewHandler());
				// mWebView.loadUrl("file:///android_asset/Menu/index.html");
				// mWebView.setVisibility(View.VISIBLE);
				// new in
			} else {
				MetaioDebug.log(Log.ERROR,
						"Error extracting assets, closing the application...");
				finish();
			}
		}
	}
}

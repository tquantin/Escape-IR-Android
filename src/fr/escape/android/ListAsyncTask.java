package fr.escape.android;

import java.io.File;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
	final BuilderActivity activity;
	private final int[] ids;
	
	public ListAsyncTask(BuilderActivity activity, int[] ids) {
		super();
		this.ids = ids;
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		ArrayList<String> list = new ArrayList<String>();
		String directory = params[0];
		
		for(int i = 1; i < params.length; i++) {
			list.add(params[i]);
		}
		
		if(directory != null) {
			File[] files = Environment.getExternalStoragePublicDirectory(directory).listFiles();
			if(files != null) {
				for(int i = 0; i < files.length; i++) {
					if(!files[i].isDirectory()) {
						list.add(files[i].getName().replaceAll(".scn", ""));
					}
				}
			}
		}
		
		return list;
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		final ListView list = (ListView) activity.findViewById(ids[0]);
		ListAdapter adapter = new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_list_item_1, result);
		list.setAdapter(adapter);
		
		final int listId = ids[1];
		OnItemClickListener listener = new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				list.setSelected(true);
				Log.i("SELECT", "Infos : " + listId + "/" + position);
				activity.setListId(listId,position);
			}
		};
		
		list.setOnItemClickListener(listener);
	}

}

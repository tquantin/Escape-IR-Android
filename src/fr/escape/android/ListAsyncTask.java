package fr.escape.android;

import java.io.File;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListAsyncTask extends AsyncTask<String[], Void, Object[]> {
	final BuilderActivity activity;
	private final int[] ids;
	
	public ListAsyncTask(BuilderActivity activity, int[] ids) {
		super();
		this.ids = ids;
		this.activity = activity;
	}
	
	@Override
	protected Object[] doInBackground(String[]... params) {
		Object[] elements = new Object[ids.length];
		for(int i = 0; i < ids.length; i++) {
			ArrayList<String> list = new ArrayList<String>();
			String directory = params[i][0];
			
			
			for(int j = 1; j < params[i].length; j++) {
				list.add(params[i][j]);
			}
			
			if(directory != null) {
				File[] files = Environment.getExternalStoragePublicDirectory(directory).listFiles();
				for(int j = 0; j < files.length; j++) {
					if(!files[j].isDirectory()) {
						list.add(files[j].getName().replaceAll(".scn", ""));
					}
				}
			}
			
			elements[i] = list;
		}
		return elements;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onPostExecute(Object[] result) {
		for(int i = 0; i < ids.length; i++) {
			final ListView list = (ListView) activity.findViewById(ids[i]);
			ListAdapter adapter = new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_list_item_1, (ArrayList<String>) result[i]);
			list.setAdapter(adapter);
			
			final int listId = i;
			OnItemClickListener listener = new OnItemClickListener() { 
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					list.setSelected(true);
					activity.setListId(listId,position);
				}
			};
			
			list.setOnItemClickListener(listener);
		}
	}

}

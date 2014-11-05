package cis573.carecoor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import cis573.carecoor.bean.Medicine;
import cis573.carecoor.bean.Schedule;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.utils.Const;
import cis573.carecoor.utils.Logger;

public class ChooseMedActivity extends BannerActivity implements
		OnItemClickListener {
	
	public static final String TAG = "ChooseMedActivity";
	AutoCompleteTextView autoCompView;
	ArrayAdapter<String> adapter;
	Medicine currentMed = null;
	boolean newMed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_med_activity);

		autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
		adapter = new ArrayAdapter<String>(this, R.layout.med_list_item);

		autoCompView.setAdapter(adapter);
		autoCompView.setOnItemClickListener(this);
		autoCompView.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					GetMedicines medTask = new GetMedicines();
					medTask.execute(autoCompView.getText().toString());
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

		});
	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		boolean nextPage = true;
		String str = (String) adapterView.getItemAtPosition(position);
		List<Schedule> mExistMeds = DataCenter.getSchedules(ChooseMedActivity.this);
		if (mExistMeds != null){
			for(Schedule med : mExistMeds){
				if (med.getMedicine().getName().equals(str)){
					Toast.makeText(getApplicationContext(), "Already take this medicine", Toast.LENGTH_SHORT).show();
					nextPage = false;
				}
			}
		}
		if (nextPage){
			Medicine item = new Medicine();
			item.setName(str);
			Intent intent = new Intent(ChooseMedActivity.this,
					SetScheduleActivity.class);
			intent.putExtra(Const.EXTRA_MEDICINE, item);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { // Back from set schedule
			setResult(RESULT_OK);
			finish();
		}
	}

	// //GET MATCHES TO CURRENT INPUT//////
	public class GetMedicines extends
			AsyncTask<String, Void, ArrayList<String>> {

		public static final String TAG = "GetMedicine";
		public final static String apiURL = "http://rxnav.nlm.nih.gov/REST/approximateTerm?term=+";
		public final static String maxEntries = "&maxEntries=5";
		public final static String options = "&option=";
		InputStream in = null;

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			ArrayList<String> resultList = null;
			HttpURLConnection conn = null;

			try {
				StringBuilder sb = new StringBuilder(apiURL);
				sb.append(URLEncoder.encode(arg0[0], "utf8"));
				sb.append(maxEntries);

				URL url = new URL(sb.toString());
				conn = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(conn.getInputStream());
				XmlPullParser parser = Xml.newPullParser();
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
						false);
				parser.setInput(in, null);
				resultList = parseXML(parser);
				resultList.add(0, arg0[0]);

			} catch (MalformedURLException e) {
				Logger.e(TAG, "Error processing API URL", e);
				return resultList;
			} catch (IOException e) {
				Logger.e(TAG, "Error connecting to API", e);
				return resultList;
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			return resultList;
		}

		public ArrayList<String> parseXML(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			ArrayList<String> entries = null;
			String rxcui = null;
			String comment = null;
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					entries = new ArrayList<String>();
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equals("comment")) {
						comment = parser.nextText();
						entries.add(comment);
					}
					if (name.equals("rxcui")) {
						rxcui = parser.nextText();
						entries.add(rxcui);
					}
					break;
				}
				eventType = parser.next();
			}

			return entries;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			Logger.d("YourApp", "onPostExecute : " + result.size());
			// update the adapter
			adapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.med_list_item);
			adapter.setNotifyOnChange(true);
			// attach the adapter to textview
			autoCompView.setAdapter(adapter);
			adapter.notifyDataSetChanged();			
			
			HashSet<String> eliminateDup = new HashSet<String>(result);
			
			for (String string : eliminateDup) {
				Logger.d("YourApp", "onPostExecute : result = " + string);
				if (string == result.get(0)) {
					adapter.add(string);
					adapter.notifyDataSetChanged();
				} else if (string.length() <= 7 && string.length() >= 4) {
					GetMedicineName medName = new GetMedicineName();
					newMed = false;
					medName.execute(string);
				}
				adapter.notifyDataSetChanged();
			}
			Logger.d("YourApp",
					"onPostExecute : autoCompleteAdapter" + adapter.getCount());
		}
	}

	// //GET MEDICINE NAME//////
	public class GetMedicineName extends
			AsyncTask<String, Void, ArrayList<String>> {

		public static final String TAG = "GetMedicineName";
		public final static String apiURL = "http://rxnav.nlm.nih.gov/REST/rxcui/";
		public final static String properties = "/properties";
		InputStream in = null;

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			ArrayList<String> resultList = null;
			HttpURLConnection conn = null;
			XmlPullParserFactory factory = null;
			XmlPullParser parser = null;
			try {
				StringBuilder sb = new StringBuilder(apiURL);
				sb.append(URLEncoder.encode(arg0[0], "utf8"));
				sb.append(properties);
				URL url = new URL(sb.toString());				
				conn = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(conn.getInputStream());
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				parser = factory.newPullParser();

				parser.setInput(in, null);
				resultList = parseXML(parser);

			} catch (MalformedURLException e) {
				Logger.e(TAG, "Error processing API URL", e);
				return resultList;
			} catch (IOException e) {
				Logger.e(TAG, "Error connecting to API", e);
				return resultList;
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			return resultList;
		}

		public ArrayList<String> parseXML(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			ArrayList<String> entries = null;
			String text = null;
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					entries = new ArrayList<String>();
					break;
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					text = parser.getText();
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("name")) {
						entries.add(text);
					}
					break;
				}
				eventType = parser.next();
			}
			return entries;

		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			if (result.size() != 0) {
				if (result.get(0) != null) {
					adapter.add(result.get(0));
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

}

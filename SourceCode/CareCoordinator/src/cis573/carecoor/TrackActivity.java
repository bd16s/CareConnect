package cis573.carecoor;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import cis573.carecoor.AppointmentFragment.AppointmentAdapter;
import cis573.carecoor.ExtendedCalendar.Day;
import cis573.carecoor.ExtendedCalendar.ExtendedCalendarView;
import cis573.carecoor.adapter.CommonAdapter;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.data.ScheduleCenter;
import cis573.carecoor.data.ScheduleCenter.Conformity;
import cis573.carecoor.utils.MyToast;

/**
 * Modified by:
 * yucongli on 11/04/14
 *
 */
public class TrackActivity extends BannerActivity {

	private ListView mListView;
	private View dailyView;
	private ExtendedCalendarView dailyTable;
	private GraphicalView weekGraph;
	private GraphicalView monthGraph;
	private LinearLayout viewTab1;
	private LinearLayout viewTab2;
	private LinearLayout viewTab3;
	private TabHost mTabHost;
	private DailyConformityAdapter mAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track);
		setBannerTitle(R.string.track);
		initList();
		initGraph();
		viewTab1 = (LinearLayout) findViewById(R.id.tab1);
		viewTab1.addView(dailyView);
		viewTab2 = (LinearLayout) findViewById(R.id.tab2);
		viewTab2.addView(weekGraph);
		viewTab3 = (LinearLayout) findViewById(R.id.tab3);
		viewTab3.addView(monthGraph);
		mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("tab_daily")
				.setIndicator("DAILY").setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_weekly")
				.setIndicator("WEEKLY").setContent(R.id.tab2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_monthly")
				.setIndicator("MONTHLY").setContent(R.id.tab3));
	}

	private void initList() {
		dailyView = getLayoutInflater().inflate(R.layout.activity_track_daily_layout, null);
		dailyTable = (ExtendedCalendarView) dailyView.findViewById(R.id.track_calendar);
		dailyTable.setOnDayClickListener(onDayClick);
		
		mListView = (ListView) dailyView.findViewById(R.id.track_daily_record_list);
		mAdapter = new DailyConformityAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	
	private ExtendedCalendarView.OnDayClickListener onDayClick = new ExtendedCalendarView.OnDayClickListener() {
		@Override
		public void onDayClicked(AdapterView<?> adapter, View view,
				int position, long id, Day day) {
//			MyToast.show(getBaseContext(), "clickTest");
			Date tmpDate = new Date(day.getYear(), day.getMonth(), day.getDay());
			List<TrackingRecord> medConfMap = ScheduleCenter.getOverallConformity_daily(TrackActivity.this, tmpDate);
			System.out.print(medConfMap);
			mAdapter.setData(medConfMap);
			mAdapter.notifyDataSetChanged();
			
			TextView tv = (TextView) dailyView.findViewById(R.id.track_daily_record_empty);
			tv.setVisibility(View.GONE);
		}
	};
	
	private void initGraph() {
		XYMultipleSeriesDataset weekDataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesDataset monthDataset = new XYMultipleSeriesDataset();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MONTH, -1);
		cal2.set(Calendar.HOUR, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.MILLISECOND, 0);

		TimeSeries weekSeries = new TimeSeries("week");
		TimeSeries monthSeries = new TimeSeries("month");
		Map<Date, Conformity> map = ScheduleCenter
				.getOverallConformity(TrackActivity.this);
		if (map != null) {
			Iterator<Entry<Date, Conformity>> iter = map.entrySet().iterator();
			Entry<Date, Conformity> entry;
			while (iter.hasNext()) {
				entry = iter.next();
				if (!cal.getTime().after(entry.getKey())) {
					weekSeries.add(entry.getKey(), entry.getValue()
							.getConformity() * 100);
				}

				if (!cal2.getTime().after(entry.getKey())) {
					monthSeries.add(entry.getKey(), entry.getValue()
							.getConformity() * 100);
				}
			}
		}
		weekDataset.addSeries(weekSeries);
		monthDataset.addSeries(monthSeries);

		weekGraph = ChartFactory.getTimeChartView(this, weekDataset,
				getConformityRenderer(weekSeries.getItemCount()), "MM/dd");
		weekGraph.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		monthGraph = ChartFactory.getTimeChartView(this, monthDataset,
				getConformityRenderer(monthSeries.getItemCount()), "MM/dd");
		monthGraph.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

	}

	private XYMultipleSeriesRenderer getConformityRenderer(int size) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(100);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(true, false);
		renderer.setZoomInLimitX(24 * 60 * 60 * 1000);

		renderer.setMargins(new int[] { 130, 130, 110, 60 });
		renderer.setMarginsColor(Color.rgb(237, 242, 250));

		// //////////////////////////////
		renderer.setLabelsColor(Color.DKGRAY);
		renderer.setChartTitle("\n\n\n\n\n\n Compliance");
		renderer.setChartTitleTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 22, getResources()
						.getDisplayMetrics()));
		renderer.setXTitle("\n\n\n\n\n\n Dates");
		renderer.setYTitle("\n\n\n Compliance Percentage");

		// /////////////////////////
		renderer.setLabelsTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 10, getResources()
						.getDisplayMetrics()));
		renderer.setAxesColor(Color.BLACK);
		renderer.setAxisTitleTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, getResources()
						.getDisplayMetrics()));

		renderer.setPointSize(5f);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.CYAN);
		// /////////////////////
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setXLabels(size <= 7 ? size : 7);
		renderer.setYLabels(6);
		renderer.setYLabelsPadding(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
						.getDisplayMetrics()));
		// ///////////////////////////////
		renderer.setShowLegend(false);

		XYSeriesRenderer r = new XYSeriesRenderer();

		r.setColor(Color.BLUE);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setLineWidth(6);
		// r.setPointStrokeWidth(20);

		renderer.addSeriesRenderer(r);
		return renderer;
	}

	public static class DailyConformityAdapter extends CommonAdapter<TrackingRecord> {

		public DailyConformityAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.activity_track_daily_item,
						null);
				vh = new ViewHolder();
				vh.medicine = (TextView) convertView
						.findViewById(R.id.activity_track_daily_medicine);
				vh.conformity = (TextView) convertView
						.findViewById(R.id.activity_track_daily_conformity);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			TrackingRecord item = (TrackingRecord) getItem(position);
			if (item != null) {
				vh.medicine.setText(item.medName);
				vh.conformity.setText(String.valueOf(item.conf) + "%");
			}
			return convertView;
		}
	}
	
	private static class ViewHolder {
		TextView medicine;
		TextView conformity;
	}
	
}

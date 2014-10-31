package cis573.carecoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cis573.carecoor.adapter.CommonAdapter;
import cis573.carecoor.bean.Game;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.utils.ResourceKit;
import cis573.carecoor.utils.Utils;

public class GameFragment extends Fragment {

	public static final String TAG = "GameFragment";

	private ListView mListView;
	private GameAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new GameAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.games_fragment, container, false);
		mListView = (ListView) view;
		mListView.setOnItemClickListener(onItemClick);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter.setData(DataCenter.getGames(getActivity()));
		mListView.setAdapter(mAdapter);
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Game item = (Game) parent.getItemAtPosition(position);
			if (item != null) {
				String pkg = getGameInstalledPkg(getActivity(), item);
				if (pkg != null) {
					showLaunchDialog(item.getName(), pkg);
				} else {
					String[] pkgs = item.getPackageNames();
					if (pkgs != null && pkgs.length > 0) {
						pkg = pkgs[0];
						showMarketDialog(item.getName(), pkg);
					}
				}
			}
		}
	};

	private void showMarketDialog(String name, final String pkg) {
		new AlertDialog.Builder(getActivity())
				.setTitle(name)
				.setMessage(R.string.dialog_game_market)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Utils.goToMarket(getActivity(), pkg);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).show();
	}

	private void showLaunchDialog(String name, final String pkg) {
		new AlertDialog.Builder(getActivity())
				.setTitle(name)
				.setMessage(R.string.dialog_game_launch)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Utils.launchApp(getActivity(), pkg);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).show();
	}

	private static String getGameInstalledPkg(Context context, Game game) {
		if (game == null) {
			return null;
		}
		String[] pkgs = game.getPackageNames();
		if (pkgs != null) {
			for (String pkg : pkgs) {
				if (Utils.isPackageInstalled(context, pkg)) {
					return pkg;
				}
			}
		}
		return null;
	}

	public static class GameAdapter extends CommonAdapter<Game> {

		public GameAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.game_list_item,
						null);
				vh = new ViewHolder();
				vh.image = (ImageView) convertView
						.findViewById(R.id.game_item_image);
				vh.name = (TextView) convertView
						.findViewById(R.id.game_item_name);
				vh.installed = (TextView) convertView
						.findViewById(R.id.game_item_installed);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			Game item = (Game) getItem(position);
			if (item != null) {
				vh.image.setImageResource(ResourceKit.getDrawableResIdByName(
						mContext, item.getIcon()));
				vh.name.setText(item.getName());
				boolean installed = getGameInstalledPkg(mContext, item) != null;
				if (installed) {
					vh.installed.setVisibility(View.VISIBLE);
				} else {
					vh.installed.setVisibility(View.GONE);
				}
			}
			return convertView;
		}

		private static class ViewHolder {
			ImageView image;
			TextView name;
			TextView installed;
		}
	}
}

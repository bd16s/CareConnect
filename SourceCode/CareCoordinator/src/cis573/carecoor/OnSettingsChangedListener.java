package cis573.carecoor;

public interface OnSettingsChangedListener {

	void onSettingsChanged(String setting, int code);

	void onImportFromContact(int type);
}

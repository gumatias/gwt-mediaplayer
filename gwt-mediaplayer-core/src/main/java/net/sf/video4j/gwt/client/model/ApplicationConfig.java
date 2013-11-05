/**
 * 
 */
package net.sf.video4j.gwt.client.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * @author luc
 *
 */
public class ApplicationConfig implements IApplicationConfig {
	
	private JSONArray mPlaylist;
	private JSONObject mCommon;
	private JSONObject mPlugins;
	private JSONObject	mAd;

	@Override
	public JSONObject getCommon() {
		return mCommon;
	}

	@Override
	public JSONArray getPlaylist() {
		return mPlaylist;
	}

	@Override
	public JSONObject getPlugins() {
		return mPlugins;
	}
	
	@Override
	public JSONObject getAd() {
		return mAd;
	}

	public void setPlaylist(JSONArray pJSON) {
		mPlaylist = pJSON;
	}
	
	public void setCommon(JSONObject pCommon) {
		mCommon = pCommon;
	}
	
	public void setPlugins(JSONObject pPlugins) {
		mPlugins = pPlugins;
	}

	public void setAd(JSONObject pAd) {
		mAd = pAd;
	}

}

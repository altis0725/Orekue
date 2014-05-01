package jp.magusa.orekue.android.model;

import com.google.gson.annotations.SerializedName;

public class Title {
    @SerializedName("id")
    long id = 1;
    
    @SerializedName("name")
    String name;
    
    @SerializedName("icon_url")
    String iconUrl;

    public long getId() {
    	return id;
    }
	public String getName() {
		return name;
	}

	public String getIconUrl() {
		return iconUrl;
	}
}


package jp.magusa.orekue.android.model;

import com.google.gson.annotations.SerializedName;

public class Prefix {
    @SerializedName("id")
    long id = 1;
    
    @SerializedName("name")
    String name;
    
    public long getId() {
    	return id;
    }
    public String getName() {
		return name;
	}
}

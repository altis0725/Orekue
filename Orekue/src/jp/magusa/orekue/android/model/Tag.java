package jp.magusa.orekue.android.model;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    long id;
    
    @SerializedName("name")
    String name;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}

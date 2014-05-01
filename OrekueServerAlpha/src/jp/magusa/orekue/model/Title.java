package jp.magusa.orekue.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.annotations.SerializedName;

public class Title {
    @SerializedName("id")
    long id;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

    public void readResultSet( ResultSet rs ) throws SQLException {
    	id = rs.getLong("_id");
    	name = rs.getString( "name" );
    	iconUrl = rs.getString( "icon" );
    }


	@SerializedName("name")
    String name;
    
    @SerializedName("icon_url")
    String iconUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}

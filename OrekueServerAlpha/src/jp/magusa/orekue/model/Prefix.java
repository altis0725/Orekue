package jp.magusa.orekue.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.annotations.SerializedName;

public class Prefix {
    @SerializedName("id")
    long id;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@SerializedName("name")
    String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void readResultSet( ResultSet rs ) throws SQLException {
		setId( rs.getLong( "_id" ) );
		setName( rs.getString( "name" ) );
	}
}

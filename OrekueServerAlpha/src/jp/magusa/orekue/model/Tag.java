package jp.magusa.orekue.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    long id;
    
    @SerializedName("name")
    String name;
    
    public void readResultSet( ResultSet rs ) throws SQLException {
    	setName( rs.getString( "tag_name" ) );
    	setId( rs.getLong( "tag_id" ) );
    }
    
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

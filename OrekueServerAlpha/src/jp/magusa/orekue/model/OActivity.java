package jp.magusa.orekue.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * @author 上野篤史
 *
 */
public class OActivity {
	public long getId() {
		return _id;
	}
	public void setId(long _id) {
		this._id = _id;
	}
	public long getTimeStamp() {
		return time_stamp;
	}
	public void setTimeStamp(long time_stamp) {
		this.time_stamp = time_stamp;
	}
	public long getDeleteTime() {
		return delete_time;
	}
	public void setDeleteTime(long delete_time) {
		this.delete_time = delete_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getUserId() {
		return user_id;
	}
	public void setUserId(long user_id) {
		this.user_id = user_id;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public long getCategoryId() {
		return category_id;
	}
	public void setCategoryId(long category_id) {
		this.category_id = category_id;
	}
	public long getTagId() {
		return tag_id;
	}
	public void setTagId(long tag_id) {
		this.tag_id = tag_id;
	}
	public long getStudyIncrement() {
		return std_inc;
	}
	public void setStudyIncrement(long std_inc) {
		this.std_inc = std_inc;
	}
	public long getExerciseIncrement() {
		return exe_inc;
	}
	public void setExerciseIncrement(long exe_inc) {
		this.exe_inc = exe_inc;
	}
	public long getCommunicationIncrement() {
		return com_inc;
	}
	public void setCommunicationIncrement(long com_inc) {
		this.com_inc = com_inc;
	}
	public long getFashionIncrement() {
		return fas_inc;
	}
	public void setFashionIncrement(long fas_inc) {
		this.fas_inc = fas_inc;
	}
	public long getSocietyIncrement() {
		return soc_inc;
	}
	public void setSocietyIncrement(long soc_inc) {
		this.soc_inc = soc_inc;
	}
	public long getArtIncrement() {
		return art_inc;
	}
	public void setArtIncrement(long art_inc) {
		this.art_inc = art_inc;
	}
	private long _id;
	private long time_stamp;
	private long delete_time;
	private String content;
	private long user_id;
	private long duration;
	private long date;
	private long category_id;
	private long tag_id;
	private long std_inc;
	private long exe_inc;
	private long com_inc;
	private long fas_inc;
	private long soc_inc;
	private long art_inc;
	
	public static class OActivityTimeStampComparator implements Comparator<OActivity>{
		@Override
		public int compare(OActivity arg0, OActivity arg1) {
			return (int) (arg1.getTimeStamp() - arg0.getTimeStamp());
		}
	}
	
	public void readResultSet( ResultSet rs ) throws SQLException{
		setId( rs.getLong( "_id" ) );
		readResultSetWithoutId( rs );
	}
	
	public void readResultSetWithoutId( ResultSet rs ) throws SQLException{
		setTimeStamp( rs.getLong( "time_stamp" ) );
		setDeleteTime( rs.getLong( "deleted_time" ) );
		setContent( rs.getString( "content" ) );
		setUserId( rs.getLong( "user_id"));
		setDuration(rs.getLong("duration"));
		setDate(rs.getLong("date"));
		setCategoryId(rs.getLong("category_id"));
		setTagId(rs.getLong("tag_id"));
		setStudyIncrement(rs.getLong("std_inc"));
		setExerciseIncrement(rs.getLong("exe_inc"));
		setCommunicationIncrement(rs.getLong("com_inc"));
		setFashionIncrement(rs.getLong("fas_inc"));
		setSocietyIncrement(rs.getLong("soc_inc"));
		setArtIncrement(rs.getLong("art_inc"));
	}
	
}

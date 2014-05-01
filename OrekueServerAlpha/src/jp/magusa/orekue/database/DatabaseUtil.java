package jp.magusa.orekue.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.magusa.orekue.model.OActivity;
import jp.magusa.orekue.model.User;

/**
 * @author 上野篤史
 * @version 1.0
 *
 */
public class DatabaseUtil {
	public static final String DATABASE_FILENAME = "/var/database/orekue.db";
	
	public static final long CATEGORY_STUDY = 1L;
	public static final long CATEGORY_EXERCISE = 2L;
	public static final long CATEGORY_COMMUNICATION = 3L;
	public static final long CATEGORY_FASHION = 4L;
	public static final long CATEGORY_SOCIETY = 5L;
	public static final long CATEGORY_ART = 6L;

	private static final String CATEGORY_PARAMETER_NAMES[] = {
		"param_study",
		"param_exercise",
		"param_communication",
		"param_fashion",
		"param_society",
		"param_art"
	};

	public static final long COIN_GACHA = 5;
	public static final long COIN_LEVELUP = 5;
	public static final long COIN_POST = 1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static long getStdInc( long category, long tag, long duration ){
		return category == CATEGORY_STUDY ? duration : 0;
	}
	
	public static long getExeInc( long category, long tag, long duration ){
		return category == CATEGORY_EXERCISE ? duration : 0;
	}
	
	public static long getComInc( long category, long tag, long duration ){
		return category == CATEGORY_COMMUNICATION ? duration : 0; 
	}
	
	public static long getFasInc( long category, long tag, long duration ){
		return category == CATEGORY_FASHION ? duration : 0;
	}
	
	public static long getSocInc( long category, long tag, long duration ){
		return category == CATEGORY_SOCIETY ? duration : 0;
	}
	
	public static long getArtInc( long category, long tag, long duration ){
		return category == CATEGORY_ART ? duration : 0;
	}
	
	public static String getCategoryParameterName( long category ){
		return CATEGORY_PARAMETER_NAMES[ (int)category - 1 ];
	}
	
	public static long getTime(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static List< OActivity > selectOActivity( long user_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List< OActivity > list = new ArrayList< OActivity >();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DATABASE_FILENAME );
			
			//TODO:ソート未実装
			String sql = "SELECT * FROM activity_table WHERE user_id = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1,  user_id );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ){
				OActivity act = new OActivity();
				act.setId( rs.getLong( "_id" ) );
				act.setTimeStamp( rs.getLong( "time_stamp" ) );
				act.setDeleteTime( rs.getLong( "deleted_time" ) );
				act.setContent( rs.getString( "content" ) );
				act.setUserId( rs.getLong( "user_id"));
				act.setDuration(rs.getLong("duration"));
				act.setDate(rs.getLong("date"));
				act.setCategoryId(rs.getLong("category_id"));
				act.setTagId(rs.getLong("tag_id"));
				act.setStudyIncrement(rs.getLong("std_inc"));
				act.setExerciseIncrement(rs.getLong("exe_inc"));
				act.setCommunicationIncrement(rs.getLong("com_inc"));
				act.setFashionIncrement(rs.getLong("fas_inc"));
				act.setSocietyIncrement(rs.getLong("soc_inc"));
				act.setArtIncrement(rs.getLong("art_inc"));
				list.add(act);
			}
			
		} catch( Exception e ){
			e.printStackTrace();
		} finally {
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return list;
		
	}
	
	public static User selectUser( long user_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = new User();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DATABASE_FILENAME );
			
			String sql = "SELECT * FROM user_table WHERE _id = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1,  user_id );
			
			rs = pstmt.executeQuery();
			user.setId( rs.getLong("_id" ) );
			user.setTimeStamp( rs.getLong( "time_stamp" ) );
			user.setDeletedTime( rs.getLong( "deleted_time" ) );
			user.setDeviceId( rs.getString( "device_id" ) );
			user.setAccountName( rs.getString( "account_name" ) );
			user.setName( rs.getString( "name" ) );
			user.setIcon( rs.getString( "icon" ) );
			user.setTitleId( rs.getLong( "title_id" ) );
			user.setPrefixId( rs.getLong( "prefix_id" ) );
			user.setParamStudy( rs.getLong( "param_study" ) );
			user.setParamExercise( rs.getLong( "param_exercise" ) );
			user.setParamCommunication( rs.getLong( "param_communication" ) );
			user.setParamFashion( rs.getLong( "param_fashion" ) );
			user.setParamSociety( rs.getLong( "param_society" ) );
			user.setParamArt( rs.getLong( "param_art" ) );
		} catch( Exception e ){
			e.printStackTrace();
		} finally {
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return user;
		
	}
	
	public static void postActivity( OActivity o ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DATABASE_FILENAME );
			
			String sql = "INSERT INTO activity_table(time_stamp,content,user_id,duration,date,category_id,tag_id) VALUES(?,?,?,?,?,?,?);";
		
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1,o.getTimeStamp() );
			pstmt.setString(2,o.getContent() );
			pstmt.setLong(3,o.getUserId() );
			pstmt.setLong(4,o.getDuration() );
			pstmt.setLong(5,o.getDate() );
			pstmt.setLong(6,o.getCategoryId() );
			pstmt.setLong(7,o.getTagId() );
			
			pstmt.executeUpdate();
			
		} catch( Exception e ){
			e.printStackTrace();
		} finally{
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void insertUser( String device_id,String account_name,String hashed_password,String name){
		Connection conn = null;
		PreparedStatement pstmt = null;
		final long time_stamp = Calendar.getInstance().getTimeInMillis();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DATABASE_FILENAME );
			
			String sql = "INSERT INTO user_table(time_stamp,device_id,account_name,hashed_password,name) VALUES(?,?,?,?,?);";
		
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1,time_stamp );
			pstmt.setString(2,device_id );
			pstmt.setString(3,account_name );
			pstmt.setString(4,hashed_password );
			pstmt.setString(5,name );
			
			pstmt.executeUpdate();
			
		} catch( Exception e ){
			e.printStackTrace();
		} finally{
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	// TODO 
	public static long calcLevel(long paramSum) {
		return paramSum / 300;
	}

}

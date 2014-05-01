package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jp.magusa.orekue.model.OActivity;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.database.DatabaseUtil;

/**
 * Servlet implementation class GetActivityList
 */
@WebServlet("/GetActivityList")
public class GetActivityList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetActivityList() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static OResponse< List< OActivity > > selectOActivity( long user_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OResponse< List< OActivity > > resp = new OResponse< List< OActivity > >();
		List< OActivity > list = new ArrayList< OActivity >();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			//TODO:ソート未実装
			String sql = "SELECT * FROM activity_table WHERE user_id = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1,  user_id );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ){
				OActivity act = new OActivity();
				/*
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
				*/
				act.readResultSet(rs);
				list.add(act);
			}
			
			resp.setData( list );
			
		} catch( SQLException e ){
			resp.setErrorCode( OResponse.ERR_SQL_EXCEPTION );
			resp.setErrorMessage( "SQLException:" + e.toString() );
			e.printStackTrace();
		} catch( ClassNotFoundException e ){
			resp.setErrorCode( OResponse.ERR_UNKNOWN );
			resp.setErrorMessage( "ClassNotFoundException:" + e.toString() );
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
		
		return resp;
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		OResponse< List< OActivity > > resp = new OResponse< List< OActivity > >();
		
		try{
			Long user_id = Long.parseLong(request.getParameter("user_id"));
			resp = selectOActivity( user_id );
		} catch ( NullPointerException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( "パラメータが不正です。" );
			e.printStackTrace();
		} catch ( NumberFormatException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( "パラメータが不正です。" );
			e.printStackTrace();
		}

		Gson gson = new Gson();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write( gson.toJson( resp ) );
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

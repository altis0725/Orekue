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

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OActivity;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.model.Pair;
import jp.magusa.orekue.model.User;

/**
 * Servlet implementation class GetTimeLine
 */
@WebServlet("/GetHomeTimeLine")
public class GetHomeTimeLine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetHomeTimeLine() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OResponse<List<Pair<User,OActivity>> > res = new OResponse< List< Pair<User,OActivity > > >();
		//num_activities = 3
		//lastest_activity_id = 0
		/*
		 SELECT * FROM activity_table, user_table WHERE 
  (user_id IN (SELECT user_id1 FROM friends_table WHERE user_id2 = 5)
   OR user_id IN  (SELECT user_id2 FROM friends_table WHERE user_id1 = 5)
    OR user_id = 5)
  AND user_id = user_table._id AND activity_table._id > 0 ORDER BY time_stamp DESC LIMIT 3;
		 */
		try{
			long user_id = new Long( request.getParameter("user_id") );
			long num_activities = new Long( request.getParameter("num_activities"));
			long last_activity_id = new Long( request.getParameter("lastest_activity_id"));
			//List< OActivity > act = GetTimeLine.getTimeLine(Long.parseLong(request.getParameter("user_id"))).getData();
			//List< Pair<User,OActivity>> data = new ArrayList< Pair<User,OActivity>>();
			
			res = getHomeTimeLine( user_id, num_activities, last_activity_id );
		}
		catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( request.getParameter( "user_id") + "は整数値ではありません。" );
			e.printStackTrace();
		}
		Gson gson = new Gson();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(gson.toJson(res));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
	
	public static OResponse<List<Pair<User,OActivity>>> getHomeTimeLine( long user_id, long num_activities, long last_activity_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OResponse<List<Pair<User,OActivity>>> resp = new OResponse<List<Pair<User,OActivity>>>();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "SELECT * FROM activity_table WHERE "+
					  "(user_id IN (SELECT user_id1 FROM friends_table WHERE user_id2 = ?)" +
					   "OR user_id IN  (SELECT user_id2 FROM friends_table WHERE user_id1 = ?)" +
					    "OR user_id = ?)" +
					  "AND _id > ? ORDER BY time_stamp DESC LIMIT ?";
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1, user_id );
			pstmt.setLong( 2, user_id );
			pstmt.setLong( 3, user_id );
			pstmt.setLong( 4, last_activity_id);
			pstmt.setLong( 5, num_activities );
			
			rs = pstmt.executeQuery();
			List<Pair<User,OActivity>> list = new ArrayList<Pair<User,OActivity>>();
			while(rs.next()){
				OActivity oa = new OActivity();
				oa.readResultSet(rs);
				list.add(new Pair<User,OActivity>(null,oa));
			}
			resp.setData(list);
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
		
		for( int i = 0; i < resp.getData().size(); ++i ){
			resp.getData().get(i).first=GetUser.getUser(resp.getData().get(i).second.getUserId()).getData();
			resp.getData().get(i).first.loadPrefixTitle();
		}
		
		return resp;
		
	}

}

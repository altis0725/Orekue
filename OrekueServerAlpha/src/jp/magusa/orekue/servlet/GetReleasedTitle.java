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

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.model.Title;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetReleasedTitle
 */
@WebServlet("/GetReleasedTitle")
public class GetReleasedTitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReleasedTitle() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long user_id = null;
		OResponse<List<Title>> res = new OResponse<List<Title>>();
		
		try{
			user_id = new Long( request.getParameter( "user_id" ) );
			res = getReleasedTitle(user_id);
		}
		catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( request.getParameter( "user_id") + "は整数値ではありません。" );
			e.printStackTrace();
		}
		Gson gson = new Gson();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write( gson.toJson( res ) );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public static OResponse<List<Title>> getReleasedTitle( long user_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OResponse<List<Title>> resp = new OResponse<List<Title>>();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "select * from title_table where _id in(select title_id from released_title_table WHERE user_id=? AND deleted_time is null );";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1, user_id);
			
			rs = pstmt.executeQuery();
			
			List< Title > list = new ArrayList< Title >();
			while(rs.next()){
				Title t = new Title();
				t.readResultSet(rs);
				list.add(t);
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
		
		return resp;
		
	}

}

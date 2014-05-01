package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 * Servlet implementation class GetTitle
 * @author dat
 * @version 1.0
 * 
 */
@WebServlet("/GetTitle")
public class GetTitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTitle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long id = null;
		OResponse<Title> res = new OResponse<Title>();
		
		try{
			id = new Long( request.getParameter( "title_id" ) );
			res = getTitle(id);
		} catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( request.getParameter( "title_id") + "は整数値ではありません。" );
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
	
	public static OResponse<Title> getTitle( long id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OResponse<Title> resp = new OResponse<Title>();
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "SELECT * FROM title_table WHERE _id = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1,  id );
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				Title title = new Title();
				title.readResultSet(rs);
				resp.setData(title);
			} else {
				//There is no title that corresponds to the title_id.
				
				resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
				resp.setErrorMessage( "title_id:" + id + "の称号IDは存在しません。" );
				resp.setData(null);
			}
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
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if( rs != null ){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return resp;
		
	}

}

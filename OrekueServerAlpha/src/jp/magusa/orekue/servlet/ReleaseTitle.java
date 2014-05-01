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

/**
 * Servlet implementation class ReleaseTitle
 */
@WebServlet("/ReleaseTitle")
public class ReleaseTitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReleaseTitle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean result = releaseTitle( new Long(request.getParameter("user_id")), new Long(request.getParameter("title_id")));
		if( result ){
			response.getWriter().write("succeeded!");
		}
		else{
			response.getWriter().write("failed!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public static boolean releaseTitle( long user_id, long title_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		if( isReleased( user_id, title_id ) ){
			return false;
		}
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "insert into released_title_table(time_stamp,user_id,title_id) values(?,?,?)";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1, DatabaseUtil.getTime());
			pstmt.setLong(2, user_id);
			pstmt.setLong(3, title_id);
			
			pstmt.executeUpdate();
				
		} catch( SQLException e ){
			e.printStackTrace();
		} catch( ClassNotFoundException e ){
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
		}
		return true;
	}
	
	public static boolean isReleased( long user_id, long title_id ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = true;
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "SELECT * FROM released_title_table WHERE user_id = ? and title_id = ?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1,  user_id );
			pstmt.setLong( 2,  title_id );
			
			rs = pstmt.executeQuery();
			ret = rs.next();
				
		} catch( SQLException e ){
			e.printStackTrace();
		} catch( ClassNotFoundException e ){
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
		return ret;
	}

}

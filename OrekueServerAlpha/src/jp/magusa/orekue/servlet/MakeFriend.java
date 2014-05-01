package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetFriendsId
 */
@WebServlet("/MakeFriend")
public class MakeFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeFriend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OResponse<Long> res = new OResponse< Long>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		long userId1, userId2;
		try { 
			userId1 = Long.parseLong(request.getParameter("user_id_1"));
			userId2 = Long.parseLong(request.getParameter("user_id_2"));
		} catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( request.getParameter( "入力パラメータは整数値ではありません。" ));
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(gson.toJson(res));
			e.printStackTrace();
			return;
		}
		
		res.setData(userId2);
		
		List< Long > friends = GetFriendsId.getFriendsId(userId1).getData();
		if( friends.indexOf(userId2) != -1 ){
			res.setErrorCode(1);
			res.setErrorMessage("既に友達です。");
		}
		else{
			try {
				Class.forName( "org.sqlite.JDBC" );
				conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
				String sql = "INSERT INTO friends_table(user_id1,user_id2,time_stamp) VALUES(?,?,?);";
			
				pstmt = conn.prepareStatement( sql );
				pstmt.setLong(1, userId1);
				pstmt.setLong(2, userId2);
				pstmt.setLong(3, DatabaseUtil.getTime());
				pstmt.execute();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				res.setErrorCode( OResponse.ERR_SQL_EXCEPTION );
				//res.setErrorMessage( request.getParameter( e.getCause().toString()));
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write(gson.toJson(res));
				e.printStackTrace();
				return;
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
		}
		
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(gson.toJson(res));
	}
	
}

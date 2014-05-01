package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.User;
import jp.magusa.orekue.model.OResponse;

/**
 * Servlet implementation class SendUserStatus
 */
@WebServlet("/SendCoinInfo")
public class SendCoinInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendCoinInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		OResponse< Boolean > resp = new OResponse< Boolean >();
		Gson gson = new Gson();
		try{
			resp = useCoin( new Long( request.getParameter("user_id") ),DatabaseUtil.COIN_GACHA );
		}
		catch( NullPointerException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( e.getCause().toString() );
			e.printStackTrace();
		} catch ( JsonSyntaxException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( e.getCause().toString() );
			e.printStackTrace();
		}

		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write( gson.toJson( resp ) );
	}
	
	public static OResponse< Boolean > earnCoin( long user_id, long coin ){
		return useCoin( user_id, -coin );
	}
	
	public static OResponse< Boolean > useCoin( long user_id, long coin ){
		OResponse< Boolean > resp = new OResponse< Boolean >();
		Connection conn = null;
		PreparedStatement pstmt = null;
		//final long time_stamp = Calendar.getInstance().getTimeInMillis();
		resp.setData(false);
		User user = GetUser.getUser(user_id).getData();
		if( user.getCoin() < coin ){
			resp.setErrorCode(OResponse.ERR_UNKNOWN);
			resp.setErrorMessage("ガチャをまわすための十分なコインがありません。");
		}

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"
					+ DatabaseUtil.DATABASE_FILENAME);
			

			String sql = "update user_table set coin = ? where _id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user.getCoin() - coin);
			pstmt.setLong(2, user_id);

			pstmt.executeUpdate();
			
			resp.setData(true);
			
		} catch (SQLException e) {
			resp.setErrorCode(OResponse.ERR_SQL_EXCEPTION);
			resp.setErrorMessage("SQLException:" + e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			resp.setErrorCode(OResponse.ERR_UNKNOWN);
			resp.setErrorMessage("ClassNotFoundException:" + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			resp.setErrorCode(OResponse.ERR_UNKNOWN);
			resp.setErrorMessage("Exception:" + e.toString());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return resp;
	}


}

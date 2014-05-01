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
import jp.magusa.orekue.servlet.GetUser;

/**
 * Servlet implementation class SendUserStatus
 */
@WebServlet("/SendUserStatus")
public class SendUserStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendUserStatus() {
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
		
		OResponse< User > resp = new OResponse< User >();
		Gson gson = new Gson();
		try{
			User user = gson.fromJson( request.getParameter("data"), User.class );
			resp = sendUserStatus(user);
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
	
	public static OResponse< User > sendUserStatus( User user ){
		OResponse< User > resp = new OResponse< User >();
		Connection conn = null;
		PreparedStatement pstmt = null;
		//final long time_stamp = Calendar.getInstance().getTimeInMillis();
		User old_user = GetUser.getUser(user.getId()).getData();

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"
					+ DatabaseUtil.DATABASE_FILENAME);

			String sql = "update user_table set "
					+ "name = ?, icon = ?, title_id = ?, prefix_id = ?, "
					+ "param_study = ?, param_exercise = ?, param_communication = ?,"
					+ "param_fashion = ?, param_society = ?, param_art = ?"
					+ "where _id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, new String(user.getName().getBytes("8859_1"), "UTF8"));
			pstmt.setString(2, user.getIcon());
			pstmt.setLong(3, user.getTitleId());
			pstmt.setLong(4, user.getPrefixId());
			pstmt.setLong(5, user.getParamStudy());
			pstmt.setLong(6, user.getParamExercise());
			pstmt.setLong(7, user.getParamCommunication());
			pstmt.setLong(8, user.getParamFashion());
			pstmt.setLong(9, user.getParamFashion());
			pstmt.setLong(10, user.getParamArt());
			pstmt.setLong(11, user.getId());

			pstmt.executeUpdate();
			
			resp.setData(user);
			
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
		
		if( user.getTitleId() != old_user.getTitleId() ){
			onUpdateTitle( user );
		}
		
		return resp;
	}
	
	public static void onUpdateTitle( User user ){
		System.out.println("称号が変更されました。");
		// not implemented
	}


}

package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.model.User;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Gson gson = new Gson();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static OResponse<Long> createUser(User user, String hashed_password) {
		OResponse<Long> resp = new OResponse<Long>(null);
		Connection conn = null;
		PreparedStatement pstmt = null;
		final long time_stamp = Calendar.getInstance().getTimeInMillis();

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"
					+ DatabaseUtil.DATABASE_FILENAME);
			
			//TODO ユーザーが存在したかどかをチェック

			String sql = "INSERT OR REPLACE INTO user_table(time_stamp,device_id,account_name,hashed_password,name,title_id,prefix_id,icon) VALUES(?,?,?,?,?,1,1,?);";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, time_stamp);
			pstmt.setString(2, user.getDeviceId());
			pstmt.setString(3, new String(user.getAccountName().getBytes("8859_1"), "UTF8"));
			pstmt.setString(4, hashed_password);
			pstmt.setString(5, new String(user.getName().getBytes("8859_1"), "UTF8"));
			pstmt.setString(6, "ic_launcher");

			pstmt.executeUpdate();
			
			ResultSet res = pstmt.getGeneratedKeys();
			if (res != null && res.next()){
				long id = res.getLong(1);
				resp.setData(id);
				ReleaseTitle.releaseTitle(id, 1);
				ReleasePrefix.releasePrefix(id, 1);
			}
			// TODO この辺でuser_idを探してresp.setData(user_id);

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OResponse<Long> resp = new OResponse<Long>(null);
		String userJson = null;
		User user = null;
		String hashed_password = request.getParameter("hashed_password");
		if (hashed_password != null)
			try {
				userJson = request.getParameter("user");
				user = gson.fromJson(userJson, User.class);
				resp = createUser(user, hashed_password);
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write(gson.toJson(resp));
				return;
			} catch (Exception e) {
				// TODO: handle exception
			}
		System.out.println("failed to register.");
		resp.setErrorCode(OResponse.ERR_INVALID_PARAMETER);
		resp.setErrorMessage("引数が不正です。");

		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(gson.toJson(resp));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
		// TODO Auto-generated method stub
	}

}

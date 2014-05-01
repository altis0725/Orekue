package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.model.User;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetRanking
 */
@WebServlet("/GetRanking")
public class GetRanking extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRanking() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//GetUser.getUser( );
		Long user_id = null;
		Long category_id = null;
		OResponse<List<User>> res = new OResponse<List<User>>();
		
		try{
			user_id = new Long( request.getParameter( "user_id" ) );
			category_id = new Long( request.getParameter( "category_id" ) );
			res = GetFriendsList.getFriendsList(user_id, 
					"or _id = " + user_id + " order by " + DatabaseUtil.getCategoryParameterName(category_id) + " DESC");
		}
		catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( "整数値を入力してください。" + e.getCause() );
			e.printStackTrace();
		}
		catch( IndexOutOfBoundsException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( "不正なcategory_idです。" + e.getCause() );
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
		doGet(request,response);
	}

}

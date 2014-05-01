package jp.magusa.orekue.servlet;

import java.io.FileWriter;
import java.io.IOException;

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
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static void test(){
    	try {
			FileWriter db = new FileWriter( "test.db", true );
			db.write("test");
			db.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String str_user_id = request.getParameter( "user_id" );
		try{
			Long user_id = new Long( str_user_id );
			if( user_id != null ){
				User user = DatabaseUtil.selectUser( user_id );
				if( user != null ){
					Gson gson = new Gson();
					OResponse< User > res = new OResponse<User>( user );
					String str = gson.toJson( res );
					if( str != null ){
						response.getWriter().write( str );
					}
					else{
						System.out.println( "gson.toJson returns null" );
					}
				}
				else{
					System.out.println( "selectUser returns null" );
				}
			}
		}
		catch( NumberFormatException e ){
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

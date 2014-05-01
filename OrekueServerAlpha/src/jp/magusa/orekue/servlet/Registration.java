package jp.magusa.orekue.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.magusa.orekue.database.DatabaseUtil;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String device_id = request.getParameter( "device_id" );
		String account_name = request.getParameter( "account_name" );
		String hashed_password = request.getParameter( "hashed_password" );
		String name = request.getParameter( "name" );
		System.out.println( "attempt to register..." );
		System.out.println("device_id="+device_id);
		System.out.println("account_name="+account_name);
		System.out.println("hashed_password="+hashed_password);
		System.out.println("name="+name);
		try{
			if( device_id != null && account_name != null && hashed_password != null && name != null ){
				DatabaseUtil.insertUser(device_id, account_name, hashed_password, name);
				System.out.println("finished registering.");
				response.getWriter().write("finished registering.");
			}
			else{
				System.out.println("failed to register.");
				response.getWriter().write("failed to register.");
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

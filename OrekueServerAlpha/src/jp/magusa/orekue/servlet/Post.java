package jp.magusa.orekue.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OActivity;

/**
 * Servlet implementation class Post
 */
@WebServlet("/Post")
public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String content = null;
		Long date = null;
		Long duration = null;
		Long category_id = null;
		Long tag_id = null;
		Long user_id = null;
		try {
			
			user_id = Long.parseLong( request.getParameter( "user_id" ) );
			tag_id = Long.parseLong( request.getParameter( "tag_id" ) );
			category_id = Long.parseLong( request.getParameter( "category_id" ) );
			duration = Long.parseLong( request.getParameter( "duration" ) );
			date = Long.parseLong( request.getParameter( "date" ) );
			content = request.getParameter( "content" );
			
			OActivity oa = new OActivity();
			oa.setUserId(user_id);
			oa.setTagId(tag_id);
			oa.setCategoryId(category_id);
			oa.setDuration(duration);
			oa.setDate(date);
			oa.setContent(content);
			
			oa.setTimeStamp( DatabaseUtil.getTime() );
			
			DatabaseUtil.postActivity( oa );
			
		} catch ( NullPointerException e ){
			e.printStackTrace();
		} catch ( NumberFormatException e ){
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (DEBUG) doPost(request, response);
	}

}

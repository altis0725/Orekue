package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jp.magusa.orekue.model.OActivity;
import jp.magusa.orekue.model.OResponse;

/**
 * Servlet implementation class GetTimeLine
 */
@WebServlet("/GetTimeLine")
public class GetTimeLine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTimeLine() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static OResponse< List< OActivity > > getTimeLine( long user_id ){
    	OResponse< List< OActivity > > resp = new OResponse< List< OActivity > >();
    	OResponse< List< Long > > friends = GetFriendsId.getFriendsId(user_id);
    	// TODO check friends.isSuccess();
    	List< OActivity > data = new ArrayList< OActivity >();
    	
    	for( final Long l : friends.getData() ){
    		OResponse< List< OActivity > > activities = GetActivityList.selectOActivity( l );
    		// TODO check activitis.isSuccess();
    		data.addAll(activities.getData());
    	}
    	
    	// TODO sort data
    	
    	Collections.sort(data, new OActivity.OActivityTimeStampComparator());
    	resp.setData(data);
    	
    	return resp;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OResponse<List<OActivity> > res = new OResponse< List< OActivity > >();
		try{
			res = getTimeLine(Long.parseLong(request.getParameter("user_id")));
		}
		catch( NumberFormatException e ){
			res.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			res.setErrorMessage( request.getParameter( "user_id") + "は整数値ではありません。" );
			e.printStackTrace();
		}
		Gson gson = new Gson();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(gson.toJson(res));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

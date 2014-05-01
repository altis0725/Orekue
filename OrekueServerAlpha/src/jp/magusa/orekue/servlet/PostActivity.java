package jp.magusa.orekue.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.model.OActivityResponse;
import jp.magusa.orekue.model.OResponse;
import jp.magusa.orekue.model.User;
import jp.magusa.orekue.model.Title;
import jp.magusa.orekue.model.Prefix;

/**
 * Servlet implementation class PostActivity
 */
@WebServlet("/PostActivity")
public class PostActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	static final Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostActivity() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static OResponse< OActivityResponse > postActivity( OActivityResponse o ){
    	OResponse< OActivityResponse > resp = new OResponse< OActivityResponse >( o );
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "INSERT INTO activity_table(time_stamp,content,user_id,duration,date,category_id,tag_id"
					+ ",std_inc,exe_inc,com_inc,fas_inc,soc_inc,art_inc) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
		
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1,o.getTimeStamp() );
			pstmt.setString(2,new String(o.getContent().getBytes("8859_1"), "UTF8") );
			pstmt.setLong(3,o.getUserId() );
			pstmt.setLong(4,o.getDuration() );
			pstmt.setLong(5,o.getDate() );
			pstmt.setLong(6,o.getCategoryId() );
			pstmt.setLong(7,o.getTagId() );
			pstmt.setLong(8,o.getStudyIncrement());
			pstmt.setLong(9,o.getExerciseIncrement());
			pstmt.setLong(10,o.getCommunicationIncrement());
			pstmt.setLong(11,o.getFashionIncrement());
			pstmt.setLong(12,o.getSocietyIncrement());
			pstmt.setLong(13,o.getArtIncrement());
			
			pstmt.executeUpdate();
			ResultSet res = pstmt.getGeneratedKeys();
			if (res != null && res.next()){
				long id = res.getLong(1);
				o.setId(id);
			}
			resp.setData(o);
		} catch( SQLException e ){
			resp.setErrorCode( OResponse.ERR_SQL_EXCEPTION );
			resp.setErrorMessage( "SQLException:" + e.toString() );
			e.printStackTrace();
		} catch( ClassNotFoundException e ){
			resp.setErrorCode( OResponse.ERR_UNKNOWN );
			resp.setErrorMessage( "ClassNotFoundException:" + e.toString() );
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			resp.setErrorCode( OResponse.ERR_UNKNOWN );
			resp.setErrorMessage( "UnsupportedEncodingException:" + e.toString() );
			e.printStackTrace();
		} finally{
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
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
    
    public static long convertPrefixIdToCategoryId( long prefix_id ){
    	int i = (int)prefix_id;
    	int mapping[] = {2,1,3,5,4,6};
		int category = (i-2)/3;
		if( category >= 6 ){
			category = i - 20;
		}
		category = mapping[category];
		return category;
    }
    
    //称号に関する仕様が変更されるたびにここを確認すること
    public static List< Title > updateReleasedTitle( long user_id ){
    	List< Title > titles = new ArrayList< Title >();
    	/* id2~25までの称号解放条件を満たしているか調べる */
    	User user = GetUser.getUser(user_id).getData();
    	long params[] = user.getParameters();
    	int mapping[] = {2,1,3,5,4,6};
    	for( int i = 2; i < 26; ++i ){
    		System.out.println("loop:"+i);
    		int category = (i-2)/3;
    		int rank = (i-2)%3;
    		if( category >= 6 ){
    			category = i - 20;
    			rank = 3;
    		}
    		category = mapping[category]-1;
    		boolean rel = false;
    		System.out.println("category:"+category);
    		System.out.println("rank:"+rank);
    		switch(rank){
    		case 0:
    			if( params[category] >= 1 ){
    				rel = true;
    			}
    			break;
    		case 1:
    			if( params[category] >= 1500 ){
    				rel = true;
    			}
    			break;
    		case 2:
    			if( params[category] >= 6000 ){
    				rel = true;
    			}
    			break;
    		case 3:
    			if( params[category] >= 3000 ){
    				int j;
    				for( j = 0; j < 6; ++j ){
    					if( j != category && params[category] - params[j] < 3000 ){
    						break;
    					}
    				}
    				if( j == 6 ){
    					rel = true;
    				}
    			}
    			break;
    		default:
    			System.out.println("unexpected rank num:" + rank );
    			assert(false);
    			break;
    		}
    		if( rel ){
    			if( ReleaseTitle.releaseTitle(user_id, i) ){
    				System.out.println("新しい称号を手に入れた！");
    				Title title = GetTitle.getTitle(i).getData();
    				System.out.println(title.getName());
    				titles.add( title );
    			}
    		}
    	}
    	return titles;
    }
    
    public static List< Prefix > updateReleasedPrefix( long user_id ){
    	List< Prefix > prefixes = new ArrayList< Prefix >();
    	User user = GetUser.getUser(user_id).getData();
    	long params[] = user.getPreParameters();
    	int mapping[] = {2,1,4,5,6,3};
    	for( int i = 0; i < 6; ++ i){
    		System.out.println(params[i]);
    	}
    	for( int i = 2; i < 8; ++ i){
    		int category = mapping[i-2];
    		if( params[category-1] >= 1500 ){
    			long rel_id = i; 
    			if( convertPrefixIdToCategoryId(user.getPrefixId() ) == category ){
    				rel_id = 8;
    			}
    			if( ReleasePrefix.releasePrefix(user_id, rel_id)){
    				System.out.println("新しい接頭辞を手に入れた。");
    				Prefix prefix = GetPrefix.getPrefix(rel_id).getData();
    				System.out.println(prefix.getName());
    				prefixes.add(prefix);
    			}
    			else{
    				System.out.println( "既に解放済み：" + i );
    			}
    		}
    	}
    	return prefixes;
    }
    
    public static void updateParameter( User user ){
    	Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			Class.forName( "org.sqlite.JDBC" );
			conn = DriverManager.getConnection( "jdbc:sqlite:" + DatabaseUtil.DATABASE_FILENAME );
			
			String sql = "UPDATE user_table SET param_study=?, param_exercise=?, param_communication=?, param_fashion=?, param_society=?, param_art=? "
					+ ", pre_std=?,pre_exe=?,pre_com=?,pre_fas=?,pre_soc=?,pre_art=? WHERE _id=?;";
		
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong(1, user.getParamStudy() );
			pstmt.setLong(2, user.getParamExercise());
			pstmt.setLong(3, user.getParamCommunication());
			pstmt.setLong(4, user.getParamFashion());
			pstmt.setLong(5, user.getParamSociety());
			pstmt.setLong(6, user.getParamArt());
			pstmt.setLong(7, user.getPreStd());
			pstmt.setLong(8, user.getPreExe());
			pstmt.setLong(9, user.getPreCom());
			pstmt.setLong(10, user.getPreFas());
			pstmt.setLong(11, user.getPreSoc());
			pstmt.setLong(12, user.getPreArt());
			pstmt.setLong(13, user.getId());
			
			pstmt.executeUpdate();
			
		} catch( SQLException e ){
			e.printStackTrace();
		} catch( ClassNotFoundException e ){
			e.printStackTrace();
		} finally{
			if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if( pstmt != null ){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		OResponse< OActivityResponse > resp = new OResponse< OActivityResponse >( null );
		OActivityResponse oa = null;
		User user = null;
		long old_level = 0;
		long user_id = -1;
		
		try {
			
			oa = gson.fromJson(request.getParameter( "data" ), OActivityResponse.class);
		} catch ( Exception e ){
			e.printStackTrace();
		}
		try{
			Long duration = oa.getDuration();
			long category_id = oa.getCategoryId();
			long tag_id = oa.getTagId();
			user_id = oa.getUserId();
			
			long std_inc = DatabaseUtil.getStdInc( category_id, tag_id, duration );
			long exe_inc = DatabaseUtil.getExeInc( category_id, tag_id, duration );
			long com_inc = DatabaseUtil.getComInc( category_id, tag_id, duration );
			long fas_inc = DatabaseUtil.getFasInc( category_id, tag_id, duration );
			long soc_inc = DatabaseUtil.getSocInc( category_id, tag_id, duration );
			long art_inc = DatabaseUtil.getArtInc( category_id, tag_id, duration );
			
			long earned_coin = 0L;
			
			oa.setStudyIncrement(std_inc);
			oa.setExerciseIncrement(exe_inc);
			oa.setCommunicationIncrement(com_inc);
			oa.setFashionIncrement(fas_inc);
			oa.setSocietyIncrement(soc_inc);
			oa.setArtIncrement(art_inc);
			
			oa.setTimeStamp( DatabaseUtil.getTime() );
			
			resp = postActivity( oa );
			
			// update user's parameter.
			OResponse< User > res_user = GetUser.getUser( user_id );
			user = res_user.getData();
			user.setParamStudy(user.getParamStudy()+std_inc);
			user.setParamExercise(user.getParamExercise()+exe_inc);
			user.setParamCommunication(user.getParamCommunication()+com_inc);
			user.setParamFashion(user.getParamFashion()+fas_inc);
			user.setParamSociety(user.getParamSociety()+soc_inc);
			user.setParamArt(user.getParamArt()+art_inc);
			user.setPreStd(user.getPreStd()+std_inc);
			user.setPreExe(user.getPreExe()+exe_inc);
			user.setPreCom(user.getPreCom()+com_inc);
			user.setPreFas(user.getPreFas()+fas_inc);
			user.setPreSoc(user.getPreSoc()+soc_inc);
			user.setPreArt(user.getPreArt()+art_inc);
			old_level = user.getParamLevel();
			user.setParamLevel(DatabaseUtil.calcLevel(user.getParamSum()));
			oa.setIncreasedLevel(user.getParamLevel()-old_level);

			System.out.println("コイン獲得");
			// コイン獲得処理
			if( oa.getIncreasedLevel() > 0 ){
				SendCoinInfo.earnCoin(user_id, oa.getIncreasedLevel() * DatabaseUtil.COIN_LEVELUP );
				earned_coin += oa.getIncreasedLevel() * DatabaseUtil.COIN_LEVELUP;
			}
			SendCoinInfo.earnCoin(user_id, DatabaseUtil.COIN_POST);
			earned_coin += DatabaseUtil.COIN_POST;
			
			System.out.println("パラメータ更新");
			// パラメータアップデート
			updateParameter( user );
			
			System.out.println("称号獲得");
			// 称号・接頭辞獲得判定
			resp.getData().setEarnedTitle(updateReleasedTitle(user_id));
			System.out.println("接頭辞獲得");
			resp.getData().setEarnedPrefix(updateReleasedPrefix(user_id));
			
			// コイン獲得
			resp.getData().setEarnedCoin(earned_coin);
			
		} catch ( NullPointerException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( "パラメータが不正です。" + e.getCause().toString());
			e.printStackTrace();
		} catch ( NumberFormatException e ){
			resp.setErrorCode( OResponse.ERR_INVALID_PARAMETER );
			resp.setErrorMessage( "パラメータが不正です。" + e.getCause().toString());
			e.printStackTrace();
		} catch ( Exception e ){
			resp.setErrorCode( OResponse.ERR_UNKNOWN );
			resp.setErrorMessage(e.getCause().toString());
			e.printStackTrace();
		}
		
		System.out.println("おわり");
		
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write( gson.toJson( resp ) );
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (DEBUG) doPost(request, response);
	}

}

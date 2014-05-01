package jp.magusa.orekue.android.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;

public class DataStore {
	public static final Gson gson = new Gson();
	static List<Category> sCategories;

	public static final Long CATEGORY_STUDY = 1l;
	public static final Long CATEGORY_EXERCISE = 2l;
	public static final Long CATEGORY_COMUNICATION = 3l;
	public static final Long CATEGORY_FASHION = 4l;
	public static final Long CATEGORY_SOCIETY = 5l;
	public static final Long CATEGORY_ART = 6l;

	static {
		sCategories = new ArrayList<Category>();
		Category cat = new Category();
		cat.setId(CATEGORY_STUDY);
		cat.setName("勉強");
		sCategories.add(cat);
		cat = new Category();
		cat.setId(CATEGORY_EXERCISE);
		cat.setName("運動");
		sCategories.add(cat);
		cat = new Category();
		cat.setId(CATEGORY_COMUNICATION);
		cat.setName("コミュニケーション");
		sCategories.add(cat);
		cat = new Category();
		cat.setId(CATEGORY_FASHION);
		cat.setName("ファッション");
		sCategories.add(cat);
		cat = new Category();
		cat.setId(CATEGORY_SOCIETY);
		cat.setName("社会勉強");
		sCategories.add(cat);
		cat = new Category();
		cat.setId(CATEGORY_ART);
		cat.setName("芸術");
		sCategories.add(cat);
	}
	static final boolean DEBUG = true;
	static long myUserId = -1;

	static HashMap<Long, OResponse<List<Tag>>> tagMap = new HashMap<Long, OResponse<List<Tag>>>();

	static HashMap<Long, String> tagNameMap = new HashMap<Long, String>();

	//public static final String BASE_URL = "http://54.200.44.168:8080/orekue/";
	public static final String BASE_URL = "http://kaseikari.com:8080/orekue/";

	public static void setMyUserId(long id) {
		myUserId = id;
	}

	public static long getMyUserId() {
		return myUserId;
	}

	/**
	 * カテゴリリスト取得 非同期でなくてもいい
	 */
	public static List<Category> getCategories() {
		return sCategories;
	}

	/**
	 * タグリスト取得 非同期でなくてもいい
	 */

	public static class TagListResponse extends OResponse<List<Tag>> {
	}

	public static OResponse<List<Tag>> getTagsByCategoryId(long catId)
			throws MalformedURLException, IOException {
		OResponse<List<Tag>> res;
		res = tagMap.get(catId);
		if (res != null)
			return res;

		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("category_id", String.valueOf(catId)));
		res = NetworkUtil.sendGetRequest(TagListResponse.class, BASE_URL
				+ "GetTags", params);
		if (res != null) {
			if (res.errorCode == 0) {
				tagMap.put(catId, res);
			}
		}
		Log.i("Tag", "" + res);
		return res;
	}

	public static OResponse<List<Tag>> getAllTags()
			throws MalformedURLException, IOException {
		OResponse<List<Tag>> res = NetworkUtil.sendGetRequest(
				TagListResponse.class, BASE_URL + "GetAllTags", null);
		if (res != null) {
			if (res.errorCode == 0) {
				for (Tag t : res.data) {
					tagNameMap.put(t.getId(), t.name);
				}
			}
		}
		return res;
	}

	public static String getTagName(long tagId) {
		String s = tagNameMap.get(tagId);
		if (s != null)
			return s;
		return "" + tagId;
	}

	public static class UserRegResopnse extends OResponse<Long> {
	}

	public static OResponse<Long> register(User user, String hashed_password)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user", gson.toJson(user)));
		params.add(new MyNameValuePair("hashed_password", hashed_password));
		return NetworkUtil.sendPostRequest(UserRegResopnse.class, BASE_URL
				+ "CreateUser", params);
	}

	public static class UserResponse extends OResponse<User> {
	}

	/** ユーザ情報取得 */
	public static OResponse<User> getUser(long user_id)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(user_id)));
		return NetworkUtil.sendGetRequest(UserResponse.class, BASE_URL
				+ "GetUser", params);
	}

	public static class UsersResponse extends OResponse<List<User>> {
	}

	/**
	 * 友達のUser情報のリストを得る
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<List<User>> getFriends()
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		return NetworkUtil.sendGetRequest(UsersResponse.class, BASE_URL
				+ "GetFriendsList", params);
	}

	public static class CommonResponse extends OResponse<OActivityResponse> {
	}

	/**
	 * ポスト画面の入力を送る
	 * 
	 * @return OResponse data:success message
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<OActivityResponse> postActivity(OActivity act)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		// params.add(new MyNameValuePair("activity", gson.toJson(act)));
		// params.add(new MyNameValuePair("user_id",
		// String.valueOf(act.getUserId())));
		params.add(new MyNameValuePair("data", gson.toJson(act)));
		return NetworkUtil.sendPostRequest(CommonResponse.class, BASE_URL
				+ "PostActivity", params);
	}

	public static class ActivitiesResponse extends OResponse<List<OActivity>> {
	}

	/**
	 * α版　：　1人分の活動　→　友達全員が必要　→　β版 タイムライン用のポストデータを取得する
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<List<OActivity>> getActivities(int num,
			long maxActId) throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("num_activities", String.valueOf(num)));
		params.add(new MyNameValuePair("max_activity_id", String
				.valueOf(maxActId)));
		return NetworkUtil.sendGetRequest(ActivitiesResponse.class, BASE_URL
				+ "GetActivityList", params);
	}

	/**
	 * タイムライン用のポストデータを取得する 現在　件数(num), 最後に取得した Id を対応していない
	 */
	public static OResponse<List<OActivity>> getTimeline(int num,
			long lastestActId) throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("num_activities", String.valueOf(num)));
		params.add(new MyNameValuePair("lastest_activity_id", String
				.valueOf(lastestActId)));
		return NetworkUtil.sendGetRequest(ActivitiesResponse.class, BASE_URL
				+ "GetTimeLine", params);
	}

	static class UserActivityPairRes extends
			OResponse<List<Pair<User, OActivity>>> {
	};

	public static OResponse<List<Pair<User, OActivity>>> getHomeTimeLine(
			int num, long lastestActId) throws MalformedURLException,
			IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("num_activities", String.valueOf(num)));
		params.add(new MyNameValuePair("lastest_activity_id", String
				.valueOf(lastestActId)));
		return NetworkUtil.sendGetRequest(UserActivityPairRes.class, BASE_URL
				+ "GetHomeTimeLine", params);
	}

	/**
	 * 自分の活動情報のリストを取得 現在　件数(num), 最後に取得した Id を対応していない
	 */
	public static OResponse<List<OActivity>> getMyActivities(int num,
			long lastestActId) throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("num_activities", String.valueOf(num)));
		params.add(new MyNameValuePair("lastest_activity_id", String
				.valueOf(lastestActId)));
		return NetworkUtil.sendGetRequest(ActivitiesResponse.class, BASE_URL
				+ "GetMyActivities", params);
	}

	public static class PrefixResponse extends OResponse<Prefix> {
	}

	/**
	 * 接頭辞IDに応じた接頭辞名を得る
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<Prefix> getPrefix(long prefix_id)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("prefix_id", String.valueOf(prefix_id)));
		return NetworkUtil.sendGetRequest(PrefixResponse.class, BASE_URL
				+ "GetPrefix", params);
	}

	public static class RankingResponse extends OResponse<List<User>> {
	}

	/**
	 * カテゴリIDについてのランキングを取得する
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<List<User>> getRanking(long category_id)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("category_id", String
				.valueOf(category_id)));
		return NetworkUtil.sendGetRequest(RankingResponse.class, BASE_URL
				+ "GetRanking", params);
	}

	public static class TitleResponse extends OResponse<Title> {
	}

	/**
	 * 称号IDに応じた称号名、アイコン画像のパスを得る
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<Title> getTitle(long title_id)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("title_id", String.valueOf(title_id)));
		return NetworkUtil.sendGetRequest(TitleResponse.class, BASE_URL
				+ "GetTitle", params);
	}

	public static class MakeFriendsResponse extends OResponse<Long> {
	}

	/**
	 * 友達登録をする
	 * 
	 * @return 友達登録した相手のUserデータ
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static OResponse<Long> makeFriends(long friendUserId)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id_1", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("user_id_2", String
				.valueOf(friendUserId)));
		return NetworkUtil.sendGetRequest(MakeFriendsResponse.class, BASE_URL
				+ "MakeFriend", params);
	}

	public static class TitlesResponse extends OResponse<List<Title>> {
	}

	/**
	 * 解放された称号を取得する
	 * 
	 * @return 解放された称号クラスのリスト
	 */
	public static OResponse<List<Title>> getReleasedTitle()
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		return NetworkUtil.sendGetRequest(TitlesResponse.class, BASE_URL
				+ "GetReleasedTitle", params);
	}

	public static class PrefixesResponse extends OResponse<List<Prefix>> {
	}

	/**
	 * 解放された接頭辞を取得する
	 * 
	 * @return 解放された接頭辞クラスのリスト
	 */
	public static OResponse<List<Prefix>> getReleasedPrefix()
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		return NetworkUtil.sendGetRequest(PrefixesResponse.class, BASE_URL
				+ "GetReleasedPrefix", params);
	}

	public static class GachaResponse extends OResponse<Boolean> {
	}

	/**
	 * ガチャの通信が正しく行われたか
	 * 
	 * @return 正しく行われればtrue, それ以外はfalse
	 */
	public static OResponse<Boolean> drawGacha() throws MalformedURLException,
			IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id", String.valueOf(myUserId)));
		return NetworkUtil.sendPostRequest(GachaResponse.class, BASE_URL
				+ "SendCoinInfo", params);
	}

	/**
	 * ユーザー情報アップデート
	 * 
	 * @return userクラス
	 */
	public static OResponse<User> sendUserStatus(User act)
			throws MalformedURLException, IOException {
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("data", gson.toJson(act)));
		return NetworkUtil.sendPostRequest(UserResponse.class, BASE_URL
				+ "SendUserStatus", params);
	}
	
	public static class RemoveFriendResponse extends OResponse<Boolean> {
	}
	public static OResponse<Boolean> removeFriend(long friendId) throws MalformedURLException, IOException{
		List<MyNameValuePair> params = new ArrayList<MyNameValuePair>();
		params.add(new MyNameValuePair("user_id_1", String.valueOf(myUserId)));
		params.add(new MyNameValuePair("user_id_2", String.valueOf(friendId)));
		return NetworkUtil.sendPostRequest(RemoveFriendResponse.class, BASE_URL
				+ "RemoveFriend", params);
	}
}

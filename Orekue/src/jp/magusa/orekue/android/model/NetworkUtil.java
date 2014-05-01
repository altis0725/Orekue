package jp.magusa.orekue.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.GZIPInputStream;

import jp.maguro.vs.samon.orekue.BuildConfig;
import android.util.Log;

import com.google.gson.Gson;

public class NetworkUtil {
	public static final String TAG = "NetworkUtil";
	private static final Gson gson = DataStore.gson;
	private static final boolean DEBUG = BuildConfig.DEBUG;

	public static <T> OResponse<T> sendGetRequest(
			Class<? extends OResponse<T>> clazz, String url,
			List<MyNameValuePair> params) throws MalformedURLException,
			IOException {
		try {
			if (params != null) {
				StringBuilder builder = new StringBuilder(url).append("?");
				for (MyNameValuePair pr : params) {
					builder.append(pr.name).append("=")
							.append(URLEncoder.encode(pr.value, "utf-8"))
							.append("&");
				}
				builder.deleteCharAt(builder.length() - 1);
				url = builder.toString();
				Log.i(TAG, "url = " + url);
			}

			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setRequestProperty("Accept-Encoding", "gzip");
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			// con.setInstanceFollowRedirects(false);
			con.connect();

			int responseCode = con.getResponseCode();
			Log.i(TAG, "responseCode = " + responseCode);
			OResponse<T> result = null;
			if (responseCode == 200) {
				InputStream in = con.getInputStream();
				if ("gzip".equals(con.getContentEncoding())) {
					in = new GZIPInputStream(in);
				}
				if (in == null) {
					return null;
				}
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						in, "utf-8"));
				if (DEBUG) {
					String s = "", ss;
					while ((ss = buf.readLine()) != null) {
						Log.d("String", ss);
						s = s + ss;
					}
					result = gson.fromJson(s, clazz);
				} else {
					result = gson.fromJson(buf, clazz);
				}
				buf.close();
				in.close();
			}
			if (result!= null) return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		OResponse<T> res = new OResponse<T>();
		res.setErrorCode(1);
		return res;
	}

	// Post string
	// for file ->
	// http://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection
	public static <T> OResponse<T> sendPostRequest(
			Class<? extends OResponse<T>> clazz, String url,
			List<MyNameValuePair> params) throws MalformedURLException,
			IOException {
		try {
			byte[] postData = null;
			if (params != null) {
				StringBuilder builder = new StringBuilder();
				for (MyNameValuePair pr : params) {
					builder.append(pr.name).append("=")
							.append(URLEncoder.encode(pr.value, "utf-8"))
							.append("&");
				}
				builder.deleteCharAt(builder.length() - 1);
				postData = builder.toString().getBytes();
			} else
				postData = new byte[0];

			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setRequestProperty("Accept-Encoding", "gzip");
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Length",
					Integer.toString(postData.length));
			con.setUseCaches(false);

			OutputStream out = con.getOutputStream();
			out.write(postData);
			out.close();

			int responseCode = con.getResponseCode();
			Log.i(TAG, "responseCode = " + responseCode);
			OResponse<T> result = null;
			if (responseCode == 200) {
				InputStream in = con.getInputStream();
				if ("gzip".equals(con.getContentEncoding())) {
					in = new GZIPInputStream(in);
				}
				if (in == null) {
					return null;
				}
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						in, "utf-8"));
				if (DEBUG) {
					String s = "", ss;
					while ((ss = buf.readLine()) != null) {
						Log.d("String", ss);
						s = s + ss;
					}
					result = gson.fromJson(s, clazz);
				} else {
					result = gson.fromJson(buf, clazz);
				}
				buf.close();
				in.close();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		OResponse<T> res = new OResponse<T>();
		res.setErrorCode(1);
		return res;
	}
}

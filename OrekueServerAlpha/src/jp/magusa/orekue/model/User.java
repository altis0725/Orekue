package jp.magusa.orekue.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.magusa.orekue.database.DatabaseUtil;
import jp.magusa.orekue.servlet.GetPrefix;
import jp.magusa.orekue.servlet.GetTitle;

public class User {
	private long _id;
	private long time_stamp;
	private long deleted_time;
	private String device_id;
	private String account_name;
	//private String hashed_password;
	private String name;
	private String icon;
	private long title_id;
	private long prefix_id;
	private long param_study;
	private long param_exercise;
	private long param_communication;
	private long param_fashion;
	private long param_society;
	private long param_art;
	private long param_level;
	
	private long pre_std;
	private long pre_exe;
	private long pre_com;
	private long pre_fas;
	private long pre_soc;
	private long pre_art;
	
	// added
	private long coin;
	
	private Title title;
	private Prefix prefix;
	
	public void readResultSet( ResultSet rs ) throws SQLException {
		setId( rs.getLong("_id" ) );
		readResultSetWithoutId(rs);
	}
	
	public void readResultSetWithoutId( ResultSet rs ) throws SQLException {
		setTimeStamp( rs.getLong( "time_stamp" ) );
		setDeletedTime( rs.getLong( "deleted_time" ) );
		setDeviceId( rs.getString( "device_id" ) );
		setAccountName( rs.getString( "account_name" ) );
		setName( rs.getString( "name" ) );
		setIcon( rs.getString( "icon" ) );
		setTitleId( rs.getLong( "title_id" ) );
		setPrefixId( rs.getLong( "prefix_id" ) );
		setParamStudy( rs.getLong( "param_study" ) );
		setParamExercise( rs.getLong( "param_exercise" ) );
		setParamCommunication( rs.getLong( "param_communication" ) );
		setParamFashion( rs.getLong( "param_fashion" ) );
		setParamSociety( rs.getLong( "param_society" ) );
		setParamArt( rs.getLong( "param_art" ) );
		setPreStd( rs.getLong( "pre_std" ) );
		setPreExe( rs.getLong( "pre_exe" ) );
		setPreCom( rs.getLong( "pre_com" ) );
		setPreFas( rs.getLong( "pre_fas" ) );
		setPreSoc( rs.getLong( "pre_soc" ) );
		setPreArt( rs.getLong( "pre_art" ) ) ;
		setCoin( rs.getLong("coin"));
		setParamLevel(DatabaseUtil.calcLevel(getParamSum()));
	}
	
	public void loadPrefixTitle(){
		setPrefix( GetPrefix.getPrefix(getPrefixId()).getData());
		setTitle( GetTitle.getTitle(getTitleId()).getData());
	}
	
	public long getParamSum(){
		return param_study + param_exercise + param_communication + param_fashion + param_society + param_art;
	}
	
	public long[] getParameters(){
		long parameters[] = {
				getParamStudy(),getParamExercise(),getParamCommunication(),
				getParamFashion(),getParamSociety(),getParamArt()
		};
		return parameters;
	}
	
	public long[] getPreParameters(){
		long parameters[] = {
				getPreStd(),getPreExe(),getPreCom(),
				getPreFas(),getPreSoc(),getPreArt()
		};
		return parameters;
	}
	
	public long getParamLevel() {
		return param_level;
	}
	public void setParamLevel(long param_level) {
		this.param_level = param_level;
	}
	public long getId() {
		return _id;
	}
	public void setId(long _id) {
		this._id = _id;
	}
	public long getTimeStamp() {
		return time_stamp;
	}
	public void setTimeStamp(long time_stamp) {
		this.time_stamp = time_stamp;
	}
	public long getDeletedTime() {
		return deleted_time;
	}
	public void setDeletedTime(long deleted_time) {
		this.deleted_time = deleted_time;
	}
	public String getDeviceId() {
		return device_id;
	}
	public void setDeviceId(String device_id) {
		this.device_id = device_id;
	}
	public String getAccountName() {
		return account_name;
	}
	public void setAccountName(String account_name) {
		this.account_name = account_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return アイコンのファイル名を返す
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon アイコンのファイル名を引数にとる
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public long getTitleId() {
		return title_id;
	}
	public void setTitleId(long title_id) {
		this.title_id = title_id;
	}
	public long getPrefixId() {
		return prefix_id;
	}
	public void setPrefixId(long prefix_id) {
		this.prefix_id = prefix_id;
	}
	public long getParamStudy() {
		return param_study;
	}
	public void setParamStudy(long param_study) {
		this.param_study = param_study;
	}
	public long getParamExercise() {
		return param_exercise;
	}
	public void setParamExercise(long param_exercise) {
		this.param_exercise = param_exercise;
	}
	public long getParamCommunication() {
		return param_communication;
	}
	public void setParamCommunication(long param_communication) {
		this.param_communication = param_communication;
	}
	public long getParamFashion() {
		return param_fashion;
	}
	public void setParamFashion(long param_fashion) {
		this.param_fashion = param_fashion;
	}
	public long getParamSociety() {
		return param_society;
	}
	public void setParamSociety(long param_society) {
		this.param_society = param_society;
	}
	public long getParamArt() {
		return param_art;
	}
	public void setParamArt(long param_art) {
		this.param_art = param_art;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Prefix getPrefix() {
		return prefix;
	}

	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}

	public long getPreStd() {
		return pre_std;
	}

	public void setPreStd(long pre_std) {
		this.pre_std = pre_std;
	}

	public long getPreExe() {
		return pre_exe;
	}

	public void setPreExe(long pre_exe) {
		this.pre_exe = pre_exe;
	}

	public long getPreCom() {
		return pre_com;
	}

	public void setPreCom(long pre_com) {
		this.pre_com = pre_com;
	}

	public long getPreFas() {
		return pre_fas;
	}

	public void setPreFas(long pre_fas) {
		this.pre_fas = pre_fas;
	}

	public long getPreSoc() {
		return pre_soc;
	}

	public void setPreSoc(long pre_soc) {
		this.pre_soc = pre_soc;
	}

	public long getPreArt() {
		return pre_art;
	}

	public void setPreArt(long pre_art) {
		this.pre_art = pre_art;
	}
}

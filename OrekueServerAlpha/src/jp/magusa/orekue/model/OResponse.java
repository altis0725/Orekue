package jp.magusa.orekue.model;

import com.google.gson.annotations.SerializedName;

public class OResponse<T> {
	
	public static final int ERR_UNKNOWN = -1;
	public static final int OK = 0;
	public static final int ERR_INVALID_PARAMETER = 1;
	public static final int ERR_SQL_EXCEPTION = 2;
	
    @SerializedName("err_code")
    int errorCode;
    
    @SerializedName("err_mess")
    String errorMessage;
    
    @SerializedName("data")
    public T data;
    
    public OResponse( T d ){
    	this.data = d;
    	errorCode = OK;
    	errorMessage = "";
    }
    
    public OResponse() {
    	this.data = null;
    	errorCode = OK;
    	errorMessage = "";
	}

	public boolean isSuccess(){
        return errorCode != OK;
    }
    
    public void setErrorCode( int errorCode ){
    	this.errorCode = errorCode;
    }
    
    public int getErrorCode(){
    	return errorCode;
    }
    
    public void setErrorMessage( String msg ){
    	this.errorMessage = msg;
    }
    
    public String getErrorMessage(){
    	return errorMessage;
    }
    
    public void setData( T data ){
    	this.data = data;
    }
    
    public T getData(){
    	return data;
    }
}

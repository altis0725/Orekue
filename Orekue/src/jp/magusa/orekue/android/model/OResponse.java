package jp.magusa.orekue.android.model;

import com.google.gson.annotations.SerializedName;

public class OResponse<T> {
    @SerializedName("err_code")
    int errorCode;
    
    @SerializedName("err_mess")
    String errorMessage;
    
    @SerializedName("data")
    public T data;
    
    public boolean isSuccess(){
        return errorCode == 0;
    }
    
    public int getErrorCode(){
    	return errorCode;
    }
    
    public void setErrorCode(int code){
    	errorCode = code;
    }
}

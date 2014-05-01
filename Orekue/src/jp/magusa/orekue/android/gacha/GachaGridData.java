package jp.magusa.orekue.android.gacha;

import android.graphics.Bitmap;

public class GachaGridData {

	private Bitmap imageData_;
	private int numData_;
	private int idData_;
	
	public GachaGridData(){
		
	}
	
	public void setImageData(Bitmap image) {
		imageData_ = image;
	}

	public Bitmap getImageData() {
		return imageData_;
	}

	public void setNumData(int num) {
		numData_ = num;
	}

	public int getNumData() {
		return numData_;
	}

	public void setId(int id){
		idData_ = id;
	}
	
	public int getId() {
		return idData_;
	}
}

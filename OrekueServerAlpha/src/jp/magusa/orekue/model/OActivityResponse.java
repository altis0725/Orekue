package jp.magusa.orekue.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OActivityResponse extends OActivity {
	@SerializedName("incleased_level")
	private long increased_level;
	
	@SerializedName("earned_title")
	private List<Title> earned_title;
	
	@SerializedName("earned_prefix")
	private List<Prefix> earned_prefix;
	
	@SerializedName("earned_coin")
	private long earned_coin;
	

	public long getIncreasedLevel() {
		return increased_level;
	}

	public void setIncreasedLevel(long increased_level) {
		this.increased_level = increased_level;
	}

	public List<Title> getEarnedTitle() {
		return earned_title;
	}

	public void setEarnedTitle(List<Title> earned_title) {
		this.earned_title = earned_title;
	}

	public List<Prefix> getEarnedPrefix() {
		return earned_prefix;
	}

	public void setEarnedPrefix(List<Prefix> earned_prefix) {
		this.earned_prefix = earned_prefix;
	}
	
	public long getEarnedCoin() {
		return earned_coin;
	}
	
	public void setEarnedCoin(long earned_coin) {
		this.earned_coin = earned_coin;
	}
}

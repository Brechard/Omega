package com.um.omega.game;

public class TTInfo {
	
	public int flag;
	public long value;
	public String playHistory;
	
	public TTInfo(int flag, long value, String playHistory) {
		this.flag = flag;
		this.value = value;
		this.playHistory = playHistory;
	}
	
	public TTInfo(int flag, String[] info) {
		this.flag = flag;
		this.value = Long.valueOf(info[0]);
		this.playHistory = info[1];
	}

	public String[] getInfo(){
		return new String[]{String.valueOf(value), playHistory};
	}
	
}

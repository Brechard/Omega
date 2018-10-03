package com.um.omega.game;

import Helpers.Flag;

public class TTInfo {
	
	public Flag flag;
	public long value;
	public String playHistory;
	public int depth;
	
	public TTInfo(Flag flag, long value, String playHistory, int depth) {
		this.flag = flag;
		this.value = value;
		this.playHistory = playHistory;
		this.depth = depth;
	}
	
	public TTInfo(Flag flag, String[] info, int depth) {
		this.flag = flag;
		this.value = Long.valueOf(info[0]);
		this.playHistory = info[1];
		this.depth = depth;
	}

	public String[] getInfo(){
		return new String[]{String.valueOf(value), playHistory};
	}
	
}

package com.luv2code.springdemo.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class DataOrderInterval {
	
	private ZonedDateTime StartData;
	
	private ZonedDateTime EndData;

	public ZonedDateTime getStartData() {
		return StartData;
	}

	public void setStartData(ZonedDateTime startData) {
		StartData = startData;
	}

	public ZonedDateTime getEndData() {
		return EndData;
	}

	public void setEndData(ZonedDateTime endData) {
		EndData = endData;
	}
	
	public void editInterval() {
		
		String start = "1970-01-01 00:00:00.0";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		String end = "2038-01-01 00:00:00.0";
		ZoneId zoneId = ZoneId.of("Europe/Minsk");
		if(StartData==null) {
			StartData=ZonedDateTime.parse(start, formatter.withZone(zoneId));;
		}
		if(EndData==null) {
			EndData=ZonedDateTime.parse(end, formatter.withZone(zoneId));;
		}
		
	}
	
	public DataOrderInterval() {
		
	}
	
	public DataOrderInterval(DataOrderInterval current) {
		 StartData=current.getStartData();
		 EndData=current.getEndData();
	}

}

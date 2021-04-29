package com.luv2code.springdemo.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.luv2code.springdemo.service.LocalDateTimeDeserializer;
import com.luv2code.springdemo.service.LocalDateTimeSerializer;


public class DataOrderInterval {
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private ZonedDateTime startData;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private ZonedDateTime endData;

	public ZonedDateTime getStartData() {
		return startData;
	}

	public void setStartData(ZonedDateTime startData) {
		this.startData = startData;
	}

	public ZonedDateTime getEndData() {
		return endData;
	}

	public void setEndData(ZonedDateTime endData) {
		this.endData = endData;
	}
	
	public void editInterval() {
		
		String start = "1970-01-01 00:00:00.0";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		String end = "2038-01-01 00:00:00.0";
		ZoneId zoneId = ZoneId.of("Europe/Minsk");
		if(startData==null) {
			startData=ZonedDateTime.parse(start, formatter.withZone(zoneId));;
		}
		if(endData==null) {
			endData=ZonedDateTime.parse(end, formatter.withZone(zoneId));;
		}
		
	}
	
	public DataOrderInterval() {
		
	}
	
	public DataOrderInterval(DataOrderInterval current) {
		 startData=current.getStartData();
		 endData=current.getEndData();
	}
	
	@Override
	public String toString() {
		return "DataOrderInterval [StartData=" + startData + ", EndData=" + endData + "]";
	}

}

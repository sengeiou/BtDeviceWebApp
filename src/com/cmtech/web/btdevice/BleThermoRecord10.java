package com.cmtech.web.btdevice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class BleThermoRecord10 extends BasicRecord {
	private static final String[] PROPERTIES = {"temp"};
	private String temp;
    
    public BleThermoRecord10(long createTime, String devAddress) {
    	super(RecordType.THERMO, createTime, devAddress);
    }
    
	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	@Override
    public String[] getProperties() {
    	return PROPERTIES;
    }

    @Override
	public void fromJson(JSONObject json) {
		super.fromJson(json);		
		temp = json.getString("temp");
	}	

	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		json.put("temp", temp);	
		return json;
	}
	
	@Override
	public void readPropertiesFromResultSet(ResultSet rs) throws SQLException {
		super.readPropertiesFromResultSet(rs);
		temp = rs.getString("temp");
	}
	
	@Override
	public int writePropertiesToPreparedStatement(PreparedStatement ps) throws SQLException {
		int begin = super.writePropertiesToPreparedStatement(ps);
		ps.setString(begin++, temp);
		return begin;
	}
}

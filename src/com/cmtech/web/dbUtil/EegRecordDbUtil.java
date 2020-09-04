package com.cmtech.web.dbUtil;

import static com.cmtech.web.dbUtil.DbUtil.INVALID_ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.cmtech.web.btdevice.BleEegRecord10;
import com.cmtech.web.btdevice.RecordType;

public class EegRecordDbUtil {
	
	public static boolean upload(JSONObject json) {
		BleEegRecord10 record = BleEegRecord10.createFromJson(json);
		if(record == null) return false;
		
		int id = RecordDbUtil.query(RecordType.EEG, record.getCreateTime(), record.getDevAddress());
		if(id != INVALID_ID) return false;
		
		Connection conn = DbUtil.connect();
		if(conn == null) return false;
		
		PreparedStatement ps = null;
		String sql = "insert into eegrecord (ver, createTime, devAddress, creatorPlat, creatorId, note, sampleRate, caliValue, leadTypeCode, recordSecond, eegData) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, record.getVer());
			ps.setLong(2, record.getCreateTime());
			ps.setString(3, record.getDevAddress());
			ps.setString(4, record.getCreatorPlat());
			ps.setString(5, record.getCreatorPlatId());
			ps.setString(6, record.getNote());
			ps.setInt(7, record.getSampleRate());
			ps.setInt(8, record.getCaliValue());
			ps.setInt(9, record.getLeadTypeCode());
			ps.setInt(10, record.getRecordSecond());
			ps.setString(11, record.getEegData());
			
			boolean rlt = ps.execute();
			if(!rlt && ps.getUpdateCount() == 1)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(null, ps, conn);
		}
		return false;
	}
	
	public static JSONObject downloadBasicInfo(int id) {
		Connection conn = DbUtil.connect();		
		if(conn == null) return null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select ver, creatorPlat, creatorId, createTime, devAddress, recordSecond, note from eegrecord where id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				String ver = rs.getString("ver");
				String creatorPlat = rs.getString("creatorPlat");
				String creatorId = rs.getString("creatorId");
				long createTime = rs.getLong("createTime");
				String devAddress = rs.getString("devAddress");
				int recordSecond = rs.getInt("recordSecond");
				String note = rs.getString("note");
				JSONObject json = new JSONObject();
				json.put("recordTypeCode", RecordType.EEG.getCode());
				json.put("ver", ver);
				json.put("creatorPlat", creatorPlat);
				json.put("creatorId", creatorId);
				json.put("createTime", createTime);
				json.put("devAddress", devAddress);
				json.put("recordSecond", recordSecond);
				json.put("note", note);
			
				return json;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs, ps, conn);
		}
		return null;
	}
	
	public static JSONObject download(int id) {
		Connection conn = DbUtil.connect();		
		if(conn == null) return null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select createTime, devAddress, creatorPlat, creatorId, note, sampleRate, caliValue, leadTypeCode, recordSecond, eegData from eegrecord where id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				long createTime = rs.getLong("createTime");
				String devAddress = rs.getString("devAddress");
				String creatorPlat = rs.getString("creatorPlat");
				String creatorId = rs.getString("creatorId");
				String note = rs.getString("note");
				int sampleRate = rs.getInt("sampleRate");
				int caliValue = rs.getInt("caliValue");
				int leadTypeCode = rs.getInt("leadTypeCode");
				int recordSecond = rs.getInt("recordSecond");
				String eegData = rs.getString("eegData");
				JSONObject json = new JSONObject();
				json.put("recordTypeCode", RecordType.EEG.getCode());
				json.put("createTime", createTime);
				json.put("devAddress", devAddress);
				json.put("creatorPlat", creatorPlat);
				json.put("creatorId", creatorId);
				json.put("note", note);
				json.put("sampleRate", sampleRate);
				json.put("caliValue", caliValue);
				json.put("leadTypeCode", leadTypeCode);
				json.put("recordSecond", recordSecond);
				json.put("eegData", eegData);
			
				return json;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.close(rs, ps, conn);
		}
		return null;
	}
}

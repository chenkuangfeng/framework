package com.ubsoft.framework.core.dal.util;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.ubsoft.framework.core.dal.model.Bio;


public class BioRowMapper implements RowMapper {

	private String bioName;
	public BioRowMapper(){}
	public BioRowMapper(String bioName){
		this.bioName=bioName;
	}
	LobHandler lobHandler = new DefaultLobHandler();
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Bio bio = new Bio(bioName);
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			if(rs.getObject(column) instanceof Clob){
				bio.setObject(column,lobHandler.getClobAsString(rs, column)); 
			} else if(rs.getObject(column) instanceof Blob){
				lobHandler.getBlobAsBytes(rs, column);
			}else{
				bio.setObject(column, rs.getObject(column));
			}
		}
		return bio;
	}

	
	

	

	
}

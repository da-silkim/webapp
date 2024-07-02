package com.bluenetworks.webapp.app.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileInfoRegMapper {
    
	String selectAtchFileId();
	
	List<Map<String, Object>> selectAtchFileList(Map<String, Object> param);

    Map<String, Object> selectAtchFileOne(Map<String, Object> param);

    int insertFileUpload(Map<String, Object> param);

    int deleteFileUpload(Map<String, Object> param);

    int deleteFileTinfModel(Map<String, Object> param);

    int deleteFileTcspCustomer(Map<String, Object> param);

    int deleteFileTinfCs(Map<String, Object> param);
}

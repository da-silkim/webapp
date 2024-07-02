package com.bluenetworks.webapp.app.main.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int rownum;			 // rownum
	private long id;             // 아이디
	private String subject;      // 제목
	private String content;      // 내용
	private long atchFileId;		 // 파일
	private long views;           // 조회 수
	private long regId;        // 등록자 아이디
	private Date regDate;     // 등록일자
	private String name;
	
	private String searchWord;
	private String sysSelect;
	private String fmtRegDate; // 등록일자 포맷
	
	private long prevId;
	private String prevTitle;
	private long nextId;
	private String nextTitle;
	private int serviceCompanyId;
	
}

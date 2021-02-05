package com.ssafy.mom.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Builder
@Data
public class SendUserInfo {


	private int uid;
	private String username;	
	private String email;	
	private String stateMsg;
	
	@Builder
	public SendUserInfo(int uid, String username, String email, String stateMsg) {
		super();
		this.uid = uid;
		this.username = username;
		this.email = email;
		this.stateMsg = stateMsg;
	}
	
	



}

package com.zju.pojo;

import java.util.Date;

public class Comment {
	private int id;
	private String content;
	private int userId;
	private int entityId;
	private int entityType;
	private Date createdDate;
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getEntiyId() {
		return entityId;
	}
	public void setEntiyId(int entiyId) {
		this.entityId = entiyId;
	}
	public int getEntiyType() {
		return entityType;
	}
	public void setEntiyType(int entiyType) {
		this.entityType = entiyType;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", userId=" + userId + ", entiyId=" + entityId
				+ ", entiyType=" + entityType + ", createDate=" + createdDate + ", status=" + status + "]";
	}
	
	
}

package com.zju.async;

import java.util.HashMap;
import java.util.Map;

//类似于项目中的pojo
public class EventModel {
	//事件类型
	private EventType type;
	//事件触发者
	private int actorId;
	//事件触发对象
	private int entityType;
	private int entityId;
	//事件拥有这
	private int entityOwnerId;
	//保存现场细节信息用的
	private Map<String,String> exts = new HashMap<>();
	//空构造器
	public EventModel() {
		
	}
	
	//空构造器
	public EventModel(EventType type) {
		this.type = type;
	}
	
	
	public EventType getType() {
		return type;
	}
	public EventModel setType(EventType type) {
		this.type = type;
		return this;
	}
	public int getActorId() {
		return actorId;
	}
	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;
	}
	public int getEntityType() {
		return entityType;
	}
	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}
	public int getEntityId() {
		return entityId;
	}
	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}
	public int getEntityOwnerId() {
		return entityOwnerId;
	}
	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;
	}
	public String getExt(String name) {
		return exts.get(name);
	}
	//像map中写入
	public EventModel setExt(String name,String value) {
		exts.put(name, value);
		return this;
	}

	public Map<String, String> getExts() {
		return exts;
	}

	public void setExts(Map<String, String> exts) {
		this.exts = exts;
	}

	@Override
	public String toString() {
		return "EventModel [type=" + type + ", actorId=" + actorId + ", entityType=" + entityType + ", entityId="
				+ entityId + ", entityOwnerId=" + entityOwnerId + ", exts=" + exts + "]";
	}
	
	
}

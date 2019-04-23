package com.zju.async;

import java.util.HashMap;
import java.util.Map;

//��������Ŀ�е�pojo
public class EventModel {
	//�¼�����
	private EventType type;
	//�¼�������
	private int actorId;
	//�¼���������
	private int entityType;
	private int entityId;
	//�¼�ӵ����
	private int entityOwnerId;
	//�����ֳ�ϸ����Ϣ�õ�
	private Map<String,String> exts = new HashMap<>();
	//�չ�����
	public EventModel() {
		
	}
	
	//�չ�����
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
	//��map��д��
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

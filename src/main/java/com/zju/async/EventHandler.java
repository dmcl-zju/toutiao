package com.zju.async;

import java.util.List;

public interface EventHandler {
	//ִ����Ӧ�Ĳ������ö�̬
	void doHandler(EventModel model);
	//ÿ��handler��Ҫ������¼�����
	List<EventType> getSupportEnventTypes();
	
}

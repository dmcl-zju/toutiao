package com.zju.async;

import java.util.List;

public interface EventHandler {
	//执行相应的操作利用多态
	void doHandler(EventModel model);
	//每个handler需要处理的事件类型
	List<EventType> getSupportEnventTypes();
	
}

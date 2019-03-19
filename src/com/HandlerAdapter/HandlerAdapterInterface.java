package com.HandlerAdapter;

import com.HandlerMapping.HandlerMappingInterface;

import java.lang.reflect.Method;
import java.util.Map;

public interface HandlerAdapterInterface {


	//-------------------------------------------------------------------------function

	void handlerAdapter() throws Exception;

	boolean getHandlerAdapter(HandlerMappingInterface handlerMapping);

	Map<String,Method> getMethodMap();

	Map<String,Object> getInstanceMap();
}

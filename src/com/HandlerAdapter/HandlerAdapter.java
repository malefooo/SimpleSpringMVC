package com.HandlerAdapter;

import Project.Controller.TestController;
import Project.Service.TestService;
import com.Annotation.AutoWrite;
import com.Annotation.Controller;
import com.Annotation.RequestMapping;
import com.Annotation.Service;
import com.HandlerMapping.HandlerMapping;
import com.HandlerMapping.HandlerMappingInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerAdapter implements HandlerAdapterInterface {


	private HandlerMapping handlerMapping;

	private Map<String,Object> instanceMap;

	private Map<String,Method> methodChainMap;


	//-------------------------------------------------------------------------------Constructor
	public HandlerAdapter(){

	}



	//--------------------------------------------------------------------------------Function


	@Override
	public void handlerAdapter() throws Exception {
		try {
			IOC();
			getMethodChain();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public boolean getHandlerAdapter(HandlerMappingInterface handlerMapping) {
		if(handlerMapping instanceof HandlerMapping) {
			this.handlerMapping = (HandlerMapping) handlerMapping;
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Method> getMethodMap() {
		return methodChainMap;
	}

	@Override
	public Map<String, Object> getInstanceMap() {
		return instanceMap;
	}

	//先判断类中是否含有controller和service，有的话就put到map中
	//然后再实现注入，判断Controller的字段是否含有autowrite，含有的话就把实例注入进去
	public void IOC() throws Exception {

		if(handlerMapping == null)
			return;

		instanceMap = new HashMap<>();

		for(String str : handlerMapping.getBeansList()){
			Class clazz = Class.forName(str);


			if(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)){
				instanceMap.put(clazz.getName(),clazz.newInstance());
			}

		}

		//注入
		for(Map.Entry<String,Object> entry : instanceMap.entrySet()){
			if(entry.getKey().equals("Project.Controller.TestController")){

				Field[] fields = ((TestController)entry.getValue()).getClass().getDeclaredFields();

				for(Field field : fields){
					if(field.isAnnotationPresent(AutoWrite.class)){
						field.setAccessible(true);
						field.set(entry.getValue(),instanceMap.get("Project.Service.ServiceImpl.TestServiceImpl"));
					}
				}
			}
		}



	}

	//然后这一步获取方法链
	public void getMethodChain(){

		methodChainMap = new HashMap<>();

		for(Map.Entry<String,Object> entry : instanceMap.entrySet()){

			if(entry.getKey().equals("Project.Controller.TestController")){

				Method[] methods = entry.getValue().getClass().getMethods();

				for(Method method : methods){

					if(method.isAnnotationPresent(RequestMapping.class)){
						String methodURI = entry.getKey() + ((RequestMapping)method.getAnnotation(RequestMapping.class)).value();
						String[] strings = methodURI.split("\\.");
						methodChainMap.put(strings[2], method);
					}

				}
			}
		}
	}

	//-------------------------------------------------------------------------------------------------------------get/set


}

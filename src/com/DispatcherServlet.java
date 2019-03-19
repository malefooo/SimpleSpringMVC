package com;


import Project.Controller.TestController;
import com.HandlerAdapter.HandlerAdapter;
import com.HandlerAdapter.HandlerAdapterInterface;
import com.HandlerMapping.HandlerMapping;
import com.HandlerMapping.HandlerMappingInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

	private List<HandlerAdapterInterface> adapterList;

	private Map<String,Method> methodMap;

	private Map<String,Object> instanceMap;

	@Override
	public void init() throws ServletException {
		//初始化容器，用来存储适配器，当有一个处理器，就用来对比是哪个适配器合适
		initAdapterList();
		//初始化HandlerMapping，这里处理的是对应的注解，因为我们使用的是注解的方式，所以HandlerMapping就是只有一种
		HandlerMapping handlerMapping = new HandlerMapping();
		//初始化HandlerAdapter，这里的适配器要传入一个处理器，然后根据处理器里边的关于有注解的类进行解析，提取出方法链，用来存储处理方法
		HandlerAdapterInterface handlerAdapter = getAdapter(handlerMapping);

		if(handlerAdapter == null)
			return;

		try {
			handlerAdapter.handlerAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}

		methodMap = handlerAdapter.getMethodMap();

		instanceMap = handlerAdapter.getInstanceMap();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] uris = req.getRequestURI().split("/");
		String uri = uris[2] + "/" + uris[3].replace(".do","");
		for(Map.Entry<String,Method> entry : methodMap.entrySet()){
			if(entry.getKey().endsWith(uri)){
				try {
					TestController object = (TestController)instanceMap.get("Project.Controller.TestController");
					entry.getValue().invoke(object,new Object[]{req,resp});
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public HandlerAdapterInterface getAdapter(HandlerMappingInterface handlerMapping){
		for(HandlerAdapterInterface adapter : adapterList){
			if(adapter.getHandlerAdapter(handlerMapping))
				return adapter;
		}
		return null;
	}

	public void initAdapterList(){
		adapterList = new ArrayList<>();
		adapterList.add(new HandlerAdapter());
	}


}

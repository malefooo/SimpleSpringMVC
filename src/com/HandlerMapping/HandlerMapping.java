package com.HandlerMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HandlerMapping implements HandlerMappingInterface{


	//扫描包后存放的包名list
	private List<String> beansList;
	//项目路径
	private String filePath = "E:\\ideaworkspace\\SimpleSpringMVC\\src\\Project";



	//--------------------------------------------------------------------------------Constructor


	public HandlerMapping(){
		init();
	}



	//--------------------------------------------------------------------------------Function


	public void init(){
		beansList = new ArrayList<>();
		//加载项目里边有注解的类
		File file = new File(filePath);
		loadClass(file);
	}

	public void loadClass(File file){
		if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i=0;i<fileList.length;i++){
				File temp = file;
				File newFile = new File(temp + File.separator + fileList[i]);
				loadClass(newFile);
			}
		}else {
//			System.out.println(this.getClass().getName());
			String[] strings = file.getPath().split("\\\\");
			String str = "";
			int index = 0;
			for(int i=0;i<strings.length;i++){
				if(strings[i].equals("Project")){
					str += strings[i];
					index = i;
				}
			}

			for(int i = index+1;i<strings.length;i++){
				str += "." + strings[i];
			}
//			str += "." + strings[index+1] + "." + strings[index+2].replace(".java","");
			beansList.add(str.replace(".java",""));
		}
	}

	//------------------------------------------------------------------------------------------------------get/set

	public List<String> getBeansList() {
		return beansList;
	}

	public void setBeansList(List<String> beansList) {
		this.beansList = beansList;
	}

}

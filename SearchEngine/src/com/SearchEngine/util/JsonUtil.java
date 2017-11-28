package com.SearchEngine.util;

import java.util.List;

import com.SearchEngine.model.News;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class JsonUtil {

	public static JSONArray formatRsToJsonArray(List<News> result)throws Exception{
		JSONArray array = null;
		if(result!=null){
			int num=result.size();
			array = new JSONArray();
			for(int i=0;i<num;i++){
				JSONObject jsonObj=new JSONObject();
				News news = result.get(i);
				String URL = "url";
				String Title = "title";
				String Hightlight = "contents";
				String Add_time = "add_time";
				jsonObj.put(URL, news.getUrl());
				jsonObj.put(Title, news.getTitle());
				jsonObj.put(Hightlight, news.getContents());
				jsonObj.put(Add_time, news.getAdd_time());
				array.add(jsonObj);
			}
		}
		return array;
	}
}

package com.SearchEngine.model;

import com.SearchEngine.util.DateUtil;

public class News {
	private String id;
    private String type;
    private String title;
    private String keyword;
    private String url;
    private String contents;
	private String comment_url;
	private String comment_count;
    private String add_time;
    private String hot=null;
    public String getHot() {
    	if(this.hot==null){
    		double count = Double.valueOf(comment_count);
    	    double delt = DateUtil.getTimeDelta(add_time, "2005-12-08 07:46:43");
    	    double hot = (Math.log(count) / Math.log(10))+delt/45000;
		    return Double.toString(hot);
    	}else{
    		return this.hot;
    	}
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return keyword;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getComment_url() {
		return comment_url;
	}
	public void setComment_url(String comment_url) {
		this.comment_url = comment_url;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

}

package com.SearchEngine.model;

import java.util.List;

public class Result {
	private int pageCount;//总页数
	private int page=1;//当前页，默认从第一页开始
	private int size=10;//一页显示的新闻数量
	private int start;//开始新闻编号
	private int end;//结束新闻编号
	private double time;
	private List<News> result;
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public List<News> getResult() {
		return result;
	}
	public void setResult(List<News> result) {
		this.result = result;
	}
	

}

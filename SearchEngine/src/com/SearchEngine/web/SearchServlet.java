package com.SearchEngine.web;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import com.SearchEngine.dao.NewsDao;
import com.SearchEngine.model.PageBean;
import com.SearchEngine.model.Result;
import com.SearchEngine.util.JsonUtil;
import com.SearchEngine.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse r¡¤esponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String page=request.getParameter("page");
		String size=request.getParameter("size");
		String sort=request.getParameter("sort");
		String query=request.getParameter("query");
		PageBean pageBean = new PageBean();
		Result rs = new Result();
		NewsDao newsDao = new NewsDao();
		newsDao.createrDirectory();
		newsDao.creatReader();
		if(page!=null){
			pageBean.setPage(Integer.valueOf(page));
		}
		if(size!=null){
			pageBean.setSize(Integer.valueOf(size));
		}
		if(sort==null){
			try {
				rs = newsDao.search(query, pageBean);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(sort.equals("time")){
			try {
				rs = newsDao.searchByTime(query, pageBean);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(sort.equals("hot")){
			try {
				rs = newsDao.searchByHotDegree(query, pageBean);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONObject result=new JSONObject();
		JSONArray jsonArray=null;
		try {
			jsonArray = JsonUtil.formatRsToJsonArray(rs.getResult());
			result.put("result", jsonArray);
		    result.put("time", rs.getTime());
		    result.put("pageCount", rs.getPageCount());
		    ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newsDao.closeReader();
	}

}

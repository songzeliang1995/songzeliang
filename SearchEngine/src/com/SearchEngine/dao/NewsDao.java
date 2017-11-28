package com.SearchEngine.dao;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.SearchEngine.model.News;
import com.SearchEngine.model.PageBean;
import com.SearchEngine.model.Result;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import net.paoding.analysis.analyzer.PaodingAnalyzer;

class timeComparator implements Comparator<News>{

    @Override
    public int compare(News news1, News news2) {
        // TODO Auto-generated method stub
        String time1 = news1.getAdd_time();
        String time2 = news2.getAdd_time();
        return time2.compareTo(time1);
    }
}

class hotComparator implements Comparator<News>{

    @Override
    public int compare(News news1, News news2) {
        // TODO Auto-generated method stub
        Double hot1 = Double.valueOf(news1.getHot());
        Double hot2 = Double.valueOf(news2.getHot());
        return hot2.compareTo(hot1);
    }
}

public class NewsDao {
	private static final String GENERAL = "general";
	public static final String FIELD_ALL = "all";
	public static final String FIELD_ID = "id";
	public static final String FIELD_TYPE = "type";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_KEYWORD = "keyword";
    public static final String FIELD_URL = "url";
    public static final String FIELD_COMMENT = "comment_url";
    public static final String FIELD_COMMENT_COUNT = "comment_count";
    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_ADD_TIME = "add_time";
    public static final String FIELD_HOT = "hot";
    public static final String INDEX_DIR = "D:\\Document\\Workspace_JEE\\SearchEngine\\src\\index_news";  
    private Directory dir = null;
    private Analyzer analyzer = null;
    private IndexWriterConfig iwc = null;
    private IndexWriter writer =null;
	private IndexReader reader = null;
	private IndexSearcher searcher = null; 
	private QueryParser parser = null;
	private Query query = null;
    public void createrDirectory() throws IOException{ 
    	dir = FSDirectory.open(Paths.get(INDEX_DIR));
    }
    public void creatWriter() throws IOException {
		analyzer = new IKAnalyzer(true);
	    iwc = new IndexWriterConfig(analyzer);
	    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	    writer = new IndexWriter(dir, iwc);
	}
    public void closeWriter() throws IOException{
    	writer.close();
    }
    public void creatReader() throws IOException{
    	analyzer = new IKAnalyzer(true);
    	reader = DirectoryReader.open(dir);
    	searcher = new IndexSearcher(reader);
    	String[] fields = { FIELD_TITLE, FIELD_CONTENTS }; 
    	parser = new MultiFieldQueryParser(fields, analyzer);
    }
    public void closeReader() throws IOException{
    	reader.close();
    }
	private Document toDocument(News news){
		Document doc = new Document();
		doc.add(new StringField(FIELD_HOT,news.getHot(),Field.Store.YES));
		doc.add(new StringField(FIELD_ID,news.getId(),Field.Store.YES));
		doc.add(new StringField(FIELD_ALL,GENERAL,Field.Store.NO));
		doc.add(new StringField(FIELD_TYPE,news.getType(),Field.Store.YES));
		doc.add(new TextField(FIELD_TITLE,news.getTitle(),Field.Store.YES));
		doc.add(new TextField(FIELD_KEYWORD,news.getKeyword(),Field.Store.YES));
		doc.add(new StringField(FIELD_URL,news.getUrl(),Field.Store.YES));
		doc.add(new StringField(FIELD_COMMENT,news.getComment_url(),Field.Store.YES));
		doc.add(new StringField(FIELD_COMMENT_COUNT,news.getComment_count(),Field.Store.NO));
		doc.add(new TextField(FIELD_CONTENTS,news.getContents(),Field.Store.YES));
		doc.add(new StringField(FIELD_ADD_TIME,news.getAdd_time(),Field.Store.YES));
		return doc;
    }
    public void addNews(News news) throws IOException{
    	news.setHot(news.getHot());
    	Document doc = this.toDocument(news);
    	writer.addDocument(doc);
    }
//    public void deleteNews(){
//    	
//    }
    public void updateNews(News news) throws IOException{
    	news.setHot(news.getHot());
    	Document doc = this.toDocument(news);
    	writer.updateDocument(new Term(FIELD_COMMENT,news.getComment_url()),doc);
    }
    public Result search(String keyword, PageBean pageBean) throws ParseException, IOException, InvalidTokenOffsetsException{
    	double begin = System.currentTimeMillis();
    	query = parser.parse(keyword);
    	Result rs = new Result();
    	List<News> ans = new ArrayList<News>();
    	TopDocs ts = searcher.search(query, 100);
    	int nums = (int) ts.totalHits;
    	ScoreDoc[] hits = ts.scoreDocs;
    	int pageSize = pageBean.getSize();
    	int pageNo = pageBean.getPage();
    	int pageCount = (nums + pageSize - 1) / pageSize;
    	rs.setPageCount(pageCount);
    	int start = 0;
    	int end = 0;
    	if (pageCount > 0) {  
            start = (pageNo - 1) * pageSize;  
            end = start + pageSize;  
            if (pageNo == pageCount) { 
                end = start + (nums % pageSize);  
            }  
        }
    	if (start < end) {  
            rs.setStart(start+1);  
            rs.setEnd(end);  
        }
    	for (int i = start; i < end; i++) {
    		News news = new News();
    		Document doc = searcher.doc(hits[i].doc);
    		news.setUrl(doc.get(FIELD_URL));
    		news.setAdd_time(doc.get(FIELD_ADD_TIME));
    		news.setComment_count(doc.get(FIELD_COMMENT_COUNT));
    		news.setTitle(doc.get(FIELD_TITLE));
    		String hot = news.getHot();
    		news.setHot(hot);
            String text = doc.get(FIELD_CONTENTS);  
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            String highLightText = null;
            TokenStream tokenStream = analyzer.tokenStream(FIELD_CONTENTS,new StringReader(text));  
            highLightText = highlighter.getBestFragment(tokenStream,text);    
            if(highLightText == null){
            	if(text.length()>100)
            		 news.setContents(text.substring(0, 100) + "..."); 
            }else{
            	news.setContents(highLightText);
            }
            ans.add(news);
        }  
    	rs.setTime((System.currentTimeMillis() - begin) / 1000.0);
    	rs.setResult(ans);
    	return rs;
    }
    
    public Result searchByTime(String keyword, PageBean pageBean) throws ParseException, IOException, InvalidTokenOffsetsException{
    	double begin = System.currentTimeMillis();
    	query = parser.parse(keyword);
    	Result rs = new Result();
    	List<News> ans = new ArrayList<News>();
    	TopDocs ts = searcher.search(query, 100);
    	int nums = (int) ts.totalHits;
    	ScoreDoc[] hits = ts.scoreDocs;
    	int pageSize = pageBean.getSize();
    	int pageNo = pageBean.getPage();
    	int pageCount = (nums + pageSize - 1) / pageSize;
    	rs.setPageCount(pageCount);
    	int start = 0;
    	int end = 0;
    	if (pageCount > 0) {  
            start = (pageNo - 1) * pageSize;  
            end = start + pageSize;  
            if (pageNo == pageCount) { 
                end = start + (nums % pageSize);  
            }  
        }
    	if (start < end) {  
            rs.setStart(start+1);  
            rs.setEnd(end);  
        }
    	for (int i = 0; i < nums; i++) {
    		News news = new News();
    		Document doc = searcher.doc(hits[i].doc);
    		news.setUrl(doc.get(FIELD_URL));
    		news.setAdd_time(doc.get(FIELD_ADD_TIME));
    		news.setComment_count(doc.get(FIELD_COMMENT_COUNT));
    		news.setTitle(doc.get(FIELD_TITLE));
    		String hot = news.getHot();
    		news.setHot(hot);
            String text = doc.get(FIELD_CONTENTS);  
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            String highLightText = null;
            TokenStream tokenStream = analyzer.tokenStream(FIELD_CONTENTS,new StringReader(text));  
            highLightText = highlighter.getBestFragment(tokenStream,text);    
            if(highLightText == null){
            	if(text.length()>100)
            		 news.setContents(text.substring(0, 100) + "..."); 
            }else{
            	news.setContents(highLightText);
            }
            ans.add(news);
        }
    	timeComparator comparator = new timeComparator();
        Collections.sort(ans, comparator);
        List<News> ans2 = new ArrayList<News>();
    	for (int i = start; i < end; i++) {
    		ans2.add(ans.get(i));
    	}
    	rs.setTime((System.currentTimeMillis() - begin) / 1000.0);
    	rs.setResult(ans2);
    	return rs;
    }
    public Result searchByHotDegree(String keyword, PageBean pageBean) throws IOException, ParseException, InvalidTokenOffsetsException{
    	double begin = System.currentTimeMillis();
    	query = parser.parse(keyword);
    	Result rs = new Result();
    	List<News> ans = new ArrayList<News>();
    	TopDocs ts = searcher.search(query, 100);
    	int nums = (int) ts.totalHits;
    	ScoreDoc[] hits = ts.scoreDocs;
    	int pageSize = pageBean.getSize();
    	int pageNo = pageBean.getPage();
    	int pageCount = (nums + pageSize - 1) / pageSize;
    	rs.setPageCount(pageCount);
    	int start = 0;
    	int end = 0;
    	if (pageCount > 0) {  
            start = (pageNo - 1) * pageSize;  
            end = start + pageSize;  
            if (pageNo == pageCount) { 
                end = start + (nums % pageSize);  
            }  
        }
    	if (start < end) {  
            rs.setStart(start+1);  
            rs.setEnd(end);  
        }
    	for (int i = 0; i < nums; i++) {
    		News news = new News();
    		Document doc = searcher.doc(hits[i].doc);
    		news.setUrl(doc.get(FIELD_URL));
    		news.setAdd_time(doc.get(FIELD_ADD_TIME));
    		news.setComment_count(doc.get(FIELD_COMMENT_COUNT));
    		news.setTitle(doc.get(FIELD_TITLE));
    		news.setHot(doc.get(FIELD_HOT));
            String text = doc.get(FIELD_CONTENTS);  
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");  
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            String highLightText = null;
            TokenStream tokenStream = analyzer.tokenStream(FIELD_CONTENTS,new StringReader(text));  
            highLightText = highlighter.getBestFragment(tokenStream,text);    
            if(highLightText == null){
            	if(text.length()>100)
            		 news.setContents(text.substring(0, 100) + "..."); 
            }else{
            	news.setContents(highLightText);
            }
            ans.add(news);
        }
    	hotComparator comparator = new hotComparator();
        Collections.sort(ans, comparator);
        List<News> ans2 = new ArrayList<News>();
    	for (int i = start; i < end; i++) {
    		ans2.add(ans.get(i));
    	}
    	rs.setTime((System.currentTimeMillis() - begin) / 1000.0);
    	rs.setResult(ans2);
    	return rs;
    }
   
}

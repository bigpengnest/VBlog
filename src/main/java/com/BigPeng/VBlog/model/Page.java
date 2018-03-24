package com.BigPeng.VBlog.model;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int pageSize=10;            //页大小
    private int pageIndex=0;           //当前页号
    private int totalPageCount=0;      //总页数
    private int record=0;              //记录总数
    private Integer nextPage;          //下一页
    private Integer prePage;           //上一页
    private List<Blog> blogList=new ArrayList<Blog>();     //BLOG信息的集合

    //得到开始记录数
    public int getSartRow(){
        return (pageIndex-1)*pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        setNextPage();
        setPrePage();
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount() {
        int totalP = record % getPageSize() == 0 ? record / getPageSize() :
                record/ getPageSize() + 1;
        this.totalPageCount = totalP;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
        setTotalPageCount();
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage() {
        this.nextPage = this.pageIndex+1;

    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage() {
        this.prePage = this.pageIndex-1;
        if (this.prePage<1){
            this.prePage = 1;
        }
    }

    public List<Blog> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<Blog> blogList) {
        this.blogList = blogList;
    }
}

package com.viewol.category.vo;

import java.util.Date;

public class CategoryVO {
    private int id;
    private int parentId;
    private int type;//1 展商分类  2 产品分类
    private String logo;//分类图片
    private String name;//分类名称
    private Date createTime;
}

package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.vo;

/**
 * @author by laiyuan
 * @Date 2019/9/6 11:24
 * @Description: 项目视图对象
 * @Version 1.0
 */
public class ProjectVO {
    /**
     * 项目名
     */
    private String name;
    /**
     * 主持单位
     */
    private String unit;
    /**
     * 单位
     */
    private String member;
    /**
     * 时间
     */
    private String year;
    /**
     * 中文摘要
     */
    private String abstractCh;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAbstractCh() {
        return abstractCh;
    }

    public void setAbstractCh(String abstractCh) {
        this.abstractCh = abstractCh;
    }

    public ProjectVO(String name, String unit, String member, String year, String abstractCh) {
        this.name = name;
        this.unit = unit;
        this.member = member;
        this.year = year;
        this.abstractCh = abstractCh;
    }

    public ProjectVO() {
    }

    @Override
    public String toString() {
        return "ProjectVO{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", member='" + member + '\'' +
                ", year='" + year + '\'' +
                ", abstractCh='" + abstractCh + '\'' +
                '}';
    }
}

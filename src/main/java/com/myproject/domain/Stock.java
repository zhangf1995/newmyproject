package com.myproject.domain;

/**
 * t_stock
 */
public class Stock extends BaseDomain {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer goodId;

    /**
     * 
     */
    private Integer count;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return goodId 
     */
    public Integer getGoodId() {
        return goodId;
    }

    /**
     * 
     * @param goodId 
     */
    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    /**
     * 
     * @return count 
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @param count 
     */
    public void setCount(Integer count) {
        this.count = count;
    }
}
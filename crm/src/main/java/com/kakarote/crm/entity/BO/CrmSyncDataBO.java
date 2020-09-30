package com.kakarote.crm.entity.BO;

import java.util.List;


public class CrmSyncDataBO {
    private Integer marketingId;

    private List<Integer> rIds;

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
    }

    public List<Integer> getrIds() {
        return rIds;
    }

    public void setrIds(List<Integer> rIds) {
        this.rIds = rIds;
    }
}

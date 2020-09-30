package com.kakarote.oa.entity.VO;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.oa.entity.PO.OaEventNotice;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class QueryEventByIdVO {

    private Date repeatStartTime;
    private String typeName;
    private Integer endType;
    private String color;
    private String endTypeConfig;
    private String batchId;
    private String repeatTime;
    private String createUserName;
    private String title;
    private String ownerUserIds;
    private Date updateTime;
    private Date repeatEndTime;
    private Long createUserId;
    private Date createTime;
    private Integer typeId;
    private Date endTime;
    private Date startTime;
    private Integer eventId;
    private Object repeatRate;
    private Integer repetitionType;
    private List<OaEventNotice> noticeList;
    private List<SimpleUser> ownerUserList;
    private List<SimpleCrmEntity> businessList;
    private List<SimpleCrmEntity> contactsList;
    private List<SimpleCrmEntity> contractList;
    private List<SimpleCrmEntity> customerList;
}

package com.lhs.service;

import com.lhs.bean.DBPogo.StageResultData;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface ApiService {

    //读取T3材料关卡效率文件
    String readStageFileT3(String expCoefficient);

    //读取T2材料关卡效率文件
    String readStageFileT2(String expCoefficient);

    //读取搓玉结果文件
    String readStageFileOrundum();

    //读取已结束活动材料关卡效率文件
    String readStageClosedFile(String expCoefficient);

    //根据掉落物品类型和样本数查询关卡  按效率倒序 数据库查询用
    Page<StageResultData> findDataByTypeAndTimesAndEffOrderByEffDesc(String main, Integer times, Double efficiency, Integer pageNum, Integer pageSize);
    //根据材料名称查询关卡  按效率倒序 数据库查询用
    Page<StageResultData> findDataByMainOrderByExpectAsc(String main, Double expect, Integer times, Integer pageNum, Integer pageSize);

    //全部关卡效率
    List<StageResultData> findByMainNotNull();

    void addVisitsAndIp(HttpServletRequest request, String domainName);

    void addVisits(String domainName);

    List<Object> selectVisits(Date start, Date end);

    void itemCostPlan (HashMap<String,Integer> itemCost);

}

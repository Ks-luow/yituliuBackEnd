package com.lhs.service;

import com.lhs.bean.DBPogo.Stage;
import com.lhs.bean.vo.PenguinDataRequestVo;
import com.lhs.bean.pojo.StageInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StageService {


    //查找全部关卡信息
	List<Stage> findAll();
    //查找全部关卡信息，计算用，设置了几个值
    List<StageInfoVo> findAllVo();
    //导入关卡信息excel
    void importStageData(MultipartFile file);
    //导出关卡信息excel
    void exportStageData(HttpServletResponse response);
    //更新关卡信息
    void updateStageInfo(Integer isShow,String stageId);

    List<PenguinDataRequestVo> penguinDataMerge(List<PenguinDataRequestVo> penguinDataList);

}

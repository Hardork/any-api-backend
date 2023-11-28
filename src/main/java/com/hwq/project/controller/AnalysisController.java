package com.hwq.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hwq.goatapicommon.model.entity.InterfaceInfo;
import com.hwq.goatapicommon.model.entity.UserInterfaceInfo;
import com.hwq.project.annotation.AuthCheck;
import com.hwq.project.common.BaseResponse;
import com.hwq.project.common.ErrorCode;
import com.hwq.project.common.ResultUtils;
import com.hwq.project.exception.BusinessException;
import com.hwq.project.mapper.InterfaceInfoMapper;
import com.hwq.project.mapper.UserInterfaceInfoMapper;
import com.hwq.project.model.vo.InterfaceInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:HWQ
 * @DateTime:2023/7/28 15:56
 * @Description:
 **/
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @AuthCheck(mustRole = "Admin")
    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterface() {
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterface(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoMap = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.in("id", interfaceInfoMap.keySet());
        List<InterfaceInfo> list = interfaceInfoMapper.selectList(interfaceInfoQueryWrapper);
        if (list.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<InterfaceInfoVO> res = list.stream().map(userInterfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(userInterfaceInfo, interfaceInfoVO);
            Integer totalNum = interfaceInfoMap.get(userInterfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(res);
    }
}

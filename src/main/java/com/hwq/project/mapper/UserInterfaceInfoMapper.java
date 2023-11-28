package com.hwq.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwq.goatapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author HWQ
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-07-14 17:27:59
* @Entity com.hwq.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterface(int limit);
}





package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.response.SystemDictionaryResponseDTO;
import com.mediguard.central.entity.DictionaryEntity;

/**
 * 系统服务接口
 */
public interface SystemService extends IService<DictionaryEntity> {

    /**
     * 获取系统字典
     * 
     * @return 字典数据
     */
    SystemDictionaryResponseDTO getDictionaries();
}

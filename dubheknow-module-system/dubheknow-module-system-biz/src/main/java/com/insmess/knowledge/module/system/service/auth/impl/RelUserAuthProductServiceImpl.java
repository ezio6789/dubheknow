package com.insmess.knowledge.module.system.service.auth.impl;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.insmess.knowledge.module.system.dal.dataobject.auth.RelUserAuthProductDO;
import com.insmess.knowledge.module.system.dal.mapper.auth.RelUserAuthProductMapper;
import com.insmess.knowledge.module.system.service.auth.IRelUserAuthProductService;

/**
 * 用户与认证中心关系Service业务层处理
 *
 * @author insmess
 * @date 2024-11-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelUserAuthProductServiceImpl  extends ServiceImpl<RelUserAuthProductMapper, RelUserAuthProductDO> implements IRelUserAuthProductService {
    @Resource
    private RelUserAuthProductMapper relUserAuthProductMapper;

}

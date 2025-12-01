package com.insmess.knowledge.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.insmess.knowledge.auth.domain.AuthClient;
import com.insmess.knowledge.auth.mapper.AuthClientMapper;
import com.insmess.knowledge.auth.service.IAuthClientService;

/**
 * 应用管理Service业务层处理
 *
 * @author insmess
 * @date 2024-08-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthClientServiceImpl  extends ServiceImpl<AuthClientMapper,AuthClient> implements IAuthClientService {
    @Autowired
    private AuthClientMapper authClientMapper;

}

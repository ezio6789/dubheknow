package com.insmess.knowledge.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.insmess.knowledge.auth.domain.AuthClientUser;
import com.insmess.knowledge.auth.mapper.AuthClientUserMapper;
import com.insmess.knowledge.auth.service.IAuthClientUserService;

/**
 * 应用和用户关联Service业务层处理
 *
 * @author insmess
 * @date 2024-08-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthClientUserServiceImpl  extends ServiceImpl<AuthClientUserMapper,AuthClientUser> implements IAuthClientUserService {
    @Autowired
    private AuthClientUserMapper authClientUserMapper;

}

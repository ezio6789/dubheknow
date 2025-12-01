package com.insmess.knowledge.common.exception.user;

import com.insmess.knowledge.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author insmess
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}

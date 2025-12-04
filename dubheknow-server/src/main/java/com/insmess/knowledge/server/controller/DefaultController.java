package com.insmess.knowledge.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.insmess.knowledge.common.core.domain.AjaxResult;


/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author insmess
 */
@RestController
public class DefaultController {

    @RequestMapping({"/dev-api/example/**", "/prod-api/example/**", "/example/**"})
    public AjaxResult example404() {
        return AjaxResult.error("[示例模块 dubheknow-module-example - 已禁用]");
    }

}

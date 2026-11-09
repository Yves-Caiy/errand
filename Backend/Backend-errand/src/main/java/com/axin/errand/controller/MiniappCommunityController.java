package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.system.domain.Community;
import com.axin.system.service.ICommunityService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "小程序小区接口")
@ApiSupport(author = "AXIN", order = 3)
@RestController
@RequestMapping("/miniapp/community")
public class MiniappCommunityController extends BaseController {

    @Autowired
    private ICommunityService communityService;

    @Operation(summary = "获取小区列表")
    @ApiOperationSupport(order = 50)
    @Anonymous
    @GetMapping("/list")
    public AjaxResult communityList() {
        Community query = new Community();
        query.setIsEnabled(1);
        return success(communityService.selectCommunityList(query));
    }
}

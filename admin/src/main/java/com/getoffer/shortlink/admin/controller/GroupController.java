package com.getoffer.shortlink.admin.controller;

import com.getoffer.shortlink.admin.common.convention.result.Result;
import com.getoffer.shortlink.admin.common.convention.result.Results;
import com.getoffer.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.getoffer.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.getoffer.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/group")
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增短链接分组
     */
    @PostMapping()
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }

    @GetMapping()
    public Result<List<ShortLinkGroupRespDTO>> listgroup() {
        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOList = groupService.listgroup();
        return Results.success(shortLinkGroupRespDTOList);
    }

}

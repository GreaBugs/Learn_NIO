package com.getoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getoffer.shortlink.admin.dao.entity.GroupDO;
import com.getoffer.shortlink.admin.dao.mapper.GroupMapper;
import com.getoffer.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.getoffer.shortlink.admin.service.GroupService;
import com.getoffer.shortlink.admin.util.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        String gid;
        do {
            gid = RandomGenerator.generateRandom();
        } while (hasGid(gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listgroup(){
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDel_flag, 0)
                .eq(GroupDO::getUsername, null)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdate_time);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
    }

    private boolean hasGid(String gid){
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                // TODO 设置用户名
                .eq(GroupDO::getUsername, null);
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}

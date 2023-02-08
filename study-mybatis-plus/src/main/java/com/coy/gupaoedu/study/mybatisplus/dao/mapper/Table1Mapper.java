package com.coy.gupaoedu.study.mybatisplus.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coy.gupaoedu.study.mybatisplus.dao.entity.Table1Entity;
import com.coy.gupaoedu.study.mybatisplus.dto.Table1Data;

import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/9/6 16:39
 */
public interface Table1Mapper extends BaseMapper<Table1Entity> {

    public Table1Data findRootNode(String groupId);

    public List<Map> findChildNode(String groupId);

    public int updateRootFlag(Integer id);
}

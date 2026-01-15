package com.gocle.lxp.tracking.mapper.tracking;

import com.gocle.lxp.tracking.domain.LearningLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearningLogMapper {
    int insert(LearningLog log);
}

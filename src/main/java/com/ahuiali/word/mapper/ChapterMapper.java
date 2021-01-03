package com.ahuiali.word.mapper;

import com.ahuiali.word.pojo.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ChapterMapper
 *
 * @author ZhengChaoHui
 * @date 2021/1/3 23:51
 */
@Mapper
@Repository
public interface ChapterMapper extends BaseMapper<Chapter> {
}

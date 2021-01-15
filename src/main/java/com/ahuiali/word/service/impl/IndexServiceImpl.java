package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.UpdateBaseDataUtil;
import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.dto.BaseInfoDto;
import com.ahuiali.word.dto.IndexDto;
import com.ahuiali.word.dto.LearnerSettingDto;
import com.ahuiali.word.service.ArticleService;
import com.ahuiali.word.service.IndexService;
import com.ahuiali.word.service.WordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IndexServiceImpl
 * 主页service实现类
 *
 * @author ZhengChaoHui
 * @date 2020/12/24 12:38
 */
@Service
@Transactional
public class IndexServiceImpl implements IndexService {

    @Autowired
    private WordbookService wordbookService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UpdateBaseDataUtil updateBaseDataUtil;

    @Override
    public Response<IndexDto> getIndexDto(Integer learnerId) {
        Response<IndexDto> response = Response.success();
        // 获取基本信息
        BaseInfoDto baseInfoDto = wordbookService.getMemorizingWordbookAndReviewCount(learnerId);
        if (baseInfoDto == null) {
            baseInfoDto = new BaseInfoDto();
        }
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                baseInfoDto.setIsNotice(dto.getIsNotice());
                baseInfoDto.setTodayLearnCount(dto.getTodayLearnCount());
                baseInfoDto.setTodayReviewCount(dto.getTodayReviewCount());
                baseInfoDto.setTodayReadCount(dto.getTodayReadCount());
            }
        } else {
            LearnerSettingDto dto = updateBaseDataUtil.getDto(learnerId);
            baseInfoDto.setIsNotice(dto.getIsNotice());
            baseInfoDto.setTodayLearnCount(dto.getTodayLearnCount());
            baseInfoDto.setTodayReviewCount(dto.getTodayReviewCount());
            baseInfoDto.setTodayReadCount(dto.getTodayReadCount());
        }
        // 获取最新文章
        List<ArticleDto> lastestArticle = articleService.getLastestArticle();
        IndexDto indexDto = new IndexDto(baseInfoDto, lastestArticle);
        response.setData(indexDto);
        return response;
    }
}

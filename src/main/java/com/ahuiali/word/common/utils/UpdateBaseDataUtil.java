package com.ahuiali.word.common.utils;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.dto.LearnerSettingDto;
import com.ahuiali.word.mapper.LearnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * UpdateBaseDataUtil
 * 更新用户基础数据工具
 * @author ZhengChaoHui
 * @date 2021/1/13 16:32
 */
@Component
public class UpdateBaseDataUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LearnerMapper learnerMapper;

    public void update(LearnerSettingDto dto, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        redisTemplate.opsForValue().set(key, dto);
    }

    /**
     * 更新每日学习单词数
     * @param todayLearnCount 新增数
     * @param learnerId 用户id
     */
    public void addTodayLearnCount(Integer todayLearnCount, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayLearnCount(dto.getTodayLearnCount() + todayLearnCount);
                redisTemplate.opsForValue().set(key, dto);
            }
        } else {
            LearnerSettingDto dto = getDto(learnerId);
            dto.setTodayLearnCount(todayLearnCount);
            redisTemplate.opsForValue().set(key, dto);
        }
    }

    /**
     * 更新复习数
     * @param todayReviewCount 新增复习数
     * @param learnerId 用户id
     */
    public void addTodayReviewCount(Integer todayReviewCount, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayReviewCount(dto.getTodayReviewCount() + todayReviewCount);
                redisTemplate.opsForValue().set(key, dto);
            }
        } else {
            LearnerSettingDto dto = getDto(learnerId);
            dto.setTodayReviewCount(todayReviewCount);
            redisTemplate.opsForValue().set(key, dto);
        }
    }

    /**
     * 更新小说阅读时间
     * @param time 时间
     * @param learnerId 用户id
     */
    public void addTodayReadCount(Integer time, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayReadCount(dto.getTodayReadCount() + time);
                redisTemplate.opsForValue().set(key, dto);
            }
        }else {
            LearnerSettingDto dto = getDto(learnerId);
            dto.setTodayReadCount(time);
            redisTemplate.opsForValue().set(key, dto);
        }
    }

    /**
     * 新建一个dto
     * @param learnerId 用户id
     * @return LearnerSettingDto
     */
    public LearnerSettingDto getDto(Integer learnerId){
        LearnerSettingDto dto = new LearnerSettingDto();
        dto.setTodayReadCount(0);
        dto.setTodayReviewCount(0);
        dto.setTodayLearnCount(0);
        dto.setIsNotice(0);
        dto.setLearnerId(learnerId);
        dto.setEmail(learnerMapper.queryEmailById(learnerId));
        return dto;
    }
}

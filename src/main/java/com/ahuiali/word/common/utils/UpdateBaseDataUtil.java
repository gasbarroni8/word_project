package com.ahuiali.word.common.utils;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.dto.LearnerSettingDto;
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

    public void update(LearnerSettingDto dto, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        redisTemplate.opsForValue().set(key, dto);
    }

    public void addTodayLearnCount(Integer todayLearnCount, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayLearnCount(dto.getTodayLearnCount() + todayLearnCount);
                redisTemplate.opsForValue().set(key, dto);
            }
        }
    }

    public void addTodayReviewCount(Integer todayReviewCount, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayReviewCount(dto.getTodayReviewCount() + todayReviewCount);
                redisTemplate.opsForValue().set(key, dto);
            }
        }
    }

    public void addTodayReadCount(Integer time, Integer learnerId) {
        String key = String.format(RedisKeyConstant.LEARNER_INFO, learnerId);
        if (redisTemplate.hasKey(key)) {
            LearnerSettingDto dto =  (LearnerSettingDto)redisTemplate.opsForValue().get(key);
            if (dto != null) {
                dto.setTodayReadCount(dto.getTodayReadCount() + time);
                redisTemplate.opsForValue().set(key, dto);
            }
        }
    }
}

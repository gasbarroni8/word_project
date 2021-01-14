package com.ahuiali.word.service.impl;

import com.ahuiali.word.mapper.LearnerDataMapper;
import com.ahuiali.word.pojo.LearnerData;
import com.ahuiali.word.service.LearnerDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * LearnerDataServiceImpl
 *
 * @author ZhengChaoHui
 * @date 2021/1/14 11:54
 */
@Service
@Slf4j
public class LearnerDataServiceImpl extends ServiceImpl<LearnerDataMapper, LearnerData> implements LearnerDataService {
}

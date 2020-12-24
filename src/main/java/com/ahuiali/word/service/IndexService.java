package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.IndexDto;

/**
 * IndexService
 * 主页service
 * @author ZhengChaoHui
 * @date 2020/12/24 12:36
 */
public interface IndexService {

    /**
     * 获取主页信息
     * @param learnerId 用户id
     * @return Response<IndexDto>
     */
    Response<IndexDto> getIndexDto(Integer learnerId);
}

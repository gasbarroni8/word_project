package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.LoginDto;
import com.ahuiali.word.pojo.Learner;


public interface LearnerService {

    Response<LoginDto> queryLearner(Learner learner);

    Response<?> queryLearnerByEmail(String email);

    Response<?> queryLearnerByNickname(String nickname);

    Response<?> register(Learner learner);

    Response<?> confirm(String token);

    Response<?> sentEmailAgain(String email);

    Response<?> findPassword(String email);

    Response<?> updatePassword(String email, String password);

    Response<?> findAllReviewNoticeLearners();
}

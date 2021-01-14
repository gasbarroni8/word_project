package com.ahuiali.word.job;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.PageUtil;
import com.ahuiali.word.dto.BaseInfoDto;
import com.ahuiali.word.dto.LearnerSettingDto;
import com.ahuiali.word.pojo.Learner;
import com.ahuiali.word.pojo.LearnerData;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.Wordbook;
import com.ahuiali.word.service.LearnerDataService;
import com.ahuiali.word.service.LearnerService;
import com.ahuiali.word.service.WordService;
import com.ahuiali.word.service.WordbookService;
import com.ahuiali.word.spider.SpiderLaunch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 定时任务类
 *
 * @author ZhengChaoHui
 * @date 2020/9/20 14:16
 */
@Component
@Slf4j
public class Job {

    @Autowired
    private LearnerService learnerService;

    @Autowired
    private WordService wordService;

    @Autowired
    private WordbookService wordbookService;

    @Autowired
    private LearnerDataService learnerDataService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpiderLaunch spiderLaunch;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每天6、9、15、21点爬取
     * 爬取CCTV
     */
    @Scheduled(cron = "0 0 6,9,15,21 * * ? ")
    public void startSpiderCCTV() {
        log.info("开始爬取CCTV，date:{}", new Date());
        spiderLaunch.startSpiderCCTV();
    }

    /**
     * 每天6、9、15、21点爬取
     * 爬取中国日报
     */
    @Scheduled(cron = "0 0 6,9,15,21 * * ? ")
    public void startSpiderChinaDaily() {
        log.info("开始爬取ChinaDaily，date:{}", new Date());
        spiderLaunch.startSpiderChinaDaily();
    }

    /**
     * 保存用户今日学习数据
     * 23：45分执行
     */
    @Scheduled(cron = "0 45 23 * * ?")
    public void startSaveLearnerData() {
        // 从redis中拿取数据
        String prefix = "learner:*";
        Set<String> keys = redisTemplate.keys(prefix);
        if (keys != null && keys.size() > 0) {
            List<LearnerData> dtos = new ArrayList<>(keys.size());
            keys.forEach(e -> {
                LearnerSettingDto dto = (LearnerSettingDto) redisTemplate.opsForValue().get(e);
                if (dto != null) {
                    // 清空数据
                    LearnerData newDto = new LearnerData();
                    BeanUtils.copyProperties(dto, newDto);
                    newDto.setDate(new Date());
                    // redis中数据置0
                    dto.setTodayReadCount(0);
                    dto.setTodayReviewCount(0);
                    dto.setTodayLearnCount(0);
                    // 更新清空
                    redisTemplate.opsForValue().set(String.format(RedisKeyConstant.LEARNER_INFO, dto.getLearnerId()), dto);
                    // 加入
                    dtos.add(newDto);
                }
                learnerDataService.saveOrUpdateBatch(dtos);
            });
        }

    }


    public static void main(String[] args) {
        new Job().cronJob1();
    }

    /**
     * 表示每天7时执行
     */
    @Scheduled(cron = "0 0 7 * * ? ")
    public void cronJob1() {
        // 查询所有用户*
        // 先给我自己查
        log.info("【邮箱推送功能】开始定时查询");
        List<Learner> list = (List<Learner>) learnerService.findAllReviewNoticeLearners().getData();
        for (Learner learner : list) {
            if (learner == null) {
                continue;
            }
            // 查询用户当前计划的复习词汇
            BaseInfoDto baseInfoDto = wordbookService.getMemorizingWordbookAndReviewCount(learner.getId());
            if (baseInfoDto == null) {
                continue;
            }
            if (baseInfoDto.getReviewCount() > 0) {
                // 查询总数
                Integer count = wordbookService.findReviewCount(learner.getId(), baseInfoDto.getId());
                // 查询单词（最多30）
                Response<List<Word>> reviewWords = (Response<List<Word>>) wordService.getReviewWords(learner.getId(), baseInfoDto.getId(), new PageUtil());
                List<Word> words = reviewWords.getData();
                // 通知
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                sb.append("<h2>您有").append(count).append("个单词需要复习</h2>");
                for (Word word : words) {
                    String wordMeaning = word.getParaphrase();
                    wordMeaning = wordMeaning.replaceAll("<br>", "");
                    if (wordMeaning.length() > 15) {
                        wordMeaning = wordMeaning.substring(0, 15) + "...";
                    } else if ("".equals(wordMeaning) || wordMeaning == null) {
                        wordMeaning = "暂无释义";
                    }
                    sb.append(word.getWord()).append(" : ").append(wordMeaning).append("<br>");
                }
                Response<?> emailResp = sentEmail("1170782807@qq.com", "1170782807@qq.com", "您有需要复习的单词", sb.append("</body></html>").toString());
                if (Response.isSuccess(emailResp)) {
                    log.info("发送邮箱成功！用户：{}", learner.getEmail());
                }
            }
        }
        log.info("【邮箱推送功能】结束定时查询");
    }


    public Response<?> sentEmail(String from, String to, String title, String msg) {
        try {
            // MimeMessage可以显示html效果
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(msg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("【定时推送】邮箱发送失败，接收方：{}", to);
            e.printStackTrace();
            //出错，邮箱发送失败
            return Response.result(Constant.Error.EMAIL_SEND_ERROR);
        } catch (Throwable e) {
            //出错，邮箱发送失败
            log.error("邮箱发送失败（大错误类捕获）, error:{}", e.toString());
        }
        //成功，返回200
        return Response.success();
    }
}

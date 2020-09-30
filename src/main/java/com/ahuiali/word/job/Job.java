package com.ahuiali.word.job;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.PageUtil;
import com.ahuiali.word.pojo.Learner;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.Wordbook;
import com.ahuiali.word.service.LearnerService;
import com.ahuiali.word.service.WordService;
import com.ahuiali.word.service.WordbookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务类
 * @author ZhengChaoHui
 * @Date 2020/9/20 14:16
 */
@Component
@Slf4j
public class Job {

    @Autowired
    private LearnerService learnerService;

    @Autowired
    WordService wordService;

    @Autowired
    WordbookService wordbookService;

    @Autowired
    private JavaMailSender javaMailSender;
//    //表示方法执行完成后5秒
//    @Scheduled(fixedDelay = 5000)
//    public void fixedDelayJob() throws InterruptedException {
//        System.out.println("fixedDelay 每隔5秒" + new Date());
//    }
//
//    //表示每隔3秒
//    @Scheduled(fixedRate = 3000)
//    public void fixedRateJob() {
//
//        System.out.println("fixedRate 每隔3秒" + new Date());
//    }

    /**
     *     表示每天7时执行
     */
    @Scheduled(cron = "0 0 7 * * ? ")
    public void cronJob1() {
        // 查询所有用户*
        // 先给我自己查
        log.info("【邮箱推送功能】开始定时查询");
        Response<Learner> learner1 = (Response<Learner>) learnerService.queryLearnerByEmail("1170782807@qq.com");
        Response<Learner> learner2 = (Response<Learner>) learnerService.queryLearnerByEmail("zhenghuihui777@gmail.com");
        Response<Learner> learner3 = (Response<Learner>) learnerService.queryLearnerByEmail("15900135325@163.com");
        List<Learner> learners = new ArrayList<>(2);
        learners.add(learner1.getData());
        learners.add(learner2.getData());
        learners.add(learner3.getData());
        for (Learner learner : learners) {
            if (learner == null) {
                continue;
            }
            // 查询用户当前计划的复习词汇
            Response<Wordbook> response = (Response<Wordbook>) wordbookService.getMemorizingWordbookAndReviewCount(learner.getId());
            if (!Constant.SUCCESS.getCode().equals(response.getCode())) {
                continue;
            }
            Wordbook wordbook = response.getData();
            if (wordbook.getReview_count() > 0) {
                // 查询总数
                Integer count = wordbookService.findReviewCount(learner.getId(), wordbook.getId());
                // 查询单词（最多30）
                Response<List<Word>> reviewWords = (Response<List<Word>>) wordService.getReviewWords(learner.getId(), wordbook.getId(), new PageUtil());
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
                if (Response.isSuccess(emailResp)){
                    log.info("发送邮箱成功！用户：{}", learner.getEmail());
                }
            }
        }
        log.info("【邮箱推送功能】结束定时查询");
    }


    public Response<?> sentEmail(String from, String to,String title, String msg){
        try{
            // MimeMessage可以显示html效果
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(msg, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error("【定时推送】邮箱发送失败，接收方：{}", to);
            e.printStackTrace();
            //出错，邮箱发送失败
            return Response.result(Constant.Error.EMAIL_SEND_ERROR);
        }
        //成功，返回200
        return Response.success();
    }
}

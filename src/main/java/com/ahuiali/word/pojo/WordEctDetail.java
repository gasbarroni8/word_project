package com.ahuiali.word.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class WordEctDetail extends WordEct implements Serializable {

    private Integer word_id;

    private String definition;

    private Integer collins;

    private Integer oxford;

    private String tag;

    private Integer bnc;

    private Integer frq;

    private String exchange;

    private String sentence_list;

    private List<Sentence> sentences;

    public Integer getWord_id() {
        return word_id;
    }

    public void setWord_id(Integer word_id) {
        this.word_id = word_id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getCollins() {
        return collins;
    }

    public void setCollins(Integer collins) {
        this.collins = collins;
    }

    public Integer getOxford() {
        return oxford;
    }

    public void setOxford(Integer oxford) {
        this.oxford = oxford;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getBnc() {
        return bnc;
    }

    public void setBnc(Integer bnc) {
        this.bnc = bnc;
    }

    public Integer getFrq() {
        return frq;
    }

    public void setFrq(Integer frq) {
        this.frq = frq;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSentence_list() {
        return sentence_list;
    }

    public void setSentence_list(String sentence_list) {
        this.sentence_list = sentence_list;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    @Override
    public String toString() {
        return "WordEctDetail{" +
                "word_id=" + word_id +
                ", definition='" + definition + '\'' +
                ", collins=" + collins +
                ", oxford=" + oxford +
                ", tag='" + tag + '\'' +
                ", bnc=" + bnc +
                ", frq=" + frq +
                ", exchange='" + exchange + '\'' +
                ", sentence_list='" + sentence_list + '\'' +
                ", sentences=" + sentences +
                super.toString()+
                '}';
    }
}

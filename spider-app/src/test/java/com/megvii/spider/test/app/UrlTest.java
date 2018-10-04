package com.megvii.spider.test.app;

import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class UrlTest {

  public static void main(String[] args) throws UnsupportedEncodingException {
    //%E5%A4%AA%E5%8E%9F%E5%B7%A5%E4%B8%9A%E5%AD%A6%E9%99%A2&
    String encode = URLEncoder.encode("太原工业学院", "UTF-8");
    System.out.println(encode);
//         List<Word> words = WordSegmenter.seg("今天晚上还睡觉不");
//         List<Word> wordss = WordSegmenter.segWithStopWords("今天晚上还睡觉不");
//         System.out.println(words);
//         System.out.println(wordss);
    System.out.println(URLDecoder.decode("%E6%B2%90%E7%B4%AB%E4%B8%BA%E6%9D%8E", "UTF-8"));

//         String url="/p/5414842553?pn=2";
//         boolean matches = url.matches("/p/\\d++\\?pn=\\d++");
//         System.out.println(matches);

//         String userHome="/home/main";
//         boolean matchess = userHome.matches("/home/main(.*?)");
//         System.out.println(matchess);
//         String level="forum_level lv13";
//         System.out.println(StringUtils.substringAfter(level, "forum_level lv"));
//                 
    String url = "0.0";
    boolean matches = url.matches("\\d++");
    System.out.println("-->" + matches);
//         
//         String utrl="http://tieba.baidu.com/f?kw=xx";
//         boolean b = utrl.matches("http://tieba.baidu.com/f\\?kw=(.*?)");
//         System.out.println(b);
//         
//         String urlss="http://tieba.baidu.com/p/5566536525";
//         System.out.println(StringUtils.substringBefore(urlss, "?pn="));
//         String age="吧龄:0";
//         System.out.println(age.substring(3, age.length()-1));
//         System.out.println(StringUtils.substringBetween(age, "吧龄:", "年"));
//         String utrl="//tieba.baidu.com/f?kw=%E5%A4%AA%E5%8E%9F%E5%B7%A5%E4%B8%9A%E5%AD%A6%E9%99%A2&ie=utf-8&pn=50";
//         boolean b = utrl.matches("(.*)kw=(.*)\\&ie=utf-8&pn=\\d++");
//         System.out.println(b);
    String intern ="https://my.hupu.com/103945086148127";
    boolean matches1 = intern.matches("https://my.hupu.com/(.*?)");
    System.out.println(matches1);
    List<String> list=new ArrayList<>();
    list.add("aaa");
    list.add("aaa");
    list.add("bbb");
    list.add("bbb");
    HashSet<String> set = new HashSet<>(list);
    System.out.println(set);
  }
}

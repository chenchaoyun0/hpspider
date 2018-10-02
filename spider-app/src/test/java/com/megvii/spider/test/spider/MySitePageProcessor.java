package com.megvii.spider.test.spider;

import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.URLGeneratedUtil;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class MySitePageProcessor implements PageProcessor {

    private static final String ONE_PAGE = "/bookmanager/indexHome\\?pageNo\\=\\d++\\&";

    private static final String ORDER_NUM = "ZF201710169692T66jkr";// 讯代理订单号
    private static final String SECRET = "3b23ace31a2447baa44d624e9c5fd0f5";// 讯代理密码

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
    private Site site = Site.me()//
            .addHeader("Proxy-Authorization", ProxyGeneratedUtil.authHeader(ORDER_NUM, SECRET, (int) (new Date().getTime() / 1000)))// 设置代理
            .setDisableCookieManagement(true).setCharset("UTF-8")//
            .setTimeOut(30000)//
            .setRetryTimes(3)//
            .setSleepTime(new Random().nextInt(20) * 100)//
            .setUserAgent(UserAgentUtil.getRandomUserAgent());

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        // 获取所有页
        List<String> listUrls = page.getHtml().links().regex(ONE_PAGE).all();
        listUrls.forEach(e -> URLGeneratedUtil.generatePostURL(e));
        page.addTargetRequests(listUrls);
        //
        if (page.getUrl().regex(ONE_PAGE).match()) {
            crawlIpCount(page);
        }
    }

    private void crawlIpCount(Page page) {
        // 4,6,8,10
        Selectable url = page.getUrl();
        System.out.println(url);
        List<IpCount> ipCountList = new ArrayList<>();
        for (int i = 4; i <= 18; i = i + 2) {
            // 获取标签 div 内的 class值
            String claAttr = page.getHtml().xpath("/html/body/div[5]/table/tbody/tr[4]/th[2]/").$("a", "href").get();
            // 获取a标签的href
            String href = page.getHtml().xpath("/html/body/div[5]/table/tbody/tr[4]/th[2]/a/").$("div", "class").get();
            // 获取标签后端传过来的值
            String ip = page.getHtml().xpath("/html/body/div[5]/table/tbody/tr[" + i + "]/th[2]/a/div/text()").toString();
            String count = page.getHtml().xpath("/html/body/div[5]/table/tbody/tr[" + i + "]/th[11]/text()").toString();
            IpCount ipCount = new IpCount();
            //
            ipCount.setCount(Integer.parseInt(count));
            ipCount.setIp(ip);
            //
            ipCountList.add(ipCount);
        }
        page.putField("ipCountList", ipCountList);

    }

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 设置动态转发代理，使用定制的ProxyProvider
        //httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
        Spider.create(new MySitePageProcessor())//
                .addUrl("http://www.shopbop.ink/bookmanager/indexHome?pageNo=119&")//
                .addPipeline(new MySitePipeline())//
                .thread(4)//
                .run();
    }
}

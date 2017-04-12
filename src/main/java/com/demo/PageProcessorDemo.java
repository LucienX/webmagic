package com.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 * @time:2017年4月12日 下午5:25:32
 * @author Wuqx
 *
 * @Description:webmagic爬虫小程序
 */
public class PageProcessorDemo implements PageProcessor {
	// 抓取网站相关配置，包括编码，抓取间隔，重试次数等。
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	@Override
	public Site getSite() {
		
		return site;
	}

	@Override
	public void process(Page page) {
		//定义如何抽取页面信息，并保存下来
				//头条新闻
				page.putField("firstNewsTitle", page.getHtml().xpath("//div[@class='mod-tab-content']/div/div/ul/li/strong/a/text()").toString());
				//头条链接
				page.putField("firstNewsLink", page.getHtml().xpath("//div[@class='mod-tab-content']/div/div/ul/li/strong").links().toString());
	
	if(page.getResultItems().get("firstNewsTitle")==null){
		//设置skip之后，这个页面的结果不会被pipeline处理
		page.setSkip(true);
	}
	
	//从页面发现后续的url地址来获取
	page.addTargetRequests(page.getHtml().links().regex("(http://news.baidu.com\\w+/\\w+)").all());
	}
	
	public static void main(String[] args){
	Spider.create(new PageProcessorDemo())
	.addUrl("http://news.baidu.com/")//设置开始抓取的页面
	.thread(5)//开启5个线程抓
	.run();//启动爬虫
	}

}

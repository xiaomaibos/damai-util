package com.flyme.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.*;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.flyme.model.Level;
import com.flyme.model.Program;
import com.flyme.model.Show;
import org.apache.http.Header;
import org.apache.http.HttpStatus;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 大麦网爬取类
 *
 * @author zzzz76
 */
public class DamaiCrawler {

    private HttpClientFactory httpClientFactory = HttpClientFactory.httpClientFactory;

    // 同步爬取
    public String synCrawl(String code, Program program) {
        String errStatus = null;
        try {
            HttpResult httpResult = crawlProgram(code);
            if(httpResult.getStatusCode() == HttpStatus.SC_OK) {
                String json = RegUtil.regFind(httpResult.getResult(), "__jp0\\(([\\s\\S]*)\\)");
                transform(json, program);

                String destUrl = "image/program/" + code + ".jpg";
                downImage(program.getImageUrl(), destUrl);
                program.setImageUrl(destUrl);

            } else {
                errStatus = "RESPONSE_ERROR";
            }
        } catch (HttpProcessException e) {
            e.printStackTrace();
            errStatus = "HTTP_ERROR";
        } catch (ParseException e) {
            e.printStackTrace();
            errStatus = "PARSE_ERROR";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errStatus = "FILE_ERROR";
        }
        return errStatus;
    }

    // 爬取演出，场次，票档
    private HttpResult crawlProgram(String code) throws HttpProcessException {
        String url = "https://detail.damai.cn/subpage?apiVersion=2.0&dmChannel=pc@damai_pc&bizCode=ali.china.damai&scenario=itemsku&callback=__jp0&itemId=" + code;

        //配置Header
        Header[] headers = HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .acceptLanguage("en-GB,en;q=0.8")
                .referer("https://detail.damai.cn/item.htm?&id=" + code)
                .build();

        //配置请求参数
        HttpConfig config = HttpConfig.custom()
                .headers(headers)
                .timeout(1000)
                .url(url)
                .encoding("utf-8")
                .client(httpClientFactory.build())
                .method(HttpMethods.GET);

        return HttpClientUtil.sendAndGetResp(config);
    }

    // 下载图片
    private void downImage(String sourUrl, String destUrl) throws HttpProcessException, FileNotFoundException {
        //配置Header
        Header[] headers = HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .acceptLanguage("en-GB,en;q=0.8")
                .build();

        //配置请求参数
        HttpConfig config = HttpConfig.custom()
                .headers(headers)
                .timeout(1000)
                .url(sourUrl)
                .encoding("utf-8")
                .client(HttpClientFactory.httpClientFactory.build())
                .method(HttpMethods.GET)
                .out(new FileOutputStream(destUrl));

        HttpClientUtil.down(config);
    }

    // 将爬取结果转换成目标格式
    private void transform(String json, Program program) throws ParseException {
        JSONObject obj = JSON.parseObject(json);
        // 获取节目信息
        JSONObject info = obj.getJSONObject("itemBasicInfo");
        program.setTitle(info.getString("projectTitle"));

        String priceRange = info.getString("priceRange");
        int lowPrice = Integer.valueOf(RegUtil.regFind(priceRange, "￥(\\d+)"));
        int highPrice = Integer.valueOf(RegUtil.regFind(priceRange, "- ￥(\\d+)"));
        program.setLowPrice(lowPrice);
        program.setHighPrice(highPrice);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        Date startTime = simpleDateFormat.parse(info.getString("sellingStartTime"));
        program.setStartTime(startTime);
        program.setAddress(info.getString("venueName"));
        program.setImageUrl(info.getString("mainImageUrl"));

        // 获取所有场次信息
        JSONArray views = obj.getJSONObject("performCalendar").getJSONArray("performViews");
        for (Object view: views) {
            JSONObject vi = (JSONObject)view;
            Show show = new Show();
            show.setName(vi.getString("performName"));
            Date istartTime = simpleDateFormat.parse(vi.getString("performBeginDTStr"));
            show.setTime(istartTime);

            JSONArray skus = obj.getJSONObject("perform").getJSONArray("skuList");
            for (Object sku : skus) {
                JSONObject sk = (JSONObject)sku;
                Level level = new Level();
                level.setName(sk.getString("priceName"));
                level.setPrice(Integer.valueOf(RegUtil.regFind(sk.getString("price"), "(\\d+)")));
                level.setLimitCount(Integer.valueOf(sk.getString("salableQuantity")));
                show.getLevels().add(level);
            }
            program.getShows().add(show);
        }
    }

}

package com.theswitchbot.common.util.html;

import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    /**
     * 获取图片字符串中所有链接
     */
    public static List<String> getImgList(String htmlStr) {
        List<String> pics = new ArrayList<>();
        if (htmlStr == null)return pics;
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    public static  List<String> getImgTags(String htmlStr) {
        List<String> imgTags = new ArrayList<>();
        if (htmlStr == null)return imgTags;
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            imgTags.add(img);
        }
        return imgTags;
    }

    public static String getSimpleText(String htmlStr){
        if (htmlStr == null)return "";
        String result = htmlStr.replaceAll("<img.*>.*</img>","")
                .replaceAll("<img.*/>","");
        return result.trim();
    }

    public static void renderHtml(TextView textView, String htmlStr){
        htmlStr = htmlStr.replaceAll("\n", "<br />");
        CharSequence html = Html.fromHtml(htmlStr, new GlideImageGetter(textView), null);
        textView.setText(html);
    }
}

package com.cmlanche.core.utils;

import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cmlanche.core.search.node.NodeInfo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.jvm.internal.PackageReference;

public class Utils {

    public final static String tag = "Jixieshou -" + Version.ver;

    /**
     * 正则标志
     */
    public final static String REGULAR = "/";
    
    
    public static void sleep(long sleeptime) {
        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 检测一个类中是否存在某个方法。
     *
     * @param clazz      要检查的类。
     * @param methodName 要检查的方法名。
     * @param param      参数列表。
     * @return 如果找到则返回true，如果未找到则返回false。
     */
    public static boolean isMethodExist(Class<?> clazz, String methodName, Class<?>... param) {
        try {
            clazz.getMethod(methodName, param);
            return true;
        } catch (NoSuchMethodException e) {
            Log.e(tag, "Cannot find method " + methodName);
        } catch (SecurityException e) {
            Log.e(tag, "Due to security issue, unable to access method " + methodName);
        }
        return false;
    }

    public static boolean isStandardWebView(String className) {
        return "android.webkit.WebView".equals(className);
    }

    public static void printAllMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Log.e("UITestin", method.getName());
            Class<?>[] types = method.getParameterTypes();
            for (Class<?> type : types) {
                Log.e("UITestin", type.toString());
            }
        }
    }


    public static String getRootPackageName(AccessibilityNodeInfo root) {
        if(root == null){
            return "";
        }
        return root.getPackageName() != null ? root.getPackageName().toString() : "";
    }

    public static String unicodeFilter(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((int) c < 55357) {
                unicode.append(c);
            }
        }
        return unicode.toString();
    }

    public static List<NodeInfo> keepXpathSimilyNode(List<NodeInfo> rects, String targetXpath) {
        if (rects == null || rects.size() <= 1) {
            return rects;
        }

        double bestMatchRate = 0.0;
        NodeInfo selectedRect = null;

        // find the best matched nodes.
        for (NodeInfo rect : rects) {
            double matchRate = getMatchRate(rect.getXpath(), targetXpath);
            if (matchRate > bestMatchRate) {
                bestMatchRate = matchRate;
                selectedRect = rect;
            }
        }

        if (selectedRect != null) {
            Log.i(tag, "Selected Node: " + selectedRect);
            Log.i(tag, "BestMatchRate: " + bestMatchRate);
        } else {
            return rects;
        }

        ArrayList<NodeInfo> newRectList = new ArrayList<NodeInfo>();
        newRectList.add(selectedRect);

        return newRectList;
    }

    /**
     * 判断两个xpath的匹配程度
     *
     * @param xpath1
     * @param xpath2
     * @return 返回一个double数值，1表示100% match, 0表示完全不match
     */
    public static double getMatchRate(String xpath1, String xpath2) {
        if (xpath1 == null || xpath2 == null) {
            return 0.0d;
        }
        if (xpath1.equals(xpath2)) {
            return 1.0d;
        }
        String[] subXpath1 = xpath1.split("-");
        String[] subXpath2 = xpath2.split("-");
        int maxLen = Math.max(subXpath1.length, subXpath2.length);
        int minLen = Math.min(subXpath1.length, subXpath2.length);

        if (maxLen == 0 || minLen == 0) {
            return 0.0d;
        }
        int matchCount = 0;
        for (int i = 0; i < minLen; i++) {
            if (subXpath1[i].equals(subXpath2[i])) {
                matchCount++;
            }
        }
        return (double) matchCount / (double) maxLen;
    }

    private static String TAG = "Utils";
    public static boolean textMatch(String patternStr, String text) {
        Log.d(TAG,"patternStr:"+patternStr + " text:"+text);
        if(text.contains(patternStr)){
            return true;
        }
        if (!"".equals(patternStr) && patternStr.startsWith(Utils.REGULAR)
                && patternStr.endsWith(Utils.REGULAR)
                && patternStr.length() >= 2) {
            patternStr = patternStr.substring(1, patternStr.lastIndexOf(Utils.REGULAR));
            boolean isMatch = isRegularMatch(patternStr, text);
            return isMatch;
        }
        return isMatch(escapeExprSpecialWord(patternStr), text);
    }


    public static String escapeExprSpecialWord(String keyword) {
        return keyword.replaceAll("[\\\\$\\(\\)\\+\\.\\[\\]\\?\\^\\{\\}|]", "\\\\" + "$0");
    }


    private static boolean isMatch(String patternStr, String text) {
        patternStr = patternStr.replaceAll("\\*", ".*");
        // 去除换行符对匹配的干扰
        patternStr = removeLineSeparatorAndSpace(patternStr);
        text = removeLineSeparatorAndSpace(text);
        Pattern pattern = Pattern.compile(patternStr);
        return pattern.matcher(text).matches();

    }

    /**
     * 正则匹配文本
     *
     * @param regStr 正则表达式
     * @param text   需要被匹配的文本
     * @return 全部被匹配到返回true
     */
    public static boolean isRegularMatch(String regStr, String text) {
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
    public static String getCompatibleText(boolean isNeedFilter,String viewText){
    	if(!isNeedFilter){
    		return viewText;
    	}
        return unicodeFilter(viewText);
    }

    
    /**
     * 判断是否包含被过滤掉的字符
     * @param patternStr 用于对比查询的字符串，也就是itestin客户端传过来的
     * @return
     */
    public static boolean hasCompatibleChar(String patternStr){
    	if(patternStr == null){
    		return false;
    	}
        for (int i = 0; i < patternStr.length(); i++) {
            char c = patternStr.charAt(i);
            if ((int) c >= 55357) {
                return true;
            }
        }
        return false;  
    }

    
    /**
     * 去除换行符和空格
     * @param string
     * @return
     */
    private static String removeLineSeparatorAndSpace(String string){
        if (Utils.isEmpty(string)){
            return "";
        }
        return string.replaceAll("[\r\n\\t\\s]", "");
    }

    /**
     * 判断node是否在界面内
     *
     * @param node
     * @return
     */
    public static boolean isVisiableToUser(AccessibilityNodeInfo node, int screenW, int screenH) {
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        return isVisiableToUser(bounds, screenW, screenH);
    }

    /**
     * 判断一个区域是否在屏幕内
     *
     * @param bounds
     * @param screenWidth
     * @param screenHeight
     * @return
     */
    public static boolean isVisiableToUser(Rect bounds, int screenWidth, int screenHeight) {
        // 指定元素是否在屏幕内
        boolean isVisiable = bounds.left < screenWidth && bounds.top < screenHeight &&
                bounds.right > 0 && bounds.bottom > 0 && bounds.width() * bounds.height() > 0;
        // Log.i(Utils.tag, "node bounds = " + bounds.toString() + "  isVisiable = " + isVisiable);
        // Log.i(Utils.tag, "node text = " + getNodeText(node, false));
        return isVisiable;
    }

    /**
     * 获取一个node的文本信息
     * 优先取text，如果text为空，则取ContentDescription属性
     *
     * @param node
     * @return
     */
    public static NodeText getNodeText(AccessibilityNodeInfo node, boolean isUseBase64) {
        return getNodeText(node, isUseBase64, false);
    }

    /**
     * 获取一个node的文本信息
     *
     * @param node
     * @param isUseBase64
     * @param ignoreContentDesc 是否忽略contentDescription
     * @return
     */
    public static NodeText getNodeText(AccessibilityNodeInfo node, boolean isUseBase64, boolean ignoreContentDesc) {
        String text = ((node == null || node.getText() == null) ? "" : node.getText().toString());
        boolean isFromContentDescription = false;
        if (!ignoreContentDesc) {
            String content = (node.getContentDescription() == null ? "" : node.getContentDescription().toString());
            if (Utils.isEmpty(text) && Utils.isNotEmpty(content)) {
                text = content;
                isFromContentDescription = true;
            }
        }
        text = getSafeText(text, isUseBase64);
        return new NodeText(text, isFromContentDescription);
    }

    public static String getSafeText(String source, boolean isUseBase64) {
        if (isUseBase64) {
            if (Utils.isNotEmpty(source)) {
                source = "[BASE64]" + getEncodedString(source);
            }
            return source;
        } else {
            return getXMLSafeText(source);
        }
    }


    public static String getXMLSafeText(String text) {
        if (Utils.isEmpty(text)) return "";
        text = text.replaceAll("\\n", " ");
        text = filterEmoji(text);
        text = safeCharSeqToString(text);
        return text;
    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {
                // buf.append("");
            }
        }
        return buf.toString();
    }


    public static String decodeBase64Xpath(String xpath) {

        if (Utils.isEmpty(xpath)) {
            return xpath;
        }

        Pattern p = Pattern.compile("'(\\[BASE64\\].*?)'");
        Matcher m = p.matcher(xpath);

        while (m.find()) {
            String match = m.group(1);
            String replacement = match;
            replacement = decodeBase64StringOnWindowInfo(replacement);
            xpath = xpath.replace(match, replacement);
        }
        return xpath;
    }


    public static String decodeBase64StringOnWindowInfo(String text) {
        try {
            if (Utils.isNotEmpty(text) && text.startsWith("[BASE64]")) {
                text = new String(com.cmlanche.core.utils.Base64.decode(text.replace("[BASE64]", "")), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
        }
        return text;
    }


    public static String pureId(String rid) {
        String id = "";
        if (Utils.isNotEmpty(rid) && rid.contains(":id/")) {
            id = rid.substring(rid.indexOf(":id/") + 4);
        } else {
            id = rid;
        }
        return id;
    }

    public static String safeCharSeqToString(CharSequence cs) {
        if (cs == null)
            return "";
        else {
            return stripInvalidXMLChars(cs);
        }
    }

    private static String stripInvalidXMLChars(CharSequence cs) {
        StringBuilder ret = new StringBuilder();
        char ch;
        for (int i = 0; i < cs.length(); i++) {
            ch = cs.charAt(i);
            if ((ch >= 0x1 && ch <= 0x8) || (ch >= 0xB && ch <= 0xC) || (ch >= 0xE && ch <= 0x1F)
                    || (ch >= 0x7F && ch <= 0x84) || (ch >= 0x86 && ch <= 0x9f) || (ch >= 0xFDD0 && ch <= 0xFDDF)
                    || (ch >= 0x1FFFE && ch <= 0x1FFFF) || (ch >= 0x2FFFE && ch <= 0x2FFFF)
                    || (ch >= 0x3FFFE && ch <= 0x3FFFF) || (ch >= 0x4FFFE && ch <= 0x4FFFF)
                    || (ch >= 0x5FFFE && ch <= 0x5FFFF) || (ch >= 0x6FFFE && ch <= 0x6FFFF)
                    || (ch >= 0x7FFFE && ch <= 0x7FFFF) || (ch >= 0x8FFFE && ch <= 0x8FFFF)
                    || (ch >= 0x9FFFE && ch <= 0x9FFFF) || (ch >= 0xAFFFE && ch <= 0xAFFFF)
                    || (ch >= 0xBFFFE && ch <= 0xBFFFF) || (ch >= 0xCFFFE && ch <= 0xCFFFF)
                    || (ch >= 0xDFFFE && ch <= 0xDFFFF) || (ch >= 0xEFFFE && ch <= 0xEFFFF)
                    || (ch >= 0xFFFFE && ch <= 0xFFFFF) || (ch >= 0x10FFFE && ch <= 0x10FFFF))
                ret.append(".");
            else
                ret.append(ch);
        }
        return ret.toString();
    }

    public static String getEncodedString(String string2encode) {
        if (string2encode == null || string2encode.equals("")) {
            return "";
        }
        try {
            byte[] data = Base64.encode(string2encode.getBytes("utf-8"), Base64.NO_WRAP);
            return new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(Utils.tag, e.getMessage(), e);
        }
        return string2encode;
    }

    /**
     * 判断是否为非空字符串
     *
     * @param str2test 要判断的文本
     * @return true/false
     */
    public static boolean isNotEmpty(String str2test) {
        return !Utils.isEmpty(str2test);
    }

    /**
     * 判断是否为空字符串，因为低版本的手机不支持 String.isEmpty方法。
     *
     * @param str2test 要测试的文本
     * @return true/false
     */
    public static boolean isEmpty(String str2test) {
        return (str2test == null || str2test.equals(""));
    }
    
    /**
     * Test if the given text is a number
     *
     * @param text given text
     * @return true/false
     */
    public static boolean isNumber(String text) {
        if(Utils.isEmpty(text)){
            return false;
        }
        Pattern p = Pattern.compile("[\\d]+[\\.]?[\\d]*");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * 将一个时间间隔转化为字符串
     *
     * @param diff 单位是毫秒
     * @return
     */
    public static String getTimeDescription(long diff) {
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String str = "";
        if (day != 0) {
            str += day + "天";
        }
        if (hour != 0) {
            str += hour + "小时";
        }
        if (min != 0) {
            str += min + "分钟";
        }
        if (sec != 0) {
            return str += sec + "秒";
        }
        return str;
    }

}

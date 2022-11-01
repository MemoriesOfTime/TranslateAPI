package cn.lanink.gamecore.translateapi.utils;

import cn.lanink.gamecore.translateapi.TranslateAPI;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author LT_Name
 */
public class Network {

    public static String get(String url, Map<String, String> params) {
        return get(getUrlWithQueryString(url, params));
    }

    public static String get(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpURLConnection uc = (HttpURLConnection) new URL(url).openConnection(TranslateAPI.getInstance().getNetworkProxy());
            uc.setRequestMethod("GET");
            uc.setConnectTimeout(10000);
            uc.setReadTimeout(10000);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
            uc.connect();
            if (uc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                in.close();
            }
            uc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    public static String post(String urlStr, Map<String, String> parameterMap) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection(TranslateAPI.getInstance().getNetworkProxy());
            uc.setConnectTimeout(10000);
            uc.setReadTimeout(10000);
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(uc.getOutputStream()));
            StringBuilder parameter = new StringBuilder();
            parameter.append("plugin=TranslateAPI");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameter.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            pw.write(parameter.toString());
            pw.flush();
            pw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            br.close();
            uc.disconnect();
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}

package com.example.demo.utils;

import com.google.gson.*;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class HTTPConnection {
    HttpURLConnection urlConnection = null;
    private String timeout = "60000";

    private String url;

    public String executeRequest(String method, String targetURL_, String params, Object body, HashMap<String, String> requestProperties,String type) throws Exception {
        HttpURLConnection con = null;
        String respJSON = null;
        String strParams []= params.split(" ");
        String paramsNew = null;
        if (strParams.length == 1){
            paramsNew = params;
        }else{
            paramsNew = strParams[0];
        }
        for (int i = 1; i < strParams.length; i++) {
            paramsNew = paramsNew + "%20" + strParams[i];
        }
        
        Map<String, JsonElement> newJson = new HashMap<String, JsonElement>();
        String[] targetURL = targetURL_ != null ? targetURL_.split(";") : url.split(";");
        boolean connectionError = false;
        String connectionDescription = "";
        for (String url: targetURL) {

            url = paramsNew != null ? url.concat(paramsNew) : url;
             try {
                URL urlToOpen = new URL(url);

                con = (HttpURLConnection) urlToOpen.openConnection();
                con.setRequestMethod(method);
                con.setConnectTimeout(Integer.parseInt(timeout));
                con.setReadTimeout(Integer.parseInt(timeout));
                if (body == null) {
                    con.setDoOutput(false);
                } else {
                    con.setDoOutput(true);
                }
                //con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                requestProperties.forEach(con::setRequestProperty);


                if (body != null) {
                    DataOutputStream out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(body.toString());
                    out.flush();
                    out.close();
                }

                int status = con.getResponseCode();
                StringBuffer content = new StringBuffer();
                if (status == 200 || status == 201) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    connectionError = false;
                } else {
                    BufferedReader error = new BufferedReader(
                            new InputStreamReader(con.getErrorStream()));
                    String inputLine;

                    while ((inputLine = error.readLine()) != null) {
                        content.append(inputLine);
                    }
                    error.close();
                    connectionError = true;
                    connectionDescription = "Status - " + status + "  " + con.getResponseMessage();
                    continue;
                }

                if (!content.toString().equals("")) {
                    respJSON = content.toString();
                    /*if (type == "results") {
                        JsonParser parser = new JsonParser();
                        JsonElement jsonTree = parser.parse(respJSON);
                        JsonObject jsonObject = jsonTree.getAsJsonObject();
                        JsonElement respResult = jsonObject.get("results");
                        JsonArray gsonArr = respResult.getAsJsonArray();
                        JsonElement name = gsonArr.get(0).getAsJsonObject().get("title");
                        newJson.put("titulo", name);
                        newJson.put("imagen", gsonArr.get(0).getAsJsonObject().get("thumbnail"));
                        newJson.put("titulo", gsonArr.get(0).getAsJsonObject().get("title"));
                        newJson.put("seller", gsonArr.get(0).getAsJsonObject().get("seller").getAsJsonObject().get("seller_reputation").getAsJsonObject().get("power_seller_status"));
                        newJson.put("state", gsonArr.get(0).getAsJsonObject().get("address").getAsJsonObject().get("state_name"));
                    }*/
                }
                // la conexion se establecio correctamente
                break;
            } catch (Exception e) {
                connectionError = true;
                connectionDescription = e.getMessage();
            } finally {
                try {
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (Exception e1) {
                }
            }
        }
        if(connectionError){
           //throw new DomainException(ErrorCodes.ErrorCodeExcecuteHttp, connectionDescription);
        }
        //return newJson.toString().replace("=",":");
        return respJSON;
    }

    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static void main(String[] args){
       System.out.println(encodeValue("http://10.46.157.218:8081/ProxyUSSD/USSD/start?msisdn=UserNo"));
    }

}

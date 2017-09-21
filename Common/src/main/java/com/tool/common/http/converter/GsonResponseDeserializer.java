package com.tool.common.http.converter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tool.common.http.ResponseEntity;

import java.lang.reflect.Type;

/**
 * 这里解决因服务器API接口缺少规范化，需实现Gson的JsonDeserializer接口来完成自定义解析
 * 这里有几种情况
 * 1、当返回结果{"code":0,"info":"\u5bc6\u7801\u9519\u8bef","result":-5}
 * result应为JsonObject或JsonArray，但返回的是-5，该类型为基本数据类型，会导致Gson对泛型的解析错误并抛出异常，导致Retrofit2返回到onFailure方法
 * 2、当返回结果{"code":1,"info":"\u767b\u5f55\u6210\u529f","result":{"userid":"00001"}}
 * result为JsonObject或JsonArray，此时为规范数据，直接返回原数据
 */
public class GsonResponseDeserializer implements JsonDeserializer<ResponseEntity> {

    public GsonResponseDeserializer(){

    }

    @Override
    public ResponseEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject response = json.getAsJsonObject();
//        if (response.get("result").isJsonPrimitive()) {// 是否为基本数据类型
//            int code = response.get("code").getAsInt();
//            String message = response.get("info").getAsString();
//            return new ResponseEntity(code, message);
//        }
        return new Gson().fromJson(json, typeOfT);
    }
}
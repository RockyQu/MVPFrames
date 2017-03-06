package com.tool.common.http.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.tool.common.http.ResponseEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * GsonConverterBodyFactory
 */
public class GsonConverterBodyFactory extends Converter.Factory {

    public static GsonConverterBodyFactory create() {
        GsonBuilder builder = new GsonBuilder().
                registerTypeAdapter(ResponseEntity.class, new GsonResponseDeserializer());
        return create(builder);
    }

    public static GsonConverterBodyFactory create(GsonBuilder builder) {
        return new GsonConverterBodyFactory(builder);
    }

    private final Gson gson;

    private GsonConverterBodyFactory(GsonBuilder builder) {
        if (builder == null) throw new NullPointerException("GsonBuilder == null");
        this.gson = builder.create();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
package org.kodluyoruz.moviedb.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class MyHttpService
{

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "f8eda68ce7177281335be5b4d338f093";
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    private final LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private final String endpoint;

    private MyHttpService(String endpoint)
    {
        this.endpoint = endpoint;
    }

    private String encode(String param)
    {
        try
        {
            return URLEncoder.encode(param, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return param;
        }
    }

    private String buildUrl()
    {
        StringBuilder url = new StringBuilder()
                .append(BASE_URL)
                .append(endpoint)
                .append("?api_key=")
                .append(API_KEY);

        for (Entry<String, String> entry : params.entrySet())
        {
            url
                    .append("&")
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        return url.toString();
    }

    public MyHttpService param(String name, String value)
    {
        params.put(name, encode(value));
        return this;
    }

    public String get() throws UnirestException
    {
        return Unirest
                .get(buildUrl())
                .asString()
                .getBody();
    }

    public <T extends Serializable> T getAs(Class<T> type) throws UnirestException
    {
        String response = get();
        return GSON.fromJson(response, type);
    }

    public static MyHttpService connect(String endpoint)
    {
        return new MyHttpService(endpoint);
    }

}

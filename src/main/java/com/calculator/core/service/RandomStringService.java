package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class RandomStringService {
    private static final String API_URL = "http://www.randomnumberapi.com/api/v1.0/randomstring";
    public String getRandomString() throws CalculatorException {
        try {
            String json = requestRandomString();
            Gson gson = new Gson();
            Type randomStringListType = new TypeToken<List<String>>() {}.getType();
            List<String> list = gson.fromJson(json, randomStringListType);

            return list.get(0);
        } catch (IOException err) {
            throw new CalculatorException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String requestRandomString()  throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}

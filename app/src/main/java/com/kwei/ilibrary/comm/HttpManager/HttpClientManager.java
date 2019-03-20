package com.kwei.ilibrary.comm.HttpManager;


import com.kwei.ilibrary.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientManager {

    private HttpClientManager() {
    }

    private static class Instance {
        public static HttpClientManager instance = new HttpClientManager();
    }

    public static HttpClientManager getInstance() {
        return Instance.instance;
    }

    public static void get(String url, HttpCallBack callBack) {
        HttpClientManager.getInstance()._get(url, callBack);
    }

    public static void post(String url, String body, HttpCallBack callBack) {
        HttpClientManager.getInstance()._post(url, body, callBack);
    }

    private void _get(String url, HttpCallBack callBack) {
        newThreadRequest(url, "GET", null, callBack);
    }

    public void _post(String url, String body, HttpCallBack callBack) {
        newThreadRequest(url, "POST", body, callBack);
    }

    private void newThreadRequest(final String url, final String method, final String body, final HttpCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = new HttpResponse();
                try {
                    response = sendRequest(url, method, body);
                    callBack.onSucceed(response);
                } catch (IOException e) {
                    callBack.onFailure(response, e);
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private HttpResponse sendRequest(String url, String method, String body) throws IOException {
        int timeout = 3;

        HttpResponse response = new HttpResponse();

        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        // set Request
        connection.setRequestMethod(method);
        // set Timeout
        connection.setConnectTimeout(timeout * 1000);
        connection.setReadTimeout(timeout * 1000);

        if (method.equals("POST")) {
            // set Output
            OutputStream out = connection.getOutputStream();
            out.write(body.getBytes("utf8"));
            out.flush();
            closeStream(out);
        }

        response.code = connection.getResponseCode();
        // get Input
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        response.body = builder.toString();

        closeStream(in);
        closeConnection(connection);
        return response;
    }

    private void closeStream(Closeable stream) {
        if (stream == null) return;
        try {
            stream.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    private void closeConnection(HttpURLConnection connection) {
        if (connection == null) return;
        try {
            connection.disconnect();
        } catch (Exception e) {
            // do nothing
        }
    }
}

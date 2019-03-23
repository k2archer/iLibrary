package com.kwei.ilibrary.comm.HttpManager;

import com.kwei.ilibrary.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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

    public static HttpResponse get(String url) throws IOException {
        return HttpClientManager.getInstance().sendRequest(url, "GET", null);
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
        int timeout = 5;

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
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        int rc = 0;
        while ((rc = in.read(buff, 0, 256)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        response.body = swapStream.toByteArray();

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

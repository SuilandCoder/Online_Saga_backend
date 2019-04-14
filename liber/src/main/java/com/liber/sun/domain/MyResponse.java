package com.liber.sun.domain;

import java.io.InputStream;

/**
 * Created by sunlingzhi on 2017/11/14.
 */
public class MyResponse {

    int contentLength;
    String content_Type;
    String content_Disposition;
    InputStream inputStream;

    public MyResponse(int contentLength, String content_Type, String content_Disposition, InputStream inputStream) {
        this.contentLength = contentLength;
        this.content_Type = content_Type;
        this.content_Disposition = content_Disposition;
        this.inputStream = inputStream;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContent_Type() {
        return content_Type;
    }

    public void setContent_Type(String content_Type) {
        this.content_Type = content_Type;
    }

    public String getContent_Disposition() {
        return content_Disposition;
    }

    public void setContent_Disposition(String content_Disposition) {
        this.content_Disposition = content_Disposition;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}

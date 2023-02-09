package com.example.hydrogen.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private String requestId;
    private Long serverTime;
    private String resultCode;
    private String displayMessage;
    private T data;

    public static <T> Result<T> of (String requestId, T data) {
        return (Result<T>) ok(requestId).data(data).build();
    }

    public static <T> ResultBuilder<T> ok(String requestId) {
        return (ResultBuilder<T>) defaultTime().requestId(requestId).resultCode("success");
    }

    public static <T> ResultBuilder<T> builder() { return new Result.ResultBuilder<T>();}

    public static <T> ResultBuilder<T> defaultTime() {
        return (ResultBuilder<T>) builder().serverTime(System.currentTimeMillis() / 1000L);
    }

    private static <T> ResultBuilder<T> bad(String requestId, String code, String message) {
        return (ResultBuilder<T>) defaultTime().requestId(requestId).resultCode(code).displayMessage(message);
    }

    public static <T> Result<T> failure(String requestId, String code, String message) {
        return (Result<T>) bad(requestId, code, message).build();
    }

    public static class ResultBuilder<T> {
        private String requestId;
        private long serverTime;
        private String resultCode;
        private String displayMessage;
        private T data;

        public ResultBuilder<T> requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public ResultBuilder<T> serverTime(long serverTime) {
            this.serverTime = serverTime;
            return this;
        }

        public ResultBuilder<T> resultCode(String resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public ResultBuilder<T> displayMessage(String displayMessage) {
            this.displayMessage = displayMessage;
            return this;
        }

        public ResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Result<T> build() {
            return new Result(this.requestId, this.serverTime, this.resultCode, this.displayMessage, this.data);
        }
    }
}

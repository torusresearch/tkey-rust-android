package com.web3auth.tkey.ThresholdKey.Common;


@SuppressWarnings("unused") // linter doesn't check T is required with extends and produces unchecked cast warnings instead
public abstract class Result<T> {
    private Result() {}

    public static final class Success<T> extends Result<T> {
        public T data;

        public Success(T data) {
            this.data = data;
        }

        public Success() {
            this.data = null;
        }
    }

    public static final class Error<T> extends Result<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}

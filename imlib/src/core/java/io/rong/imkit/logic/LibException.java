package io.rong.imkit.logic;

/**
 * Created by DragonJ on 14-9-16.
 */
public class LibException extends Exception {
    ErrorCode errorCode;

    public static enum ErrorCode {
        /**
         * 未知错误。
         */
        UNKNOWN(-1, "Unknown error."),

        /**
         * 服务器超时。
         */
        TIMEOUT(3001, "Server is timed out.");

        private int code;
        private String msg;

        /**
         * 构造函数。
         *
         * @param code 错误代码。
         * @param msg  错误消息。
         */
        ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        /**
         * 获取错误代码值。
         *
         * @return 错误代码值。
         */
        public int getValue() {
            return this.code;
        }

        /**
         * 获取错误消息。
         *
         * @return 错误消息。
         */
        public String getMessage() {
            return this.msg;
        }

        /**
         * 设置错误代码值。
         *
         * @param code 错误代码。
         * @return 错误代码枚举。
         */
        public static ErrorCode setValue(int code) {
            for (ErrorCode c : ErrorCode.values()) {
                if (code == c.getValue()) {
                    return c;
                }
            }

            return UNKNOWN;
        }
    }

    public LibException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public LibException(Throwable throwable) {
        super(throwable);
    }
}

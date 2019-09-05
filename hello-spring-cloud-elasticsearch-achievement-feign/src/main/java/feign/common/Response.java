package feign.common;

/**
 * Created by laiyuan on 2017/3/21.
 * 返回结果封装
 */
public class Response {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private Integer code;
    private Meta meta;
    private Object data;

    public Response success() {
        this.code = 20000;
        this.meta = new Meta(1,true, OK);
        return this;
    }

    public Response success(Object data) {
        this.code = 20000;
        this.meta = new Meta(1,true, OK);
        this.data = data;
        return this;
    }

    public Response success(int result,Object data) {
        this.code = 20000;
        this.meta = new Meta(result,true, OK);
        this.data = data;
        return this;
    }

    public Response success(int result, Object data, String message) {
        this.meta = new Meta(result,true,message);
        this.data = data;
        this.code = 20000;
        return this;
    }

    public Response failure() {
        this.meta = new Meta(0,false, ERROR);
        return this;
    }

    public Response failure(String message) {
        this.meta = new Meta(0,false, message);
        return this;
    }

    public Response failure(int result,String message) {
        this.meta = new Meta(result,false, message);
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }

    public class Meta {
        private int result;
        private boolean success;
        private String message;

        public Meta(int result,boolean success) {
            this.result = result;
            this.success = success;
        }

        public Meta(int result,boolean success, String message) {
            this.result = result;
            this.success = success;
            this.message = message;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public boolean getSuccess(){ return success;}

        public int getResult() {
            return result;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
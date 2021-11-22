package top.poloo.common.com;

/**
 * @Author: polo
 * @Date: 2021/11/22 9:44
 */
public enum Rcode {
    //成功
    SUCCESS(200,"成功"),
    //失败
    FAILED(500,"失败");

    public String getMsg(){
        return msg;
    }
    public Integer getCode(){
        return code;
    }

    Rcode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private final Integer code;
    private final String msg;
}

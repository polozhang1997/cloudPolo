package top.poloo.common.com;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: polo
 * @Date: 2021/10/27 16:08
 */
@Data
public class R<T>  implements Serializable {

    private static final long serialVersionUID = 3490211779130241508L;
    private Integer code;
    private String msg;
    private T data;
    private Boolean operation;



    public R() {
    }

    public static  <T> R<T>  feignOperation(Integer code,Boolean operation,String message,T t){
        R<T> r = new R();
        r.setCode(code);
        r.setOperation(operation);
        r.setMsg(message);
        r.setData(t);
        return r;
    }


}

package top.poloo.common.com;

import lombok.Data;
import lombok.experimental.Accessors;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.Serializable;

/**
 * @Author: polo
 * @Date: 2021/10/27 16:08
 */
@Data
@Accessors(chain = true)
public class R  implements Serializable {

    private static final long serialVersionUID = 3490211779130241508L;
    private Integer code;
    private String msg;
    private Object data;

    public static  R success(Object data){
        return new R().setCode(Rcode.SUCCESS.getCode()).setMsg(Rcode.SUCCESS.getMsg()).setData(data);
    }
    public static R success(String msg){
        return new R().setCode(Rcode.SUCCESS.getCode()).setMsg(msg).setData(null);
    }
    public static R failed(String msg){
        return new R().setCode(Rcode.FAILED.getCode()).setMsg(msg).setData(null);
    }

}

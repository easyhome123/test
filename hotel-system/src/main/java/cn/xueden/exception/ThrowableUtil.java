package cn.xueden.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**异常处理

 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)){
            throwable.printStackTrace();
            return sw.toString();
        }
    }
}

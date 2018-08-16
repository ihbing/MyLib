package louis.general.util;

import louis.general.bean.ResultBean;

public class ResultUtil {

    public static ResultBean<String> resultSuccess(String msg) {
        ResultBean<String> result = new ResultBean<String>();
        result.isOk = true;
        result.result = msg;
        return result;
    }

    public static ResultBean<String> resultError(String msg) {
        ResultBean<String> result = new ResultBean<String>();
        result.isOk = false;
        result.result = msg;
        return result;
    }
}


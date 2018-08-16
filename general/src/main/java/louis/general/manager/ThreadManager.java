package louis.general.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/7.
 * 描述    :用于本App线程管理，可以防止创建太多线程
 */

public class ThreadManager
{
    private static ExecutorService executorService;
    private static final int maxTheardNum=5;//限制创建线程个数
    static{
        if(maxTheardNum==1){
            executorService= Executors.newSingleThreadExecutor();
        }else{
            executorService= Executors.newFixedThreadPool(maxTheardNum);
        }
    }
    public static void execute(Runnable runnable){
        executorService.execute(runnable);
    }
}


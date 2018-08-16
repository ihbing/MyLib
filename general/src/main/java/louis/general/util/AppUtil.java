package louis.general.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import louis.general.bean.AppBean;

/**
 * Created by Louis on 2018/8/12.
 */

public class AppUtil {
    public static String path(Context context,String pkgName){
        for(AppBean appBean: appInfo(context)){
            if(appBean.pkgName.equals(pkgName)){
                return appBean.path;
            }
        }
        return null;
    }
    /**
     * 获取已安装应用信息（不包含系统自带）
     */
    private static ArrayList<AppBean> appInfo(Context context) {
        List<ApplicationInfo> apps = context.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        ArrayList<AppBean> infos = new ArrayList<AppBean>();
        for (ApplicationInfo info : apps) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                AppBean appInfo = new AppBean();
                appInfo.name = info.loadLabel(context.getPackageManager()).toString();
                appInfo.icon = info.loadIcon(context.getPackageManager());
                appInfo.path = info.sourceDir;
                appInfo.pkgName = info.packageName;
                infos.add(appInfo);
            }
        }
        return infos;
    }

}

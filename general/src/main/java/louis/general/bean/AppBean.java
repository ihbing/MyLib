package louis.general.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/4/7.
 */

public class AppBean implements Comparable<AppBean> {
    public String name;
    public String pkgName;
    public Drawable icon;
    public boolean disApp =false;
    public int priority=0;
    public String path;

    @Override
    public int compareTo( AppBean appBean) {
        return appBean.priority - priority;
    }
}

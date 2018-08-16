package louis.general.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import louis.general.R;
import louis.general.bean.AppBean;

/**
 * Created by Louis on 2018/7/18.
 */

public class AppListAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private List<AppBean> beans;
    private List<AppBean> back;
    private Filter filter;
    private OnItemListener ilistener;

    public AppListAdapter(Activity activity, List<AppBean> beans) {
        this.activity = activity;
        this.beans = beans;
        this.back = beans;
    }

    public void upData(List<AppBean> beans) {
        this.beans = beans;
        this.back = beans;
    }
    public void setItemListener(OnItemListener listener){
        this.ilistener =listener;
    }
    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(activity).inflate(R.layout.view_list_item_app, null);
        TextView name = view.findViewById(R.id.name);
        TextView pkgName = view.findViewById(R.id.pkgname);
        ImageView icon = view.findViewById(R.id.icon);
        final LinearLayout ll = view.findViewById(R.id.ll);
        final AppBean bean = beans.get(i);
        name.setText(bean.name);
        pkgName.setText(bean.pkgName);
        icon.setImageDrawable(bean.icon);
        //当一个item被创建时，返回创建的背景，名字，包名View
        if(ilistener!=null)ilistener.onCreate(bean,ll,name,pkgName);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ilistener !=null) ilistener.onClick(bean);
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) filter = new AppListFilter();
        return filter;
    }

    private class AppListFilter extends Filter {
        //定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<AppBean> beans;
            if (TextUtils.isEmpty(charSequence)) {
                beans = back;
            } else {
                beans = new ArrayList<AppBean>();
                //这里是对back进行过滤，对beans过滤会越来越少
                for (AppBean bean : back) {
                    //根据包名和app名过滤
                    if (bean.name.contains(charSequence) || bean.pkgName.contains(charSequence)) {
                        beans.add(bean);
                    }
                }
            }
            results.values = beans;
            results.count = beans.size();
            return results;
        }

        //通知适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            beans = (List<AppBean>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
    public interface OnItemListener {
        void onCreate(AppBean bean,LinearLayout ll,TextView name,TextView pkgName);
        void onClick(AppBean bean);
    }
}

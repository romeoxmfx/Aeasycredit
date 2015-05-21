
package com.aeasycredit.order.activitys;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.aeasycredit.order.R;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.tool.AeaCamera;
import com.aeasycredit.order.utils.AeaCommonUtils;
import com.aeasycredit.order.utils.AeaFileUtil;
import com.aeasycredit.order.utils.AeasySharedPreferencesUtil;
import com.aeasycredit.order.volley.VolleyError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {
    GridView gridView;
    ArrayList<String> dashBoardList;
    DashboardAdapter mAdapter;
    PopupWindow popupWindow;
    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.dashboard_grid);
        iv_logo = (ImageView) findViewById(R.id.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
//        getSupportActionBar().setIcon(R.drawable.back_actionbar_selector);
        getSupportActionBar().setTitle(R.string.login_title_zh);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));
        
//        List<String> list = new ArrayList<String>();
//        list.add(AeasySharedPreferencesUtil.getUserCode(this));
//        ActionBarListAdapter adapter = new ActionBarListAdapter(list);
//        getSupportActionBar().setNavigationMode(ActionBar.);
//        getSupportActionBar().setListNavigationCallbacks(adapter, null);
        buildDashboard();
    }
    
    public class ActionBarListAdapter extends BaseAdapter{
        public List<String> list;
        
        public ActionBarListAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater v = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = v.inflate(R.layout.home_login_list_item, null);
            
            TextView tv = (TextView) convertView.findViewById(R.id.home_login_name);
            Button bt = (Button) convertView.findViewById(R.id.home_logout);
            String str = list.get(position);
            tv.setText(str);
            bt.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
            return convertView;
        }
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("userInfo")
        .setIcon(R.drawable.btn_login_icon_selector)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 0:
//                Toast.makeText(HomeActivity.this, "asdf", 1).show();
                showActionbarList();
                break;

            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    public void logout(){
        AeasySharedPreferencesUtil.clearUUid(this);
        AeasySharedPreferencesUtil.clearUserCode(this);
        //删除所有照片
//        AeaFileUtil.delFolder(Environment.getExternalStorageDirectory()
//                + "/" + AeaCamera.baselocalTempImgDir);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void buildDashboard() {
        dashBoardList = new ArrayList<String>();
        dashBoardList.add("考察任务");
        dashBoardList.add("尚未开发");
        dashBoardList.add("尚未开发");
        dashBoardList.add("尚未开发");

        mAdapter = new DashboardAdapter(dashBoardList, this);
        gridView.setAdapter(mAdapter);

        // gridView.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id) {
        // openListByPosition(position);
        // }
        // });
    }

    public void openListByPosition(int id) {
        switch (id) {
            case 0:
                // 打开考察任务列表
                Intent intent = new Intent();
                intent.setClass(this, InspectTaskList.class);
                startActivity(intent);
                break;
            case 1:
                // test
//                byte[] igbyte = AeasyRequestUtil.compressImage(BitmapFactory.decodeFile(Environment
//                        .getExternalStorageDirectory()
//                        + "/"
//                        + "aeasy"
//                        + "/"
//                        + "taskid142855997461266371000"
//                        + "/"
//                        + "20150409021431.jpg"));
//
//                File file = new File(Environment.getExternalStorageDirectory()
//                        + "/" + "aeasy" + "/" + "taskid142855997461266371000" + "/"
//                        + "20150409021431_compress.jpg");
//                if (file.exists()) {
//                    file.delete();
//                }
//                try {
//                    FileOutputStream out = new FileOutputStream(file);
//                    out.write(igbyte);
//                    out.close();
//                } catch (Exception e) {
//                }
                break;
            default:
                break;
        }
    }

    class DashboardAdapter extends BaseAdapter {
        private ArrayList<String> buttons;
        Context context;

        public DashboardAdapter(ArrayList<String> list, Context context) {
            buttons = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return buttons.size();
        }

        @Override
        public String getItem(int position) {
            return buttons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            public Button icon;
            public TextView textView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                v = vi.inflate(R.layout.dashboarditem, null);
                holder = new ViewHolder();
                holder.icon = (Button) v.findViewById(R.id.dashboard_home_btn_analytics);
                holder.textView = (TextView) v.findViewById(R.id.dashboard_home_text);
                holder.icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        openListByPosition(position);
                    }
                });
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.textView.setText(buttons.get(position));
            switch (position) {
                case 1:
                    holder.icon.setBackgroundResource(R.drawable.inspect_undeveloped_1);
                    break;
                case 2:
                    holder.icon.setBackgroundResource(R.drawable.inspect_undeveloped_2);
                    break;
                case 3:
                    holder.icon.setBackgroundResource(R.drawable.inspect_undeveloped_3);
                    break;
                default:
                    break;
            }
            return v;
        }
    }
    
    private void showActionbarList(){
        if(null == popupWindow){
            View layout = LayoutInflater.from(this).inflate(R.layout.home_login_list_item, null);
            TextView tv = (TextView) layout.findViewById(R.id.home_login_name);
            tv.setText("工号: " + AeasySharedPreferencesUtil.getUserCode(HomeActivity.this));
            Button btLogout = (Button) layout.findViewById(R.id.home_logout);
            btLogout.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
            int w = iv_logo.getWidth();
            int h = iv_logo.getHeight();
            popupWindow = new PopupWindow(layout, AeaCommonUtils.dip2px(this, 220), AeaCommonUtils.dip2px(this, 40), true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
        }
        int[] pos = new int[2];
        iv_logo.getLocationOnScreen(pos);
        popupWindow.showAtLocation(iv_logo, Gravity.RIGHT|Gravity.TOP, pos[0], pos[1]);
    }

    @Override
    public void onResponse(RequestWrapper response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}

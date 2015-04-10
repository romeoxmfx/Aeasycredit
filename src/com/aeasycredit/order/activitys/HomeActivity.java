
package com.aeasycredit.order.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.aeasycredit.order.R;
import com.aeasycredit.order.R.id;
import com.aeasycredit.order.R.layout;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.volley.VolleyError;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class HomeActivity extends BaseActivity {
    GridView gridView;
    ArrayList<String> dashBoardList;
    DashboardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.dashboard_grid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.login_title_zh);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));
        buildDashboard();
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
                byte[] igbyte = AeasyRequestUtil.compressImage(BitmapFactory.decodeFile(Environment
                        .getExternalStorageDirectory()
                        + "/"
                        + "aeasy"
                        + "/"
                        + "taskid142855997461266371000"
                        + "/"
                        + "20150409021431.jpg"));

                File file = new File(Environment.getExternalStorageDirectory()
                        + "/" + "aeasy" + "/" + "taskid142855997461266371000" + "/"
                        + "20150409021431_compress.jpg");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(igbyte);
                    out.close();
                } catch (Exception e) {
                }
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

            holder.icon.setText(buttons.get(position));
            return v;
        }
    }

    @Override
    public void onResponse(RequestWrapper response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}

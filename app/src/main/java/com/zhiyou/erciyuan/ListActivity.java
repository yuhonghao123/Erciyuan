package com.zhiyou.erciyuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        this.userListView = findViewById(R.id.user_list);

        /*Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);*/
        getDynamicList();
    }

    private void getDynamicList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*Map<String, Object> user = new HashMap<>();
                user.put("userId", userId);
                user.put("username", username);*/
                Result<List> dynamicListResult = OKHttpUtils.post("/dynamicList", null,null, List.class);
                showUser(dynamicListResult);
            }
        }).start();
    }

    private void showUser(final  Result<List> dynamicListResult ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dynamicListResult.isSuccess()) {
                    UserAdapter adapter = new UserAdapter(ListActivity.this, R.layout.dynamic_item, dynamicListResult.getData());
                    userListView.setAdapter(adapter);
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent_user=new Intent(ListActivity.this,DynamicActivity.class);
                            Map dynamic = (Map) dynamicListResult.getData().get(position);
                            String dynamicIdStr = String.valueOf(dynamic.get("dynamicId"));
                            Double dynamicIdDouble = Double.parseDouble(dynamicIdStr);
                            int dynamicIdInt = dynamicIdDouble.intValue();
                            //intent_user.putExtra("userId", userId);
                            intent_user.putExtra("dynamicId",dynamicIdInt);
                            startActivity(intent_user);
                        }
                    });
                }
            }
        });
    }

    static class UserAdapter extends ArrayAdapter<Map> {

        private int resourceId;

        //将上下文、ListView子项布局的id、数据 传递进来
        public UserAdapter(Context context, int textViewResourceId, List<Map> users){
            super(context, textViewResourceId, users);
            resourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Map dynamic = getItem(position);//获取当前项的Weather实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            TextView name = (TextView) view.findViewById(R.id.username);
            ImageView imageView = view.findViewById(R.id.user_avatar);

            imageView.setImageResource(R.drawable.ic_launcher_background);

            name.setText(String.valueOf(dynamic.get("title")));
            return view;
        }
    }
}

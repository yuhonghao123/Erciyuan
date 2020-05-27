package com.zhiyou.erciyuan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicActivity extends AppCompatActivity {

    private int dynamicId;
    private TextView show_view;
    private Button adoptButton;
    private Button failButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        adoptButton=findViewById(R.id.adopt);
        failButton=findViewById(R.id.fail);

        this.dynamicId = getIntent().getIntExtra("dynamicId", -1);
        getdynamicPo();

        adoptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adoptDynamicType();
            }
        });

        failButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failDynamicType();
            }
        });
    }

    private void getdynamicPo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> dynamic = new HashMap<>();
                dynamic.put("dynamicId", dynamicId);
                Result<Map> dynamicResult = OKHttpUtils.post("/getDetailsDynamic", null,dynamic, Map.class);
                showUser(dynamicResult);
            }
        }).start();
    }

    private void adoptDynamicType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> dynamic = new HashMap<>();
                String dynamic_type="审核已通过";
                dynamic.put("dynamicId", dynamicId);
                dynamic.put("dynamic_type", dynamic_type);
                Result<Map> dynamicResult = OKHttpUtils.post("/updateDynamicType", null,dynamic, Map.class);
                show(dynamicResult);
            }
        }).start();
    }

    private void failDynamicType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> dynamic = new HashMap<>();
                String dynamic_type="审核未通过";
                dynamic.put("dynamicId", dynamicId);
                dynamic.put("dynamic_type", dynamic_type);
                Result<Map> dynamicResult = OKHttpUtils.post("/updateDynamicType", null,dynamic, Map.class);
                show(dynamicResult);
            }
        }).start();
    }

    private void show(final Result<Map> dynamicResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dynamicResult.isSuccess()) {
                    Map dynamic=dynamicResult.getData();
                    new AlertDialog.Builder(DynamicActivity.this)
                            .setMessage((String)dynamic.get("dynamic_type"))
                            .show();
                    show_view.setText("");

                    String dynamicIdStr = String.valueOf(dynamic.get("dynamicId"));
                    Double dynamicIdDouble = Double.parseDouble(dynamicIdStr);
                    int dynamicIdInt = dynamicIdDouble.intValue();
                    String str_dynamicId="Id："+dynamicIdInt;
                    show_view.append(str_dynamicId+"\n");

                    String title=String.valueOf(dynamic.get("title"));
                    String str_title="标题："+title;
                    show_view.append(str_title+"\n");

                    String thumbnailIdStr = String.valueOf(dynamic.get("thumbnailId"));
                    Double thumbnailIdDouble = Double.parseDouble(thumbnailIdStr);
                    int thumbnailIdInt = thumbnailIdDouble.intValue();
                    String str_thumbnailId="图片："+thumbnailIdInt;
                    show_view.append(str_thumbnailId+"\n");

                    String content=String.valueOf(dynamic.get("content"));
                    String str_content="内容："+content;
                    show_view.append(str_content+"\n");

                    String username=String.valueOf(dynamic.get("username"));
                    String str_username="作者名称："+username;
                    show_view.append(str_username+"\n");

                    String tag=String.valueOf(dynamic.get("tag"));
                    String str_tag="标签："+tag;
                    show_view.append(str_tag+"\n");

                    String time=String.valueOf(dynamic.get("time"));
                    String str_time="发布时间："+time;
                    show_view.append(str_time+"\n");

                    String dynamic_type=String.valueOf(dynamic.get("dynamic_type"));
                    String str_dynamic_type="审核状态："+dynamic_type;
                    show_view.append(str_dynamic_type);
                }
            }
        });
    }

    private void showUser(final Result<Map> dynamicResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dynamicResult.isSuccess()) {
                    show_view=findViewById(R.id.show_view);
                    Map dynamic=dynamicResult.getData();

                    String dynamicIdStr = String.valueOf(dynamic.get("dynamicId"));
                    Double dynamicIdDouble = Double.parseDouble(dynamicIdStr);
                    int dynamicIdInt = dynamicIdDouble.intValue();
                    String str_dynamicId="Id："+dynamicIdInt;
                    show_view.append(str_dynamicId+"\n");

                    String title=String.valueOf(dynamic.get("title"));
                    String str_title="标题："+title;
                    show_view.append(str_title+"\n");

                    String thumbnailIdStr = String.valueOf(dynamic.get("thumbnailId"));
                    Double thumbnailIdDouble = Double.parseDouble(thumbnailIdStr);
                    int thumbnailIdInt = thumbnailIdDouble.intValue();
                    String str_thumbnailId="图片："+thumbnailIdInt;
                    show_view.append(str_thumbnailId+"\n");

                    String content=String.valueOf(dynamic.get("content"));
                    String str_content="内容："+content;
                    show_view.append(str_content+"\n");

                    String username=String.valueOf(dynamic.get("username"));
                    String str_username="作者名称："+username;
                    show_view.append(str_username+"\n");

                    String tag=String.valueOf(dynamic.get("tag"));
                    String str_tag="标签："+tag;
                    show_view.append(str_tag+"\n");

                    String time=String.valueOf(dynamic.get("time"));
                    String str_time="发布时间："+time;
                    show_view.append(str_time+"\n");

                    String dynamic_type=String.valueOf(dynamic.get("dynamic_type"));
                    String str_dynamic_type="审核状态："+dynamic_type;
                    show_view.append(str_dynamic_type);
                }
            }
        });
    }
}

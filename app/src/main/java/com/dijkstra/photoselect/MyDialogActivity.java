package com.dijkstra.photoselect;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @Description:
 * @Author: maoshenbo
 * @Date: 2019/5/28 11:33 AM
 * @Version: 1.0
 */
public class MyDialogActivity extends Activity implements View.OnClickListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_pick_photo = findViewById(R.id.btn_pick_photo);
        btn_cancel = findViewById(R.id.btn_cancel);

        layout = findViewById(R.id.pop_layout);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                break;
            case R.id.btn_pick_photo:
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();
    }
}

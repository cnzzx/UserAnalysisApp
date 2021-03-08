package com.example.useranalysisapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.useranalysisapp.R;
import com.example.useranalysisapp.model.EmotionCausePair;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * 需要调整情感显示位置！！
 */
public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";

    private static final int lineWidth = 36; // 展示时一行的最大字符数

    /**
     * 情感原因对数组，注意排序
     * <p>假定情感种类不会太多</p>
     */
    private EmotionCausePair[] ecpArray = new EmotionCausePair[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //透明度渐增动画
        /*Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(500);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        this.show();

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button detailButton = findViewById(R.id.detail_button);
        PopupWindow popupWindow = initPopupWindow();
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //显示方式一：在窗口的任意位置进行显示
                WindowManager manager = getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int width = outMetrics.widthPixels;
                int height = outMetrics.heightPixels;

                // 设置窗口出现的位置（任意位置的窗口）
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0,
                        0);
            }
        });
    }

    /**
     * 初始化PopupWindow，用于显示详细的情感-原因信息
     *
     * @return 返回构造好的信息弹窗
     */
    private PopupWindow initPopupWindow(){
        TextView causeText = new TextView(ReportActivity.this);
        causeText.setTextColor(ContextCompat.getColor(this, R.color.causeTextColor));
        causeText.setTextSize(18);
        String causeString = "";
        for(int i=0; i<5; i++){
            //只显示排名前五的情感
            if(ecpArray[i].getEmotion().equals("unknown")){
                break;
            }
            if(i != 0) causeString += "\n";
            causeString += "心情：" + ecpArray[i].getEmotion() + "\n";
            LinkedList<String>causes = ecpArray[i].getCause();
            causeString += "        " + "小短句：" + "\n";
            int count = 0;
            for(String cause : causes) {
                causeString += "        " + cause + "\n";
                count++;
                if(count == 10) break;//只显示前十个原因句
            }
        }
        causeText.setText(causeString);

        //创建滚动视图，以免展示内容太长，单页无法容纳
        ScrollView sv = new ScrollView(ReportActivity.this);
        sv.addView(causeText);
        sv.setAlpha(0.6f);
        sv.setBackgroundColor(ContextCompat.getColor(this, R.color.causeBoardColor));

        //获取屏幕尺寸，并适当缩放得到信息窗大小
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = (int)(outMetrics.widthPixels * 0.95);
        int height = (int)(outMetrics.heightPixels * 0.8);

        PopupWindow returnPopupWindow = new PopupWindow(sv,width,height);//参数为1.View 2.宽度 3.高度
        returnPopupWindow.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow

        return returnPopupWindow;
    }

    /**
     * 情感原因展示
     */
    private void show(){
        LinearLayout contentLayout = findViewById(R.id.report_body);
        initializeEcpArray();
        for(int i=0;i<5;i++){
            //只显示排名前五的情感
            String showString = "";
            if(ecpArray[i].getEmotion().equals("unknown")){
                break;
            }
            showString += formalize(ecpArray[i].getEmotion(),ecpArray[i].getCause().size());
            TextView tempText = new TextView(this);
            tempText.setText(showString);
            tempText.setTextSize(25);
            tempText.setGravity(Gravity.CENTER);
            tempText.setTextColor(ContextCompat.getColor(this, R.color.emotionTextColor));
            contentLayout.addView(tempText);
        }
    }

    /**
     * 初始化情感原因对数组（目前仅仅是测试）
     */
    private void initializeEcpArray(){
        for(int i=0;i<10;i++) ecpArray[i] = new EmotionCausePair();
        ecpArray[0] = new EmotionCausePair("爽", new String[]{"哈哈哈", "呵呵呵", "嘿嘿嘿"});
        ecpArray[1] = new EmotionCausePair("快乐", new String[]{"I feel good", "What a good weather", "I love cakes", "快乐", "快乐", "快乐", "快乐", "快乐", "快乐", "快乐", "快乐"});
        ecpArray[2] = new EmotionCausePair("郁闷", new String[]{"Such a bad weather", "I dislike cakes", "He's stupid", "I feel sick"});
        ecpArray[3] = new EmotionCausePair("一般般", new String[]{"just so so", "哦", "斐波那契数列", "莫比乌斯反演"});
        ecpArray[4] = new EmotionCausePair("一般般", new String[]{"just so so", "哦", "斐波那契数列", "莫比乌斯反演"});
        ecpArray[5] = new EmotionCausePair("一般般", new String[]{"just so so", "哦", "斐波那契数列", "莫比乌斯反演"});
        ecpArray[6] = new EmotionCausePair("一般般", new String[]{"just so so", "哦", "斐波那契数列", "莫比乌斯反演"});
        ecpArray[7] = new EmotionCausePair("一般般", new String[]{"just so so", "哦", "斐波那契数列", "莫比乌斯反演"});/**/

        Arrays.sort(ecpArray, EmotionCausePair.ecpCompare); //按原因数逆序排列
    }

    /**
     * 把给定的情感名称和情感对应的原因数目按照漂亮的格式整理一下。
     *
     * @param emotion 情感名称
     * @param freq    情感对应的原因数目
     * @return 返回整理后的一行（不带换行符）
     */
    private String formalize(String emotion, int freq){
        String result = emotion + String.valueOf(freq);
        int currenLength = emotion.length()*4 + getLength(freq)*2;
        Log.e(TAG, String.valueOf(currenLength));
        int insertPos = emotion.length(); //用于插入空格的位置
        while(currenLength < lineWidth){
            result = result.substring(0,insertPos) + " " + result.substring(insertPos,result.length());
            currenLength++;
        }
        // result += "\n";
        return result;
    }

    /**
     * 获取非负整数占用的位数
     * @param figure 要计算位数的非负整数
     * @return 返回位数
     */
    private int getLength(int figure){
        if(figure <0 ) return 0; //Invalid argument.
        if(figure == 0) return 1;
        int answer = 0;
        while(figure > 0){
            figure /= 10;
            answer++;
        }
        return answer;
    }
}
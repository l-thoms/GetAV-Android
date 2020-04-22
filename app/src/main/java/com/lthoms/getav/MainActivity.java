package com.lthoms.getav;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityManagerCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    android.widget.Button buttonDown;
    EditText TextBV;
    EditText TextAV;
    android.widget.Button buttonUp;
    android.widget.Button CopyAV;
    android.widget.Button CopyBV;
    android.widget.Button Thanks;
    android.widget.Button ButtonLeft;
    private android.content.ClipboardManager myClipboard;
    java.lang.String Table="fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
    @Override protected void onCreate(Bundle savedInstanceState)
    { setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        buttonDown= findViewById(R.id.buttonDown);
        TextBV= findViewById(R.id.editBV);
        TextAV= findViewById(R.id.editAV);
        CopyAV = findViewById(R.id.buttonCopyAV);
        buttonUp=findViewById(R.id.buttonUP);
        CopyBV=findViewById(R.id.buttonCopyBV);
        Thanks=findViewById(R.id.Thanks);
        ButtonLeft=new Button(MainActivity.this);

        buttonDown.setOnClickListener(new View.OnClickListener()//BV转AV
        {
            @SuppressLint("ResourceType")
            @Override public void onClick(View v)
            {
                InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                try
                {
                    String Input=TextBV.getText().toString();
                    switch (Input.length()) //自动加入前缀
                    {
                        case 9:Input="001"+Input;break;
                        case 10:Input="00"+Input;break;
                        case 11:Input="0"+Input;break;
                    }
                    String result = String.valueOf(GetAV(Input));
                    if(GetAV(Input)<0) throw new Exception("");
                    TextAV.setText("AV"+result);
                    TextAV.setSelection(TextAV.getText().length());
                    final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "转换成功。", Snackbar.LENGTH_LONG);
                    SnackbarAddView(snackbar,R.layout.twosnackbar,1);
                    SetAction(snackbar,R.id.cancel_btn,"打开",new View.OnClickListener() 
					{
                        @Override public void onClick(View v) 
						{
                            final Uri Link = Uri.parse("https://www.bilibili.com/video/"+TextAV.getText().toString().toLowerCase());
                            Intent I=new Intent(Intent.ACTION_VIEW,Link);
                            startActivity(I);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setAction("复制", new View.OnClickListener() 
					{
                        @Override public void onClick(View v) {
                            CopyAV.callOnClick();
                        }

                    });
                    snackbar.setDuration(5000);
                    snackbar.show();

                }
                catch (Exception e)
                {
                    Snackbar.make(getWindow().getDecorView(), "转换失败!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        CopyAV.setOnClickListener(new View.OnClickListener() //复制AV号
        {
            @Override public void onClick(View v)
            {myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if(TextAV.getText().length()>0)
                {

                    ClipData c;
                    c = ClipData.newPlainText("text", TextAV.getText());
                    myClipboard.setPrimaryClip(c);
                    Toast.makeText(getApplicationContext(), "已复制。", Toast.LENGTH_SHORT).show();
                }
                else
                {
					try
					{
						ClipData c = myClipboard.getPrimaryClip();
						ClipData.Item item = c.getItemAt(0);
						TextAV.setText(item.getText().toString());
					}catch (Exception e){;}
                }
            }
        });
        CopyBV.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                myClipboard = (android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                if(TextBV.length()>0)
                {
					ClipData c;
					c= ClipData.newPlainText("text",TextBV.getText());
					myClipboard.setPrimaryClip(c);
					Toast.makeText(getApplicationContext(), "已复制。", Toast.LENGTH_SHORT).show();
                }
                else
                {
					try
					{
						ClipData c = myClipboard.getPrimaryClip();
						ClipData.Item item = c.getItemAt(0);
						TextBV.setText(item.getText().toString());
					}catch (Exception e){;}
				}
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() //AV转BV
        {
            @Override public void onClick(View v)
            {InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                try
                {
                    String Input=TextAV.getText().toString();
                    long Number = Long.parseLong(Pattern.compile("[^0-9]").matcher(Input).replaceAll("").trim());
                    TextBV.setText(GetBV(Number));
                    TextBV.setSelection(TextBV.getText().length());
                    final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "转换成功。", Snackbar.LENGTH_LONG);
                    SnackbarAddView(snackbar,R.layout.twosnackbar,1);
                    SetAction(snackbar,R.id.cancel_btn,"打开",new View.OnClickListener() 
					{
                        @Override public void onClick(View v) 
						{
                            final Uri Link = Uri.parse("https://www.bilibili.com/video/"+TextBV.getText());
                            Intent I=new Intent(Intent.ACTION_VIEW,Link);
                            startActivity(I);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setAction("复制", new View.OnClickListener() 
					{
                        @Override public void onClick(View v) 
						{
                            CopyBV.callOnClick();
                        }

                    });
                    snackbar.setDuration(5000);
                    snackbar.show();
                }
                catch (Exception e)
                {
                    Snackbar.make(getWindow().getDecorView(), "转换失败!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        final AlertDialog.Builder ThanksDialog= new AlertDialog.Builder(this);
        Thanks.setOnClickListener(new View.OnClickListener() //参考资料
        {
            @Override public void onClick(View v)
            {
                ThanksDialog.setTitle("参考资料");
                ThanksDialog.setItems(new String[]{"BV转AV","AV转BV"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                final Uri Link = Uri.parse("https://www.bilibili.com/video/av99160403");
                                Intent I=new Intent(Intent.ACTION_VIEW,Link);
                                startActivity(I);
                                break;
                            case 1:
                                final Uri link = Uri.parse("https://www.bilibili.com/video/av98869161");
                                Intent i=new Intent(Intent.ACTION_VIEW,link);
                                startActivity(i);
                                break;
                        }
                    }
                });
                ThanksDialog.show();
            }
        });
        if(TextAV.getText().length()==0)CopyAV.setText("粘贴");
        TextAV.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }@Override public void afterTextChanged(Editable s) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
if(TextAV.getText().length()==0)CopyAV.setText("粘贴");
else CopyAV.setText("复制");
            }
        });

        if(TextBV.getText().length()==0)CopyBV.setText("粘贴");
        TextBV.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }@Override public void afterTextChanged(Editable s) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextBV.getText().length()==0)CopyBV.setText("粘贴");
                else CopyBV.setText("复制");
            }
        });

    }

    long GetAV(String BV) //获取AV号核心算法
    {
        String Value = BV.substring(2);
        long num1= (long)((Table.indexOf(Value.charAt(0)))*Math.pow(58,6));
        long num2= (long)((Table.indexOf(Value.charAt(1)))*Math.pow(58,2));
        long num3= (long)((Table.indexOf(Value.charAt(2)))*Math.pow(58,4));
        long num4= (long)((Table.indexOf(Value.charAt(3)))*Math.pow(58,8));
        long num5= (long)((Table.indexOf(Value.charAt(4)))*Math.pow(58,5));
        long num6= (long)((Table.indexOf(Value.charAt(5)))*Math.pow(58,9));
        long num7= (long)((Table.indexOf(Value.charAt(6)))*Math.pow(58,3));
        long num8= (long)((Table.indexOf(Value.charAt(7)))*Math.pow(58,7));
        long num9= (long)((Table.indexOf(Value.charAt(8)))*Math.pow(58,1));
        long num10= (long)((Table.indexOf(Value.charAt(9)))*Math.pow(58,0));
        long sum =num1+num2+num3+num4+num5+num6+num7+num8+num9+num10;
        long minus=sum - 100618342136696320L;
        long answer = minus ^ 177451812 ;
        return answer;
    }
    String GetBV(Long AV) //获取BV号核心算法
    {
        AV=(AV^177451812 )+100618342136696320L;
        String[] BuildString=new String[10] ;
        for(int i=0;i<10;i++)
        {
            long Medium = AV/(long)(Math.pow(58,i)) % 58 ;
            BuildString[i]=String.valueOf(Table.charAt((int)Medium));
        }
        String Final="BV"+BuildString[6]+BuildString[2]+BuildString[4]+BuildString[8]+BuildString[5]+BuildString[9]+BuildString[3]+BuildString[7]+BuildString[1]+BuildString[0];
        return Final;
    }
    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View snackbarview = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;
        snackbarLayout.setForegroundGravity(Gravity.END);
        View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        p.gravity=Gravity.END;
        snackbarLayout.addView(add_view, index, p);
    }
    public static void SetAction(Snackbar snackbar, int btn_id, String action_string, View.OnClickListener onClickListener) {
        View view = snackbar.getView();
        if (view != null) {
            ((Button)view.findViewById(btn_id)).setText(action_string);
            (view.findViewById(btn_id)).setOnClickListener(onClickListener);
        }
    }
}


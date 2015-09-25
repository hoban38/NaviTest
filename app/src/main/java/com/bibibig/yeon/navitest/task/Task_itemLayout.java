package com.bibibig.yeon.navitest.task;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bibibig.yeon.navitest.R;

/**
 * Created by Administrator on 2015-06-21.
 */
public class Task_itemLayout extends LinearLayout {
    Context mcontext;
    LayoutInflater inflater;

    TextView idtext;
    TextView titletext;
    TextView notetext;

    public Task_itemLayout(Context context) {
        super(context);
        mcontext = context;
        init();
    }

    public Task_itemLayout(Context context, AttributeSet attrs, Context mcontext) {
        super(context, attrs);
        this.mcontext = mcontext;
        init();
    }
    private void init(){
        inflater  = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.task_item,this,true);

        idtext = (TextView) findViewById(R.id.idTEXT);
        titletext = (TextView) findViewById(R.id.titleTEXT);
        notetext = (TextView) findViewById(R.id.noteTEXT);
    }

   public void setidText(String id){
       try {
           idtext.setText(id);
       }catch(NullPointerException e){
           Log.e("Task_itemLayout",e.toString());
       }
    }
   public void settitleText(String title){
       try {
           titletext.setText(title);
       }catch(NullPointerException e){
           Log.e("Task_itemLayout",e.toString());
       }

   }
   public void setNotetext(String note){
       try {
        notetext.setText(note);
       }catch(NullPointerException e){
           Log.e("Task_itemLayout",e.toString());
        }
    }
}

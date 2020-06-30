package com.example.icetouch.dorest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class TestAdapter extends ArrayAdapter<Test> {
    public TestAdapter(Context context, int resource, List<Test> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Test Test = getItem(position);
        final View oneTestView = LayoutInflater.from(getContext()).inflate(R.layout.test_item, parent, false);
        ImageView imageView = (ImageView) oneTestView.findViewById(R.id.test_small_imageView);
        TextView textView = (TextView) oneTestView.findViewById(R.id.test_name_textView);

        imageView.setImageResource(Test.getImageId());
        textView.setText(Test.getName());
        textView.setTextColor(Color.rgb(255, 255, 255));

        oneTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TestActivity.class);
                intent.putExtra("test_name", Test.getName());
                intent.putExtra("test_id", Test.getId());
                intent.putExtra("test_num", Test.getNumId());
                intent.putExtra("test_word", Test.getTimeId());
                intent.putExtra("test_priority", Test.getPriority());
                intent.putExtra("test_imageArr", Test.getImageArray());
                intent.putExtra("test_ans", Test.getAnswer());
                intent.putExtra("test_order", Test.getOrder());
                getContext().startActivity(intent);
            }
        });
        return oneTestView;
    }
}
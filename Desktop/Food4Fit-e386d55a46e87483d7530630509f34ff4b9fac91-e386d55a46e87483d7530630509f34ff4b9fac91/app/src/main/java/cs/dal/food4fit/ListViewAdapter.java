package cs.dal.food4fit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by graceliu on 2017-11-19.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> data;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, ArrayList<Object> data) {

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getItemViewType(int position){
        if(data.get(position)instanceof String){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        switch(getItemViewType(i)){
            case 1:
                view = inflater.inflate(R.layout.list_item_header,null);
                TextView header = (TextView) view.findViewById(R.id.header);
                header.setText(data.get(i).toString());

                Log.i("set","this view has inflated. has setHeader");
                break;
            case 0:
                view = inflater.inflate(R.layout.list_item_layout,null);
                Recipe item = (Recipe) data.get(i);
                TextView imageitem_title = (TextView) view.findViewById(R.id.text);
                ImageView imageitem_image = (ImageView) view.findViewById(R.id.image);

                imageitem_title.setText(item.getName());
                imageitem_image.setImageBitmap(item.getPhoto());

                Log.i("set","this view has inflated. has setImage&Text");

                break;
        }

        Log.i("ViewLog","the view postion:"+i);
        return view;
    }



}
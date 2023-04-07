package com.example.projectui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ingredientAdapter extends BaseAdapter {
    ArrayList<ListViewAdapterData> list = new ArrayList<ListViewAdapterData>();
    ImageView recipe_image;
    Bitmap bitmap;

    public ingredientAdapter(){}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ingredient_list,viewGroup,false);

        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView ingredient_name = (TextView)view.findViewById(R.id.ingredient_name);

        //현재 포지션에 해당하는 아이템에 내용을 적용하기 위해 list배열에서 객체를 가져온다.
        ListViewAdapterData listdata = list.get(i);

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        ingredient_name.setText(listdata.getRECIPE_NM_KO());

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함
    public void addItem(String name){
        ListViewAdapterData listdata = new ListViewAdapterData();

        listdata.setRECIPE_NM_KO(name);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);

    }
}

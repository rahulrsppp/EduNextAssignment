package com.edunext.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edunext.R;
import com.edunext.model.SchoolModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolRecyclerViewAdapter extends RecyclerView.Adapter<SchoolRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<SchoolModel> schoolList;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_GRID = 2;
    private int currentType=1;
    public SchoolRecyclerViewAdapter(Context context) {
        this.context=context;
    }

    public void setSchoolList(List<SchoolModel> schoolList){
        this.schoolList=schoolList;
    }

    @NonNull
    @Override
    public SchoolRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        if(viewType==TYPE_LIST)
           view= layoutInflater.inflate(R.layout.row_school_recyclerview,parent,false);
        else
           view= layoutInflater.inflate(R.layout.row_school_recyclerview_grid,parent,false);

        return new ViewHolder(view);
    }

    public void setLayoutType(int currentType){
        this.currentType=currentType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(currentType==TYPE_LIST) {
            return TYPE_LIST;
        }else {
            return TYPE_GRID;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setView(holder);
    }

    private void setView(ViewHolder holder) {
        SchoolModel schoolModel= schoolList.get(holder.getAdapterPosition());

        if(schoolModel!=null) {
            String school_code=schoolModel.getSchool_code();
            String url=schoolModel.getUrl();
            holder.tv_school_code_text.setText(school_code!=null?school_code:"");
            holder.tv_school_url_text.setText(url!=null?url:"");

            Picasso.with(context).load("https://picsum.photos/200")
                    .error(R.mipmap.placeholder)
                    .placeholder(R.mipmap.placeholder)
                    .into(holder.iv_school);
        }
    }


    @Override
    public int getItemCount() {
        return schoolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_school)
        ImageView iv_school;

        @BindView(R.id.tv_school_code_text)
        TextView tv_school_code_text;

        @BindView(R.id.tv_school_url_text)
        TextView tv_school_url_text;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}

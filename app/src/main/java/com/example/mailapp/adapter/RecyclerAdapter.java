package com.example.mailapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.RecyclerViewItemClickListener;

import java.util.List;
import java.util.Objects;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> mdata;
    private RecyclerViewItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item
        TextView city, mailfrom, mailto, duedate;
        Button btn;

        ViewHolder(View v) {
            super(v);
            System.out.println("-------------------");
            System.out.println("Constructor ViewHolder");
            System.out.println("-------------------");
            city = v.findViewById(R.id.RecyclerCity);
            mailfrom = v.findViewById(R.id.RecyclerFrom);
            mailto = v.findViewById(R.id.RecyclerTo);
            duedate = v.findViewById(R.id.RecyclerDate);
            btn = v.findViewById(R.id.RecyclerBtn);

        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_test, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
//        //What's done when click on the text
//        v.setOnClickListener(view -> listener.onItemClick(view, viewHolder.getAdapterPosition()));
//        v.setOnLongClickListener(view -> {
//            listener.onItemLongClick(view, viewHolder.getAdapterPosition());
//            return true;
//        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mdata.get(position);
        if (item.getClass().equals(MailEntity.class)) {
            holder.city.setText(((MailEntity) item).getCity());
            holder.mailfrom.setText(((MailEntity) item).mailFrom);
            holder.mailto.setText(((MailEntity) item).mailTo);
            holder.duedate.setText(((MailEntity) item).shippedDate);
         }
    }

    /**
     * How many mail are displayed in home page
     * @return
     */
    @Override
    public int getItemCount() {
        if (mdata != null) {
            return mdata.size();
        } else {
            return 0;
        }
       // return 2;
    }

    public void setMdata(final List<T> data1) {
        if (mdata == null) {
            mdata = data1;
            notifyItemRangeInserted(0, data1.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerAdapter.this.mdata.size();
                }

                @Override
                public int getNewListSize() {
                    return data1.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mdata instanceof MailEntity) {
                        return ((MailEntity) mdata.get(oldItemPosition)).getIdMail() ==
                                ((MailEntity) mdata.get(newItemPosition)).getIdMail();
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (RecyclerAdapter.this.mdata instanceof MailEntity) {
                       MailEntity newMail = (MailEntity) mdata.get(newItemPosition);
                        MailEntity oldMail = (MailEntity) mdata.get(newItemPosition);
                        return Objects.equals(newMail.getIdMail(), oldMail.getIdMail());
                    }
                    return false;
                }
            });
            mdata = data1;
            result.dispatchUpdatesTo(this);
        }
    }
}

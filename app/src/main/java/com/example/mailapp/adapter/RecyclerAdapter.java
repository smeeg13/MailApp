package com.example.mailapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.RecyclerViewItemClickListener;

import java.util.List;
import java.util.Objects;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> data;
    private RecyclerViewItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item
        TextView city, mailfrom, mailto, duedate;
        ViewHolder(View v) {
            super(v);
            System.out.println("-------------------");
            System.out.println("-------------------");
            System.out.println("Constructor ViewHolder");
            System.out.println("-------------------");
            this.city = v.findViewById(R.id.city);
            this.mailfrom = v.findViewById(R.id.mailFrom);
            this.mailto = v.findViewById(R.id.mailTo);
            this.duedate = v.findViewById(R.id.shipDate);
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, null, false);
//        TextView city = v.findViewById(R.id.city);
//        final ViewHolder viewHolder = new ViewHolder(v);
//        //What's done when click on the text
//        v.setOnClickListener(view -> listener.onItemClick(view, viewHolder.getAdapterPosition()));
//        v.setOnLongClickListener(view -> {
//            listener.onItemLongClick(view, viewHolder.getAdapterPosition());
//            return true;
//        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
//        MailEntity item =(MailEntity) data.get(position);
//        holder.city.setText(item.city);
//        holder.mailfrom.setText(item.mailFrom);
//        holder.mailto.setText(item.mailTo);
//        holder.duedate.setText(item.shippedDate);
    }

    /**
     * How many mail are displayed in home page
     * @return
     */
    @Override
    public int getItemCount() {

            return 2;

    }

    public void setData(final List<T> data1) {
        if (data == null) {
            data = data1;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerAdapter.this.data.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (data instanceof MailEntity) {
                        return ((MailEntity)data.get(oldItemPosition)).getIdMail() ==
                                ((MailEntity)data.get(newItemPosition)).getIdMail();
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (RecyclerAdapter.this.data instanceof MailEntity) {
                       MailEntity newMail = (MailEntity) data.get(newItemPosition);
                        MailEntity oldMail = (MailEntity) data.get(newItemPosition);
                        return Objects.equals(newMail.getIdMail(), oldMail.getIdMail());
                    }
                    return false;
                }
            });
            this.data = data1;
            result.dispatchUpdatesTo(this);
        }
    }
}

package com.example.mailapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class RecyclerAdapterForAll<T> extends RecyclerView.Adapter<RecyclerAdapterForAll.ViewHolder> {

    static Animation open, close, toright,fromright;

    private List<T> mdata;
    private RecyclerViewItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each view item in each row
        TextView city, address, mailto, duedate, statusEntry;
        FloatingActionButton moreBtn, doneBtn, editBtn;
        boolean isOpen = false;

        ViewHolder(View v) {
            super(v);
            System.out.println("-------------------");
            System.out.println("Constructor ViewHolder");
            System.out.println("-------------------");
            city = v.findViewById(R.id.RecyclerCity);
            address = v.findViewById(R.id.RecyclerAddress);
            mailto = v.findViewById(R.id.RecyclerTo);
            duedate = v.findViewById(R.id.RecyclerDate);
            statusEntry = v.findViewById(R.id.statusEntry);

            moreBtn = v.findViewById(R.id.RecyclerMoreButton);
            doneBtn = v.findViewById(R.id.RecyclerDoneButton);
            editBtn = v.findViewById(R.id.RecyclerEditButton);
        }
    }

    public RecyclerAdapterForAll(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerAdapterForAll( ) {
    }

    @Override
    public RecyclerAdapterForAll.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        open = AnimationUtils.loadAnimation(parent.getContext(), R.anim.rotate_open_anim);
        close = AnimationUtils.loadAnimation(parent.getContext(), R.anim.rotate_close_anim);
        fromright = AnimationUtils.loadAnimation(parent.getContext(), R.anim.from_right_anim);
        toright = AnimationUtils.loadAnimation(parent.getContext(), R.anim.to_right_anim);

        viewHolder.moreBtn.setOnClickListener(view -> {
            if (!viewHolder.isOpen){
                viewHolder.doneBtn.setVisibility(View.VISIBLE);
                viewHolder.editBtn.setVisibility(View.VISIBLE);
                viewHolder.doneBtn.startAnimation(fromright);
                viewHolder.editBtn.startAnimation(fromright);
                viewHolder.moreBtn.startAnimation(open);
                viewHolder.doneBtn.setClickable(true);
                viewHolder.editBtn.setClickable(true);
            }
            else {
                viewHolder.doneBtn.setVisibility(View.INVISIBLE);
                viewHolder.editBtn.setVisibility(View.INVISIBLE);
                viewHolder.doneBtn.startAnimation(toright);
                viewHolder.editBtn.startAnimation(toright);
                viewHolder.moreBtn.startAnimation(close);
                viewHolder.doneBtn.setClickable(false);
                viewHolder.editBtn.setClickable(false);
            }
            viewHolder.isOpen= !viewHolder.isOpen;
        });

        viewHolder.moreBtn.setOnLongClickListener(view -> {
            listener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });

        viewHolder.editBtn.setOnClickListener(view -> listener.onItemClick("edit",view, viewHolder.getAdapterPosition()));
        viewHolder.doneBtn.setOnClickListener(view -> listener.onItemClick("done",view, viewHolder.getAdapterPosition()));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterForAll.ViewHolder holder, int position) {
        T item = mdata.get(position);
        if (item.getClass().equals(MailEntity.class)) {
            holder.city.setText(((MailEntity) item).getCity());
            holder.address.setText(((MailEntity) item).getAddress());
            holder.mailto.setText(((MailEntity) item).getMailTo());
            holder.duedate.setText(((MailEntity) item).getShippedDate());
            holder.statusEntry.setText(((MailEntity) item).getStatus());
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
    }

    public void setMdata(final List<T> data1) {
        if (mdata == null) {

            mdata = data1;
            notifyItemRangeInserted(0, data1.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerAdapterForAll.this.mdata.size();
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
                    if (RecyclerAdapterForAll.this.mdata instanceof MailEntity) {
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

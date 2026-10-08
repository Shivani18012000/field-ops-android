package com.shivani.fieldops.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shivani.fieldops.databinding.ItemWorkOrderBinding;
import com.shivani.fieldops.domain.WorkOrder;
import java.util.ArrayList;
import java.util.List;

/** Java RecyclerView adapter intentionally included to demonstrate Kotlin/Java interoperability. */
public final class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderHolder> {
    private final List<WorkOrder> items = new ArrayList<>();

    public void submitList(List<WorkOrder> updated) {
        items.clear();
        items.addAll(updated);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkOrderHolder(ItemWorkOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static final class WorkOrderHolder extends RecyclerView.ViewHolder {
        private final ItemWorkOrderBinding binding;
        WorkOrderHolder(ItemWorkOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(WorkOrder item) {
            binding.titleText.setText(item.getTitle());
            binding.siteText.setText(item.getId() + "  •  " + item.getSite());
            binding.priorityText.setText(item.getPriority().name());
            binding.statusText.setText(item.getStatus().name().replace('_', ' '));
        }
    }
}

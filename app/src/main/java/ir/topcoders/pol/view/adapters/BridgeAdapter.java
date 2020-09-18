package ir.topcoders.pol.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.databinding.ItemListBridgeBinding;
import ir.topcoders.pol.view.activities.BridgeDetailActivity;

public class BridgeAdapter extends RecyclerView.Adapter<BridgeAdapter.Holder> {

    private ArrayList<Bridge> allBridges = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return Holder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.onBind(allBridges.get(i));
    }

    @Override
    public int getItemCount() {
        return allBridges.size();
    }

    public void setData(List<Bridge> bridges) {
        allBridges.clear();
        allBridges.addAll(bridges);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemListBridgeBinding binding;

        private static Holder create(LayoutInflater inflater, ViewGroup parent) {
            return new Holder(ItemListBridgeBinding.inflate(inflater, parent, false));
        }

        private Holder(ItemListBridgeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> BridgeDetailActivity.showActivityForEdit(v.getContext(), binding.getBridge().bridgeId));
        }

        private void onBind(Bridge bridge) {
            if (binding != null) {
                binding.setBridge(bridge);
                binding.executePendingBindings();
            }
        }
    }
}

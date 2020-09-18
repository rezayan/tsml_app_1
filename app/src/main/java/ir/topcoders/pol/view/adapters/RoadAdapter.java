package ir.topcoders.pol.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ir.topcoders.pol.data.model.RoadWithBridgeCount;
import ir.topcoders.pol.databinding.ItemListRoadBinding;
import ir.topcoders.pol.view.activities.BridgeListActivity;
import ir.topcoders.pol.view.activities.RoadListActivity;

public class RoadAdapter extends RecyclerView.Adapter<RoadAdapter.Holder> {

    private ArrayList<RoadWithBridgeCount> AllRoads = new ArrayList<>();
    private AtomicBoolean isSelectionMode = new AtomicBoolean(false);

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return Holder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup, isSelectionMode);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.onBind(AllRoads.get(i));
    }

    public void setSelectionMode(boolean enable) {
        isSelectionMode.set(enable);
        if (AllRoads != null)
            for (RoadWithBridgeCount road : AllRoads)
                road.isSelected = false;
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode.get();
    }

    @Override
    public int getItemCount() {
        return AllRoads.size();
    }

    public void setData(List<RoadWithBridgeCount> roads) {
        AllRoads.clear();
        AllRoads.addAll(roads);
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedRoadIds() {
        ArrayList<String> selected = new ArrayList<>();
        if (AllRoads != null)
            for (RoadWithBridgeCount road : AllRoads)
                if (road.isSelected)
                    selected.add(road.roadId);
        return selected;
    }

    public RoadWithBridgeCount getSelectedRoad() {
        if (AllRoads != null)
            for (RoadWithBridgeCount road : AllRoads)
                if (road.isSelected)
                    return road;
        return null;
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemListRoadBinding binding;
        private AtomicBoolean isSelectionMode;

        private static Holder create(LayoutInflater inflater, ViewGroup parent, AtomicBoolean isSelectionMode) {
            return new Holder(ItemListRoadBinding.inflate(inflater, parent, false), isSelectionMode);
        }

        private Holder(ItemListRoadBinding binding, AtomicBoolean isSelectionMode) {
            super(binding.getRoot());
            this.isSelectionMode = isSelectionMode;
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (this.isSelectionMode.get()) {
                    binding.getRoad().isSelected = !binding.getRoad().isSelected;
                    if (itemView.getContext() instanceof RoadListActivity)
                        ((RoadListActivity) itemView.getContext()).selectionUpdated(getAdapterPosition());
                } else
                    BridgeListActivity.showActivity(v.getContext(), binding.getRoad());
            });
        }

        private void onBind(RoadWithBridgeCount road) {
            if (binding != null) {
                binding.setRoad(road);
                binding.executePendingBindings();
            }
        }
    }
}

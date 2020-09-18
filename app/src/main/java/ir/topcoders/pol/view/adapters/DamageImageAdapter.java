package ir.topcoders.pol.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.topcoders.pol.data.model.DamageImagesWithFullInfo;
import ir.topcoders.pol.databinding.ItemListDamageImageBinding;
import ir.topcoders.pol.view.activities.DamageDetailActivity;

public class DamageImageAdapter extends RecyclerView.Adapter<DamageImageAdapter.Holder> {

    private ArrayList<DamageImagesWithFullInfo> allImages = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return Holder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.onBind(allImages.get(i));
    }

    @Override
    public int getItemCount() {
        return allImages.size();
    }

    public void setData(List<DamageImagesWithFullInfo> images) {
        allImages.clear();
        allImages.addAll(images);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemListDamageImageBinding binding;

        private static Holder create(LayoutInflater inflater, ViewGroup parent) {
            return new Holder(ItemListDamageImageBinding.inflate(inflater, parent, false));
        }

        private Holder(ItemListDamageImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.damageImageView.setOnClickListener(v -> {
                if (itemView.getContext() instanceof DamageDetailActivity)
                    ((DamageDetailActivity) itemView.getContext()).openDamageImage(binding.getDamageImage());
            });
            this.binding.removeButton.setOnClickListener(v -> {
                if (itemView.getContext() instanceof DamageDetailActivity)
                    ((DamageDetailActivity) itemView.getContext()).removeDamageImage(binding.getDamageImage());
            });
        }

        private void onBind(DamageImagesWithFullInfo image) {
            if (binding != null) {
                binding.setDamageImage(image);
                binding.executePendingBindings();
            }
        }
    }
}

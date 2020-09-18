package ir.topcoders.pol.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.topcoders.pol.R;
import ir.topcoders.pol.data.model.BridgeDamageWithImage;
import ir.topcoders.pol.databinding.ItemListDamageBinding;
import ir.topcoders.pol.utils.DamageUtils;
import ir.topcoders.pol.view.activities.DamageDetailActivity;

public class DamageAdapter extends RecyclerView.Adapter<DamageAdapter.Holder> {

    private ArrayList<BridgeDamageWithImage> allDamages = new ArrayList<>();
    private String roadId;

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return Holder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup, roadId);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.onBind(allDamages.get(i));
    }

    @Override
    public int getItemCount() {
        return allDamages.size();
    }

    public void setData(List<BridgeDamageWithImage> damages) {
        allDamages.clear();
        allDamages.addAll(damages);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemListDamageBinding binding;
        private String[] elements;
        private String[] damageLevels;
        private String[] investigationStatuss;
        private String[] investigationProblems;

        private static Holder create(LayoutInflater inflater, ViewGroup parent, String roadId) {
            return new Holder(ItemListDamageBinding.inflate(inflater, parent, false), roadId);
        }

        private Holder(ItemListDamageBinding binding, String roadId) {
            super(binding.getRoot());
            setupLists();
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> DamageDetailActivity.showActivityForEdit(v.getContext(), roadId, binding.getDamage().damageId));
        }

        private void setupLists() {
            elements = itemView.getContext().getResources().getStringArray(R.array.element_list);
            damageLevels = itemView.getContext().getResources().getStringArray(R.array.damage_level_list);
            investigationStatuss = itemView.getContext().getResources().getStringArray(R.array.investigation_status_list);
            investigationProblems = itemView.getContext().getResources().getStringArray(R.array.investigation_problem_list);
        }

        private void onBind(BridgeDamageWithImage damage) {
            if (binding != null) {
                binding.setDamage(damage);
                binding.executePendingBindings();
                binding.elementTextView.setText(elements[damage.elementCode]);
                binding.damageTextView.setText(DamageUtils.getDamageNameByIndex(damage.damageCode));
                binding.damageLevelTextView.setText(damageLevels[damage.damageLevel]);
                binding.investigationStatusTextView.setText(investigationStatuss[damage.investigationStatus]);
                binding.investigationProblemTextView.setText(investigationProblems[damage.investigationProblem]);
            }
        }
    }
}

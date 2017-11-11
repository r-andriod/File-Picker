package com.jxyedu.lib.filepicker.adapter;

import android.support.v7.widget.RecyclerView;

import com.jxyedu.lib.filepicker.model.BaseFileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseSelectAdapter
 * Created by renwoxing on 2017/11/10.
 */
public abstract class BaseSelectAdapter <VH extends RecyclerView.ViewHolder,T extends BaseFileBean> extends RecyclerView.Adapter<VH> implements ISelection<T> {

    private static final String TAG = BaseSelectAdapter.class.getSimpleName();
    private List<T> items;

    protected List<T> selectedPahts;

    /**
     * 构造
     * @param items
     * @param selectedPaths
     */
    public BaseSelectAdapter(List<T> items, List<String> selectedPaths) {

        this.items = items;
        this.selectedPahts = new ArrayList<>();

        addPathsToSelections(selectedPaths);
    }

    /**
     * 新增选择的图片
     * @param selectedPaths
     */
    private void addPathsToSelections(List<String> selectedPaths) {
        if (selectedPaths == null) return;

        for (int index = 0; index < items.size(); index++) {
            for (int jindex = 0; jindex < selectedPaths.size(); jindex++) {
                if (items.get(index).getPath().equals(selectedPaths.get(jindex))) {
                    selectedPahts.add(items.get(index));
                }
            }
        }
    }

    /**
     * Indicates if the item at position where is selected
     *
     * @param photo Media of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override public boolean isSelected(T photo) {
        return selectedPahts.contains(photo);
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Media of the item to toggle the selection status for
     */
    @Override public void toggleSelection(T photo) {
        if (selectedPahts.contains(photo)) {
            selectedPahts.remove(photo);
        } else {
            selectedPahts.add(photo);
        }
    }

    /**
     * Clear the selection status for all items
     */
    @Override public void clearSelection() {
        selectedPahts.clear();
        notifyDataSetChanged();
    }

    @Override public int getSelectedItemCount() {
        return selectedPahts.size();
    }


    public void selectAll() {
        selectedPahts.clear();
        selectedPahts.addAll(items);
        notifyDataSetChanged();
    }


    public void setData(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public ArrayList<String> getSelectedPaths(){
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < selectedPahts.size(); index++) {
            paths.add(selectedPahts.get(index).getPath());
        }
        return paths;
    }

}

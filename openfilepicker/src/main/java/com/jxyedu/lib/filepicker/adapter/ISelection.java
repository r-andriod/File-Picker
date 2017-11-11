package com.jxyedu.lib.filepicker.adapter;

/**
 * ISelection
 * Created by renwoxing on 2017/11/10.
 */
public interface ISelection<T> {

    /**
     * Indicates if the item at position position is selected
     * 判断是否是选择项。
     * @param item to check
     * @return true if the item is selected, false otherwise
     */
    boolean isSelected(T item);

    /**
     * Toggle the selection status of the item at a given position
     * 选择／取消
     * @param item to toggle the selection status for
     */
    void toggleSelection(T item);

    /**
     * Clear the selection status for all items
     * 清除选择项
     */
    void clearSelection();

    /**
     * Count the selected items
     * 已选择条数
     * @return Selected items count
     */
    int getSelectedItemCount();

}

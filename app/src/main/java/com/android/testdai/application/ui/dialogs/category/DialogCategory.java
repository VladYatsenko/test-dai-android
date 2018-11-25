package com.android.testdai.application.ui.dialogs.category;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.testdai.R;
import com.android.testdai.application.domain.category.model.Category;
import com.android.testdai.application.domain.category.model.GroupEnum;
import com.android.testdai.application.ui.dialogs.abstraction.AbstractDialog;
import com.android.testdai.application.ui.dialogs.category.abstraction.ICategoryView;
import com.android.testdai.di.DIProvider;

import java.util.List;


public class DialogCategory extends AbstractDialog<CategoryPresenter> implements ICategoryView {

    private RecyclerView categoryRecyclervView;

    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_category, null);

        categoryRecyclervView = (RecyclerView) v.findViewById(R.id.category_recycler);
        categoryRecyclervView.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.select_category)
                .setPositiveButton(android.R.string.ok,
                        (dialog, i) -> presenter.onOkClick());

        dialog = builder.create();
        dialog.show();

        return  dialog;

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);

    }

    @Override
    protected void injectDependencies() {
        DIProvider.getDialogsComponent().inject(this);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout linearLayout;
        private TextView categoryText;

        CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_category, parent,false));

            itemView.setOnClickListener((View.OnClickListener) this);
            categoryText = (TextView) itemView.findViewById(R.id.category_text);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.category_layout);

        }

        void bind(Category category){

            if(!category.getGroup().getGroupName().equals(GroupEnum.EMPTY)){
                categoryText.setText(category.getName());
                if(category.getGroup().isEnabled()) {
                    if(category.isSelected()){
                        linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.selected_category));
                        categoryText.setTextColor(getResources().getColor(R.color.selected));
                    }else{
                        linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.not_selected));
                        categoryText.setTextColor(getResources().getColor(R.color.enable));
                    }
                }
                else{
                    linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.disable));
                    categoryText.setTextColor(getResources().getColor(R.color.disable));
                }

            }

        }

        @Override
        public void onClick (View view){
            presenter.onClick(this.getAdapterPosition());
        }

    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder>{

        private List<Category> categories;

        CategoryAdapter(List<Category> categories){
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

    }

    @Override
    public void updateUI(List<Category> categories, boolean okState) {

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        categoryRecyclervView.setAdapter(categoryAdapter);
        setButtonEnabled(okState);

    }

    private void setButtonEnabled(boolean state){

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(state);

    }


}
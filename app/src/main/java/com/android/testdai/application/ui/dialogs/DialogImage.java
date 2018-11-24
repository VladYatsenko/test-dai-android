package com.android.testdai.application.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.testdai.R;
import com.android.testdai.util.AnalyticUtil;
import com.squareup.picasso.Picasso;

public class DialogImage extends DialogFragment {

    private static final String ARG_IMAGE = "imageSource";

    public static DialogImage newInstance(String source){
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, source);

        DialogImage fragment = new DialogImage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        String source = (String) getArguments().getSerializable(ARG_IMAGE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);

        AnalyticUtil.getInstance(getActivity().getApplicationContext()).logScreenEvent(getClass().getSimpleName());


        ImageView mImageView = v.findViewById(R.id.imageQuestion);
        Picasso.get()
                .load(source)
                .placeholder(R.drawable.empty)
                .into(mImageView);
        mImageView.setOnClickListener(view -> getDialog().dismiss());


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

}
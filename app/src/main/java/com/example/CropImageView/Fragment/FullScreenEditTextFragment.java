package com.example.CropImageView.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cropimageview.R;

public class FullScreenEditTextFragment extends DialogFragment {

    private EditText editText;
    private OnCloseListener onCloseListener;

    public interface OnCloseListener {
        void onClose(String strSticker);
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        editText = view.findViewById(R.id.editText);
        Button closeButton = view.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCloseListener != null) {
                    if(!editText.getText().toString().isEmpty()){
                        onCloseListener.onClose(editText.getText().toString());
                    }
                    else {
                        onCloseListener.onClose("");
                    }

                   /* Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra(editText.getText().toString(),"text");
                    getContext().startActivity(intent);*/
                }
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        openKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }
    private void openKeyboard() {
        if (editText != null) {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
    private void hideKeyboard() {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }
}

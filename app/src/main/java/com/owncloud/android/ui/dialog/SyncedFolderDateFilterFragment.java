package com.owncloud.android.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.owncloud.android.databinding.SyncedFoldersDateFilterFragmentBinding;
import com.owncloud.android.datamodel.SyncDelay;

import androidx.fragment.app.DialogFragment;

public class SyncedFolderDateFilterFragment extends DialogFragment {
    private Spinner newerThan;
    private Spinner olderThan;

    public Listener mListener;

    private SyncedFoldersDateFilterFragmentBinding binding;

    // Empty constructor required for DialogFragment
    public SyncedFolderDateFilterFragment() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    interface Listener {
        void onDateFilterResult(int requestCode, SyncDelay dateFilter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = SyncedFoldersDateFilterFragmentBinding.inflate(requireActivity().getLayoutInflater(), null, false);

        newerThan = binding.newerThan;
        newerThan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) {
                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        }
                    };
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), onDateSetListener, 2022, 11, 1);
                    datePickerDialog.show();
                } else if(i==2) {
                    DurationPickerFragment dialog = new DurationPickerFragment();
                    dialog.setMetrics("d", "h", "m");
                    dialog.show(getParentFragmentManager(), "dialog");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] newerThanSpinnerValues = {"Od zawsze","Od dnia","Nowsze niż"};
        ArrayAdapter newerThanSpinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, newerThanSpinnerValues);
        newerThanSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newerThan.setAdapter(newerThanSpinnerAdapter);



        olderThan = binding.olderThan;
        olderThan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] olderThanSpinnerValues = {"Do zawsze","Do dnia","Starsze niż"};
        ArrayAdapter olderThanSpinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, olderThanSpinnerValues);
        olderThanSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        olderThan.setAdapter(newerThanSpinnerAdapter);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext());
        builder.setView(binding.getRoot());

        //viewThemeUtils.dialog.colorMaterialAlertDialogBackground(binding.getRoot().getContext(), builder);

        return builder.create();
    }


}

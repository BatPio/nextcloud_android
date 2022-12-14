package com.owncloud.android.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nextcloud.client.di.Injectable;
import com.owncloud.android.R;
import com.owncloud.android.databinding.DurationPickerBinding;
import com.owncloud.android.databinding.SyncedFoldersSettingsLayoutBinding;
import com.owncloud.android.datamodel.SyncedFolderDisplayItem;
import com.owncloud.android.ui.dialog.parcel.SyncedFolderParcelable;
import com.owncloud.android.utils.theme.ViewThemeUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DurationPickerFragment extends DialogFragment implements Injectable {

    public static final String DURATION = "duration";

    @Inject ViewThemeUtils viewThemeUtils;

    private NumberPicker daysPicker;
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;

    private DurationPickerBinding binding;

    public Listener mListener;

    public static DurationPickerFragment newInstance(long duration) {
        Bundle args = new Bundle();
        args.putLong(DURATION, duration);

        DurationPickerFragment dialogFragment = new DurationPickerFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setStyle(STYLE_NORMAL, R.style.Theme_ownCloud_Dialog);

        return dialogFragment;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    interface Listener {
        void onDurationPickerResult(int resultCode, long duration);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep the state of the fragment on configuration changes
        setRetainInstance(true);

        binding = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DurationPickerBinding.inflate(requireActivity().getLayoutInflater(), null, false);

        daysPicker = binding.daysPicker;
        hoursPicker = binding.hoursPicker;
        minutesPicker = binding.minutesPicker;

        daysPicker.setMaxValue(30);
        hoursPicker.setMaxValue(24);
        minutesPicker.setMaxValue(59);

        long duration = requireArguments().getLong(DURATION);
        setDuration(duration);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext());
        builder.setView(binding.getRoot());
        builder.setPositiveButton(R.string.common_save, (dialog, whichButton) -> mListener.onDurationPickerResult(Activity.RESULT_OK, getDuration()));
        builder.setNegativeButton(R.string.common_cancel, (dialog, whichButton) -> mListener.onDurationPickerResult(Activity.RESULT_CANCELED, 0));

        viewThemeUtils.dialog.colorMaterialAlertDialogBackground(binding.getRoot().getContext(), builder);

        AlertDialog dialog = builder.create();
        //viewThemeUtils.platform.colorTextButtons(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
        //viewThemeUtils.platform.colorTextButtons(dialog.getButton(AlertDialog.BUTTON_NEGATIVE));
        return dialog;
    }

    private long getDuration() {
        return TimeUnit.DAYS.toMillis(daysPicker.getValue()) +
            TimeUnit.HOURS.toMillis(hoursPicker.getValue()) +
            TimeUnit.MINUTES.toMillis(minutesPicker.getValue());
    }

    private void setDuration(long duration) {
        DurationComponents durationComponents = decomposeDuration(duration);
        daysPicker.setValue(durationComponents.getDays());
        hoursPicker.setValue(durationComponents.getHours());
        minutesPicker.setValue(durationComponents.getMinutes());
    }

    private DurationComponents decomposeDuration(long duration) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(duration);
        int hours = (int) TimeUnit.MILLISECONDS.toHours(duration) - (days * 24);
        int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(duration) - (TimeUnit.MILLISECONDS.toHours(duration)* 60));
        return new DurationComponents(days, hours, minutes);
    }


    private class DurationComponents {
        int days;
        int hours;
        int minutes;

        public DurationComponents(int days, int hours, int minutes) {
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
        }

        public int getDays() {
            return days;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }
    }
}

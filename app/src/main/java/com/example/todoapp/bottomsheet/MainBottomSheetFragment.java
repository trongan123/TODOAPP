package com.example.todoapp.bottomsheet;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todoapp.R;
import com.example.todoapp.adater.TabItemBottomSheetAdapter;
import com.example.todoapp.databinding.FragmentMainBottomSheetBinding;
import com.example.todoapp.databinding.UpdateBottomSheetLayoutBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class MainBottomSheetFragment extends Fragment {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private final TodoItem todoItem = new TodoItem();
    private BottomSheetDialog bottomSheetDialog;
    private FragmentMainBottomSheetBinding fragmentMainBinding;
    private UpdateBottomSheetLayoutBinding updateBottomSheetLayoutBinding;
    private TodoItemViewModel todoItemViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMainBinding = FragmentMainBottomSheetBinding.inflate(inflater, container, false);

        View mView = fragmentMainBinding.getRoot();
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);

        fragmentMainBinding.setMainFragViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //set shared element back for recyclerview
        postponeEnterTransition();
        final ViewGroup parentView = (ViewGroup) view.getParent();
        parentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                parentView.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
        super.onViewCreated(view, savedInstanceState);
        //click button add to show bottom sheet add new item
        fragmentMainBinding.btnAdd.setOnClickListener(view1 -> addItemBottomSheet(parentView));

        ViewPager2 viewPager2 = requireView().findViewById(R.id.vpg);
        fragmentMainBinding.vpg.setAdapter(new TabItemBottomSheetAdapter(requireActivity(), todoItemViewModel));
        TabLayout tabLayout = requireView().findViewById(R.id.tloMenu);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("All");
                    break;
                case 1:
                    tab.setText("Pending");
                    break;
                default:
                    tab.setText("Completed");
                    break;
            }
        });

        tabLayoutMediator.attach();
        fragmentMainBinding.svSearch.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            fragmentMainBinding.sbSearchBar.setText(fragmentMainBinding.svSearch.getText());
            fragmentMainBinding.svSearch.hide();
            todoItemViewModel.getStringMutableLiveData().postValue(Objects.requireNonNull(
                    fragmentMainBinding.svSearch.getText()).toString());
            return false;
        });

        todoItemViewModel.getListMutableLiveDataCheck().observe(requireActivity(), longs ->
                fragmentMainBinding.btnClearAll.setEnabled(!longs.isEmpty()));
        fragmentMainBinding.btnClearAll.setOnClickListener(view12 -> clearItem());
    }

    private void addItemBottomSheet(ViewGroup parentView) {
        if (updateBottomSheetLayoutBinding == null) {
            updateBottomSheetLayoutBinding = UpdateBottomSheetLayoutBinding.inflate(LayoutInflater
                    .from(parentView.getContext()), parentView, false);
            bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(updateBottomSheetLayoutBinding.getRoot());

            MaterialDatePicker<Long> datePickerCreated;
            datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

            updateBottomSheetLayoutBinding.edtCompletedDate.setInputType(
                    InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            updateBottomSheetLayoutBinding.edtCreatedDate.setInputType(
                    InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);

            String[] type = new String[]{"pending", "completed"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.dropdown_menu_popup_item, R.id.txtStyle, type);
            updateBottomSheetLayoutBinding.dropDownStatus.setAdapter(adapter);

            updateBottomSheetLayoutBinding.btnAdd.setOnClickListener(view1 -> {
                try {
                    addItem();
                } catch (ParseException e) {
                    //Method empty
                }

            });
            updateBottomSheetLayoutBinding.btnClear.setOnClickListener(view12 -> clearText());

            updateBottomSheetLayoutBinding.edtCreatedDate.setOnClickListener(view13 ->
                    addDatePicker(updateBottomSheetLayoutBinding.edtCreatedDate, datePickerCreated));

            //create datePicker
            updateBottomSheetLayoutBinding.edtCompletedDate.setOnClickListener(view14 ->
                    addDatePicker(updateBottomSheetLayoutBinding.edtCompletedDate, datePickerCreated));
        }
        // Opt in to perform swipe to dismiss animation when dismissing bottom sheet dialog.
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    //create date picker
    private void addDatePicker(TextInputEditText textDate, MaterialDatePicker<Long> datePickerCreated) {
        if (datePickerCreated.isAdded()) {
            return;
        }
        datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
        datePickerCreated.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
            String formattedDate = format.format(calendar.getTime());
            textDate.setText(formattedDate);
        });
    }

    //show dialog to confirm clear all item choice
    private void clearItem() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Confirm Clear All")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    todoItemViewModel.clearAllItem();
                    Toast.makeText(getActivity(), "Clear successfully", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", null).show();
    }

    private void addItem() throws ParseException {
        if (validation()) {
            String stringTitle = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtTitle.getText()).toString().trim();
            String stringDescription = Objects.requireNonNull(updateBottomSheetLayoutBinding
                    .edtDescription.getText()).toString().trim();
            Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                    .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCreatedDate.getText()).toString().trim());
            Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                    .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCompletedDate.getText()).toString().trim());
            String stringStatus = updateBottomSheetLayoutBinding.dropDownStatus.getText().toString().trim();

            //update database
            todoItem.setTitle(stringTitle);
            todoItem.setDescription(stringDescription);
            todoItem.setCreatedDate(createdDate);
            todoItem.setCompletedDate(completedDate);
            todoItem.setStatus(stringStatus);

            todoItemViewModel.addItem(todoItem);
            clearText();
            Toast.makeText(getActivity(), "Add success", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        }
    }

    private boolean validation() throws ParseException {
        boolean check = true;
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtTitle.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtTitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtDescription.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtDescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCreatedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtCreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCompletedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtCompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (updateBottomSheetLayoutBinding.dropDownStatus.getText().toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.dropDownStatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }
        Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(updateBottomSheetLayoutBinding.edtCreatedDate.getText().toString().trim());
        Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(updateBottomSheetLayoutBinding.edtCompletedDate.getText().toString().trim());
        assert createdDate != null;
        if (createdDate.compareTo(completedDate) > 0) {
            updateBottomSheetLayoutBinding.tilCompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }

    private void clearText() {
        updateBottomSheetLayoutBinding.edtTitle.setText("");
        updateBottomSheetLayoutBinding.edtTitle.setError(null);
        updateBottomSheetLayoutBinding.edtDescription.setText("");
        updateBottomSheetLayoutBinding.edtDescription.setError(null);
        updateBottomSheetLayoutBinding.edtCreatedDate.setText("");
        updateBottomSheetLayoutBinding.edtCreatedDate.setError(null);
        updateBottomSheetLayoutBinding.edtCompletedDate.setText("");
        updateBottomSheetLayoutBinding.edtCompletedDate.setError(null);
        updateBottomSheetLayoutBinding.dropDownStatus.setText("", false);
        updateBottomSheetLayoutBinding.dropDownStatus.setError(null);
    }
}
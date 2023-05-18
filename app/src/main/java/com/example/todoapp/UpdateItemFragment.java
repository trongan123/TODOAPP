package com.example.todoapp;


import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.todoapp.databinding.FragmentUpdateItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class UpdateItemFragment extends Fragment {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private FragmentUpdateItemBinding fragmentUpdateItemBinding;
    private TodoItemViewModel todoItemViewModel;
    private TodoItem todoItem = new TodoItem();

    public UpdateItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setSharedElementEnterTransition(new ChangeBounds());
        fragmentUpdateItemBinding = FragmentUpdateItemBinding.inflate(inflater, container, false);
        View mView = fragmentUpdateItemBinding.getRoot();
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        fragmentUpdateItemBinding.setTodoItemViewModel(todoItemViewModel);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        fragmentUpdateItemBinding.btnUpdate.setOnClickListener(view1 -> {
            try {
                updateItem();
            } catch (ParseException e) {
                Log.e("ParseException", e.getMessage());
            }
        });
        fragmentUpdateItemBinding.btnDelete.setOnClickListener(view12 -> {
            try {
                deleteItem();
            } catch (ParseException e) {
                Log.e("ParseException", e.getMessage());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }


    private void updateItem() throws ParseException {
        if (validation()) {
            String stringTitle = Objects.requireNonNull(fragmentUpdateItemBinding.edtTitle.getText()).toString().trim();
            String stringDescription = Objects.requireNonNull(fragmentUpdateItemBinding.edtDescription.getText()).toString().trim();
            Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentUpdateItemBinding.edtCreatedDate.getText()).toString().trim());
            Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentUpdateItemBinding.edtCompletedDate.getText()).toString().trim());
            String stringStatus = fragmentUpdateItemBinding.dropDownStatus.getText().toString().trim();

            //update database
            todoItem.setTitle(stringTitle);
            todoItem.setDescription(stringDescription);
            todoItem.setCreatedDate(createdDate);
            todoItem.setCompletedDate(completedDate);
            todoItem.setStatus(stringStatus);

            todoItemViewModel.updateItem(todoItem);
            Toast.makeText(getActivity(), "Update success", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_updateItemFragment_to_mainFragment);
        }
    }

    private void deleteItem() throws ParseException {
        String strtitle = Objects.requireNonNull(fragmentUpdateItemBinding.edtTitle.getText()).toString().trim();
        String strDes = Objects.requireNonNull(fragmentUpdateItemBinding.edtDescription.getText()).toString().trim();
        Date credate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentUpdateItemBinding.edtCreatedDate.getText()).toString().trim());
        Date comdate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentUpdateItemBinding.edtCompletedDate.getText()).toString().trim());
        String strStt;
        strStt = fragmentUpdateItemBinding.dropDownStatus.getText().toString().trim();

        //update database
        todoItem.setTitle(strtitle);
        todoItem.setDescription(strDes);
        todoItem.setCreatedDate(credate);
        todoItem.setCompletedDate(comdate);
        todoItem.setStatus(strStt);

        new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Confirm delete").setMessage("Are you sure?").setPositiveButton("Yes", (dialogInterface, i) -> {
            todoItemViewModel.deleteItem(todoItem);
            Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
        }).setNegativeButton("No", null).show();

    }

    private void initUi() {
        assert getArguments() != null;
        todoItem = (TodoItem) getArguments().getSerializable("objectTodoItem");

        String transition = getArguments().getString("transition");
        DateFormat dateFormat = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
        fragmentUpdateItemBinding.constraintLayout.setTransitionName(transition);

        if (todoItem != null) {
            fragmentUpdateItemBinding.edtTitle.setText(todoItem.getTitle());
            fragmentUpdateItemBinding.edtDescription.setText(todoItem.getDescription());
            fragmentUpdateItemBinding.edtCreatedDate.setText(dateFormat.format(todoItem.getCreatedDate()));
            fragmentUpdateItemBinding.edtCompletedDate.setText(dateFormat.format(todoItem.getCompletedDate()));
            fragmentUpdateItemBinding.dropDownStatus.setText(todoItem.getStatus(), false);
        }
        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, R.id.txtStyle, type);
        fragmentUpdateItemBinding.dropDownStatus.setAdapter(adapter);

        fragmentUpdateItemBinding.edtCreatedDate.setOnClickListener(view ->
                addDatePicker(fragmentUpdateItemBinding.edtCreatedDate));

        fragmentUpdateItemBinding.edtCompletedDate.setOnClickListener(view ->
                addDatePicker(fragmentUpdateItemBinding.edtCompletedDate));
    }

    //create date picker
    private void addDatePicker(TextInputEditText textDate) {
        MaterialDatePicker<Long> datePickerCreated;
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
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

    private boolean validation() throws ParseException {
        boolean check = true;

        if (Objects.requireNonNull(fragmentUpdateItemBinding.edtTitle.getText()).toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.edtTitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentUpdateItemBinding.edtDescription.getText()).toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.edtDescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentUpdateItemBinding.edtCreatedDate.getText()).toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.edtCreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentUpdateItemBinding.edtCompletedDate.getText()).toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.edtCompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (fragmentUpdateItemBinding.dropDownStatus.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.dropDownStatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }
        Date credate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(fragmentUpdateItemBinding.edtCreatedDate.getText().toString().trim());
        Date comdate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(fragmentUpdateItemBinding.edtCompletedDate.getText().toString().trim());
        assert credate != null;
        if (credate.compareTo(comdate) > 0) {
            fragmentUpdateItemBinding.edtCompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }
}
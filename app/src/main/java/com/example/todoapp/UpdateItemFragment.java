package com.example.todoapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.ChangeBounds;
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
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class UpdateItemFragment extends Fragment {

    private FragmentUpdateItemBinding fragmentUpdateItemBinding;
    private TodoItemViewModel todoItemViewModel;
    private MaterialDatePicker datePickerCompleted;
    private MaterialDatePicker datePickerCreated;
    private TodoItem todoItem = new TodoItem();

    private View mView;


    public UpdateItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        R.layout.dropdown_menu_popup_item, R.id.txtstyle,
                        type);
        fragmentUpdateItemBinding.dropdownstatus.setAdapter(adapter);

        initUi();
        fragmentUpdateItemBinding.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateItem();

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        fragmentUpdateItemBinding.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteItem();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setSharedElementEnterTransition(new ChangeBounds());

        fragmentUpdateItemBinding = FragmentUpdateItemBinding.inflate(inflater, container, false);
        mView = fragmentUpdateItemBinding.getRoot();

        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        fragmentUpdateItemBinding.setTodoItemViewModel(todoItemViewModel);

        datePickerCreated = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePickerCompleted = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        return mView;
    }

    private void updateItem() throws ParseException {
        if (validation()) {
            String strtitle = Objects.requireNonNull(fragmentUpdateItemBinding.edttitle.getText()).toString().trim();
            String strDes = Objects.requireNonNull(fragmentUpdateItemBinding.edtdescription.getText()).toString().trim();
            Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fragmentUpdateItemBinding.edtcreatedDate.getText().toString().trim());
            Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fragmentUpdateItemBinding.edtcompletedDate.getText().toString().trim());
            String strStt = fragmentUpdateItemBinding.dropdownstatus.getText().toString().trim();

            //update database
            todoItem.setTitle(strtitle);
            todoItem.setDescription(strDes);
            todoItem.setCreatedDate(credate);
            todoItem.setCompletedDate(comdate);
            todoItem.setStatus(strStt);

            todoItemViewModel.updateItem(todoItem);
            Toast.makeText(getActivity(), "Update success", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_updateItemFragment_to_mainFragment);
        }
    }

    private void deleteItem() throws ParseException {
        String strtitle = fragmentUpdateItemBinding.edttitle.getText().toString().trim();
        String strDes = fragmentUpdateItemBinding.edtdescription.getText().toString().trim();
        Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentUpdateItemBinding.edtcreatedDate.getText().toString().trim());
        Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentUpdateItemBinding.edtcompletedDate.getText().toString().trim());
        String strStt = fragmentUpdateItemBinding.dropdownstatus.getText().toString().trim();


        //update database
        todoItem.setTitle(strtitle);
        todoItem.setDescription(strDes);
        todoItem.setCreatedDate(credate);
        todoItem.setCompletedDate(comdate);
        todoItem.setStatus(strStt);

//        new AlertDialog.Builder(getContext())
//                .setTitle("Confirm delete")
//                .setMessage("Are you sure?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        todoItemViewModel.deleteItem(todoItem);
//                        Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
//                        Navigation.findNavController(getView()).navigate(R.id.action_updateItemFragment_to_mainFragment);
//
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();


        new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Confirm delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    todoItemViewModel.deleteItem(todoItem);
                    Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.mainFragment);
                })
                .setNegativeButton("No", null)
                .show();
        Toast.makeText(getActivity(), "Delete success", Toast.LENGTH_SHORT).show();


    }

    private void initUi() {
        todoItem = (TodoItem) getArguments().getSerializable("object_TodoItem");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (todoItem != null) {
            fragmentUpdateItemBinding.edttitle.setText(todoItem.getTitle());
            fragmentUpdateItemBinding.edtdescription.setText(todoItem.getDescription());
            fragmentUpdateItemBinding.edtcreatedDate.setText(dateFormat.format(todoItem.getCreatedDate()));
            fragmentUpdateItemBinding.edtcompletedDate.setText(dateFormat.format(todoItem.getCompletedDate()));
            fragmentUpdateItemBinding.dropdownstatus.setText(todoItem.getStatus(), false);
        }
        fragmentUpdateItemBinding.edtcreatedDate.setOnClickListener(view -> {
            datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
            datePickerCreated.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);

                String formattedDate = dateFormat.format(calendar.getTime());
                fragmentUpdateItemBinding.edtcreatedDate.setText(formattedDate);
            });
        });

        //create datePicker
        fragmentUpdateItemBinding.edtcompletedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerCompleted.show(getParentFragmentManager(), "Material_Date_Picker");
                datePickerCompleted.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis((Long) selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = format.format(calendar.getTime());
                    fragmentUpdateItemBinding.edtcompletedDate.setText(formattedDate);
                });
            }
        });
    }

    private boolean validation() throws ParseException {
        boolean check = true;

        if (fragmentUpdateItemBinding.edttitle.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.tiltitle.setError("Field title can't empty");
            check = false;
        }
        if (fragmentUpdateItemBinding.edtdescription.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.tildescription.setError("Field description can't empty");
            check = false;
        }
        if (fragmentUpdateItemBinding.edtcreatedDate.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.tilcreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (fragmentUpdateItemBinding.edtcompletedDate.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.tilcompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (fragmentUpdateItemBinding.dropdownstatus.getText().toString().trim().isEmpty()) {
            fragmentUpdateItemBinding.tilstatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return check;
        }
        Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentUpdateItemBinding.edtcreatedDate.getText().toString().trim());
        Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentUpdateItemBinding.edtcompletedDate.getText().toString().trim());
        assert credate != null;
        if (credate.compareTo(comdate) > 0) {
            fragmentUpdateItemBinding.tilcompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }

}
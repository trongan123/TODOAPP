package com.example.todoapp.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoItemBottomSheetAdapter;
import com.example.todoapp.R;
import com.example.todoapp.databinding.FragmentAllItemBottomSheetBinding;
import com.example.todoapp.databinding.UpdateBottomSheetLayoutBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class AllItemBottomSheetFragment extends Fragment {

    private final int TAP_NUMBER;
    private final TodoItemViewModel todoItemViewModel;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private FragmentAllItemBottomSheetBinding fragmentAllItemBinding;
    private TodoItemBottomSheetAdapter todoItemAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private UpdateBottomSheetLayoutBinding updateBottomSheetLayoutBinding;
    private MaterialDatePicker datePickerCompleted;
    private MaterialDatePicker datePickerCreated;


    public AllItemBottomSheetFragment(TodoItemViewModel todoItemViewModel, int TAP_NUMBER) {
        this.todoItemViewModel = todoItemViewModel;
        this.TAP_NUMBER = TAP_NUMBER;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListTodo();
        postponeEnterTransition();
    }

    public void displayListTodo() {

        RecyclerView rcvItem = fragmentAllItemBinding.rcvTodoitem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);

        todoItemAdapter = new TodoItemBottomSheetAdapter(new TodoItemBottomSheetAdapter.TodoItemDiff(), todoItemViewModel);
        todoItemAdapter.setHasStableIds(true);


        //set data to recyclerview
        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), s -> {
            switch (TAP_NUMBER) {
                case 1:
                    //set data for tab all item
                    todoItemViewModel.getAllList(todoItemViewModel.getStringMutableLiveData().getValue()).observe(requireActivity(), items -> todoItemAdapter.submitList(items));
                    break;
                case 2:
                    //set data for tab pending item
                    todoItemViewModel.getPendingList().observe(requireActivity(), items -> todoItemAdapter.submitList(items));
                    break;
                case 3:
                    //set data for tab completed item
                    todoItemViewModel.getCompletedList().observe(requireActivity(), items -> todoItemAdapter.submitList(items));
                    break;
            }
        });

        todoItemAdapter.setClickListenner(new TodoItemBottomSheetAdapter.IClickItemToDo() {
            @Override
            public void DetaiItem(TodoItem todoItem) {
                clickDetailItem(todoItem);
            }

            @Override
            public void clearItem(TodoItem todoItem, long id, boolean check) {
                todoItemViewModel.setClearAll(todoItem.getId(), check);
                todoItemViewModel.setCheckItem(id, check);
            }
        });
        rcvItem.setAdapter(todoItemAdapter);
    }

    //method create new bottom sheet
    private void setBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(updateBottomSheetLayoutBinding.getRoot());
        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, R.id.txtstyle, type);
        updateBottomSheetLayoutBinding.dropdownstatus.setAdapter(adapter);
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        datePickerCompleted = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        updateBottomSheetLayoutBinding.edtcreatedDate.setOnClickListener(view -> {
            datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
            datePickerCreated.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                String formattedDate = dateFormat.format(calendar.getTime());
                updateBottomSheetLayoutBinding.edtcreatedDate.setText(formattedDate);
            });
        });
        //create datePicker
        updateBottomSheetLayoutBinding.edtcompletedDate.setOnClickListener(view -> {
            datePickerCompleted.show(getParentFragmentManager(), "Material_Date_Picker");
            datePickerCompleted.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                String formattedDate = dateFormat.format(calendar.getTime());
                updateBottomSheetLayoutBinding.edtcompletedDate.setText(formattedDate);
            });
        });
    }

    private void clickDetailItem(TodoItem todoItem) {
        if (bottomSheetDialog == null) {
            //create new bottom sheet if bottom sheet not exist
            setBottomSheetDialog();
        }

        updateBottomSheetLayoutBinding.btnAdd.setText(R.string.update);
        updateBottomSheetLayoutBinding.btnAdd.setOnClickListener(view1 -> {
            try {
                updateItemTodo(todoItem);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        updateBottomSheetLayoutBinding.btnclear.setText(R.string.delete);
        updateBottomSheetLayoutBinding.btnclear.setOnClickListener(view12 -> {
            try {
                deleteItemTodo(todoItem);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        initUi(todoItem);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAllItemBinding = FragmentAllItemBottomSheetBinding.inflate(inflater, container, false);

        updateBottomSheetLayoutBinding = UpdateBottomSheetLayoutBinding.inflate(inflater, container, false);
        View mView = fragmentAllItemBinding.getRoot();
        fragmentAllItemBinding.setAllItemViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;

    }


    private void initUi(TodoItem item) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (item != null) {
            updateBottomSheetLayoutBinding.edttitle.setText(item.getTitle());
            updateBottomSheetLayoutBinding.edtdescription.setText(item.getDescription());
            updateBottomSheetLayoutBinding.edtcreatedDate.setText(dateFormat.format(item.getCreatedDate()));
            updateBottomSheetLayoutBinding.edtcompletedDate.setText(dateFormat.format(item.getCompletedDate()));
            updateBottomSheetLayoutBinding.dropdownstatus.setText(item.getStatus(), false);
        }
    }

    private void deleteItemTodo(TodoItem todoItem) throws ParseException {
        String strtitle = Objects.requireNonNull(updateBottomSheetLayoutBinding.edttitle.getText()).toString().trim();
        String strDes = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtdescription.getText()).toString().trim();
        Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcreatedDate.getText()).toString().trim());
        Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcompletedDate.getText()).toString().trim());
        String strStt;
        strStt = updateBottomSheetLayoutBinding.dropdownstatus.getText().toString().trim();

        //update database
        todoItem.setTitle(strtitle);
        todoItem.setDescription(strDes);
        todoItem.setCreatedDate(credate);
        todoItem.setCompletedDate(comdate);
        todoItem.setStatus(strStt);

        if (materialAlertDialogBuilder == null) {
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Confirm delete").setMessage("Are you sure?").setNegativeButton("No", null);
        }

        materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            todoItemViewModel.deleteItem(todoItem);
            Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        }).show();
    }

    private void updateItemTodo(TodoItem todoItem) throws ParseException {
        if (validation()) {
            String strtitle = Objects.requireNonNull(updateBottomSheetLayoutBinding.edttitle.getText()).toString().trim();
            String strDes = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtdescription.getText()).toString().trim();
            Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcreatedDate.getText()).toString().trim());
            Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcompletedDate.getText()).toString().trim());
            String strStt = updateBottomSheetLayoutBinding.dropdownstatus.getText().toString().trim();

            //update database
            todoItem.setTitle(strtitle);
            todoItem.setDescription(strDes);
            todoItem.setCreatedDate(credate);
            todoItem.setCompletedDate(comdate);
            todoItem.setStatus(strStt);

            todoItemViewModel.updateItem(todoItem);
            Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        }
    }

    private boolean validation() throws ParseException {
        boolean check = true;

        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edttitle.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edttitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtdescription.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtdescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcreatedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtcreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtcompletedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtcompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (updateBottomSheetLayoutBinding.dropdownstatus.getText().toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.dropdownstatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }
        Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(updateBottomSheetLayoutBinding.edtcreatedDate.getText().toString().trim());
        Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(updateBottomSheetLayoutBinding.edtcompletedDate.getText().toString().trim());
        assert credate != null;
        if (credate.compareTo(comdate) > 0) {
            updateBottomSheetLayoutBinding.edtcompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }
}
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

import com.example.todoapp.Adapter.TabItemBottomSheetAdapter;
import com.example.todoapp.R;
import com.example.todoapp.databinding.FragmentMainBottomSheetBinding;
import com.example.todoapp.databinding.UpdateBottomSheetLayoutBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class MainBottomSheetFragment extends Fragment {

    private final TodoItem todoItem = new TodoItem();
    private BottomSheetDialog bottomSheetDialog;
    private FragmentMainBottomSheetBinding fragmentMainBinding;
    private UpdateBottomSheetLayoutBinding updateBottomSheetLayoutBinding;
    private MaterialDatePicker<Long> datePickerCompleted;
    private MaterialDatePicker<Long> datePickerCreated;
    private TodoItemViewModel todoItemViewModel;

    public MainBottomSheetFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //set shared element back for recylerview
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

        fragmentMainBinding.btnAdd.setOnClickListener(view1 ->
                //click button add to show bottom sheet add new item todo
                addItemBottomSheet(parentView));

        ViewPager2 viewPager2 = requireView().findViewById(R.id.vpg);
        fragmentMainBinding.vpg.setAdapter(new TabItemBottomSheetAdapter(requireActivity(), todoItemViewModel));
        TabLayout tabLayout = requireView().findViewById(R.id.tlomenu);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("All");
                    break;
                case 1:
                    tab.setText("Pending");
                    break;
                case 2:
                    tab.setText("Completed");
                    break;
            }
        });

        tabLayoutMediator.attach();
        fragmentMainBinding.svSearch.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            fragmentMainBinding.searchBar.setText(fragmentMainBinding.svSearch.getText());
            fragmentMainBinding.svSearch.hide();
            todoItemViewModel.getStringMutableLiveData().postValue(Objects.requireNonNull(fragmentMainBinding.svSearch.getText()).toString());
            return false;
        });

        todoItemViewModel.getListMutableLiveDataCheck().observe(requireActivity(), longs -> fragmentMainBinding.btnclearall.setEnabled(longs.size() > 0));
        fragmentMainBinding.btnclearall.setOnClickListener(view12 -> clearItem());
    }


    private void addItemBottomSheet(ViewGroup parentView) {
        if (updateBottomSheetLayoutBinding == null) {
            updateBottomSheetLayoutBinding = UpdateBottomSheetLayoutBinding.inflate(LayoutInflater.from(parentView.getContext()), parentView, false);
            bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(updateBottomSheetLayoutBinding.getRoot());

            updateBottomSheetLayoutBinding.edtcompletedDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            updateBottomSheetLayoutBinding.edtcreatedDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);

            String[] type = new String[]{"pending", "completed"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, R.id.txtstyle, type);
            updateBottomSheetLayoutBinding.dropdownstatus.setAdapter(adapter);
            if (datePickerCreated.isAdded()) {
                return;
            }

            updateBottomSheetLayoutBinding.btnAdd.setOnClickListener(view1 -> {
                try {
                    addItem();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            });
            updateBottomSheetLayoutBinding.btnclear.setOnClickListener(view12 -> clearText());

            updateBottomSheetLayoutBinding.edtcreatedDate.setOnClickListener(view13 -> {
                if (datePickerCreated.isAdded()) {
                    return;
                }
                datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
                datePickerCreated.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = format.format(calendar.getTime());
                    updateBottomSheetLayoutBinding.edtcreatedDate.setText(formattedDate);
                });
            });

            //create datePicker
            updateBottomSheetLayoutBinding.edtcompletedDate.setOnClickListener(view14 -> {
                if (datePickerCompleted.isAdded()) {
                    return;
                }
                datePickerCompleted.show(getParentFragmentManager(), "Material_Date_Picker");
                datePickerCompleted.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis((Long) selection);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = format.format(calendar.getTime());
                        updateBottomSheetLayoutBinding.edtcompletedDate.setText(formattedDate);
                    }
                });
            });
        }
        // Opt in to perform swipe to dismiss animation when dismissing bottom sheet dialog.
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    //show dialog to comfirm clear all item choiced
    private void clearItem() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Confirm Clear All").setMessage("Are you sure?").setPositiveButton("Yes", (dialogInterface, i) -> {
            todoItemViewModel.clearItem();
            Toast.makeText(getActivity(), "Clear successfully", Toast.LENGTH_SHORT).show();
        }).setNegativeButton("No", null).show();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMainBinding = FragmentMainBottomSheetBinding.inflate(inflater, container, false);

        View mView = fragmentMainBinding.getRoot();
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        datePickerCompleted = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();


        fragmentMainBinding.setMainFragViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }

    private void addItem() throws ParseException {
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

            todoItemViewModel.addItem(todoItem);
            clearText();
            Toast.makeText(getActivity(), "Add success", Toast.LENGTH_SHORT).show();
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
            updateBottomSheetLayoutBinding.tilcompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }

    private void clearText() {
        updateBottomSheetLayoutBinding.edttitle.setText("");
        updateBottomSheetLayoutBinding.edttitle.setError(null);
        updateBottomSheetLayoutBinding.edtdescription.setText("");
        updateBottomSheetLayoutBinding.edtdescription.setError(null);
        updateBottomSheetLayoutBinding.edtcreatedDate.setText("");
        updateBottomSheetLayoutBinding.edtcreatedDate.setError(null);
        updateBottomSheetLayoutBinding.edtcompletedDate.setText("");
        updateBottomSheetLayoutBinding.edtcompletedDate.setError(null);
        updateBottomSheetLayoutBinding.dropdownstatus.setText("", false);
        updateBottomSheetLayoutBinding.dropdownstatus.setError(null);
    }
}
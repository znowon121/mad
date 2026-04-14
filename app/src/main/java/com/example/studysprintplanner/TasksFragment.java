package com.example.studysprintplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TasksFragment extends Fragment {
    private EditText editTaskInput;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    private StudyStatsManager studyStatsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTaskInput = view.findViewById(R.id.editTaskInput);
        ListView listViewTasks = view.findViewById(R.id.listViewTasks);
        Button buttonAddTask = view.findViewById(R.id.buttonAddTask);
        studyStatsManager = new StudyStatsManager(requireContext());

        taskList = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(taskAdapter);

        buttonAddTask.setOnClickListener(v -> addTask());

        listViewTasks.setOnItemLongClickListener((parent, itemView, position, id) -> {
            taskList.remove(position);
            studyStatsManager.addCompletedTask();
            taskAdapter.notifyDataSetChanged();
            Toast.makeText(requireContext(), "Task completed", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void addTask() {
        String taskName = editTaskInput.getText().toString().trim();
        if (taskName.isEmpty()) {
            editTaskInput.setError("Please enter a task");
            editTaskInput.requestFocus();
            return;
        }

        editTaskInput.setError(null);
        taskList.add(taskName);
        taskAdapter.notifyDataSetChanged();
        editTaskInput.setText("");
    }
}

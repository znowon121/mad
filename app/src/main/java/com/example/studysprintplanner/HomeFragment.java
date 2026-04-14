package com.example.studysprintplanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public interface HomeActions {
        void onStartFocusRequested();
    }

    private HomeActions homeActions;
    private StudyStatsManager studyStatsManager;
    private TextView textHomeTasksValue;
    private TextView textHomeSessionsValue;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActions) {
            homeActions = (HomeActions) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeActions = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studyStatsManager = new StudyStatsManager(requireContext());

        Button buttonStartFromHome = view.findViewById(R.id.buttonStartFromHome);
        textHomeTasksValue = view.findViewById(R.id.textHomeTasksValue);
        textHomeSessionsValue = view.findViewById(R.id.textHomeSessionsValue);

        buttonStartFromHome.setOnClickListener(v -> {
            if (homeActions != null) {
                homeActions.onStartFocusRequested();
            }
        });

        refreshStats();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStats();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshStats();
        }
    }

    private void refreshStats() {
        if (textHomeTasksValue == null || textHomeSessionsValue == null || studyStatsManager == null) {
            return;
        }
        textHomeTasksValue.setText(String.valueOf(studyStatsManager.getCompletedTasks()));
        textHomeSessionsValue.setText(String.valueOf(studyStatsManager.getTotalSessions()));
    }
}

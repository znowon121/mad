package com.example.studysprintplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SummaryFragment extends Fragment {
    private TextView textCompletedTasksValue;
    private TextView textTotalSessionsValue;
    private TextView textStudyMinutesValue;
    private StudyStatsManager studyStatsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textCompletedTasksValue = view.findViewById(R.id.textCompletedTasksValue);
        textTotalSessionsValue = view.findViewById(R.id.textTotalSessionsValue);
        textStudyMinutesValue = view.findViewById(R.id.textStudyMinutesValue);
        studyStatsManager = new StudyStatsManager(requireContext());

        loadSummaryStats();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSummaryStats();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadSummaryStats();
        }
    }

    private void loadSummaryStats() {
        if (studyStatsManager == null || textCompletedTasksValue == null) {
            return;
        }

        textCompletedTasksValue.setText(String.valueOf(studyStatsManager.getCompletedTasks()));
        textTotalSessionsValue.setText(String.valueOf(studyStatsManager.getTotalSessions()));
        textStudyMinutesValue.setText(String.valueOf(studyStatsManager.getTotalStudyMinutes()));
    }
}

package com.example.studysprintplanner;

import android.content.Context;
import android.content.SharedPreferences;

public class StudyStatsManager {
    private static final String PREF_NAME = "study_stats";
    private static final String KEY_TOTAL_SESSIONS = "totalSessions";
    private static final String KEY_TOTAL_STUDY_MINUTES = "totalStudyMinutes";
    private static final String KEY_COMPLETED_TASKS = "completedTasks";

    private final SharedPreferences prefs;

    public StudyStatsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getTotalSessions() {
        return prefs.getInt(KEY_TOTAL_SESSIONS, 0);
    }

    public int getTotalStudyMinutes() {
        return prefs.getInt(KEY_TOTAL_STUDY_MINUTES, 0);
    }

    public int getCompletedTasks() {
        return prefs.getInt(KEY_COMPLETED_TASKS, 0);
    }

    public void addCompletedSession(int minutes) {
        int newTotalSessions = getTotalSessions() + 1;
        int newTotalMinutes = getTotalStudyMinutes() + minutes;

        prefs.edit()
                .putInt(KEY_TOTAL_SESSIONS, newTotalSessions)
                .putInt(KEY_TOTAL_STUDY_MINUTES, newTotalMinutes)
                .apply();
    }

    public void addCompletedTask() {
        int newCompletedTasks = getCompletedTasks() + 1;
        prefs.edit().putInt(KEY_COMPLETED_TASKS, newCompletedTasks).apply();
    }
}

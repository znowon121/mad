package com.example.studysprintplanner;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class TimerFragment extends Fragment {
    private static final boolean TEST_MODE = false;
    private static final long TEST_DURATION_IN_MILLIS = 10 * 1000;
    private static final long NORMAL_DURATION_IN_MILLIS = 25 * 60 * 1000;
    private static final int SESSION_MINUTES = 25;
    private static final int COMPLETION_TONE_DURATION_MS = 250;
    private static final String COMPLETED_TEXT = "Session completed";

    private TextView textViewTimer;
    private TextView textViewStatus;
    private Button buttonStart;
    private Button buttonPause;
    private StudyStatsManager studyStatsManager;
    private ToneGenerator toneGenerator;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = getInitialDurationInMillis();
    private boolean timerRunning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTimer = view.findViewById(R.id.textViewTimer);
        textViewStatus = view.findViewById(R.id.textViewStatus);
        buttonStart = view.findViewById(R.id.buttonStart);
        buttonPause = view.findViewById(R.id.buttonPause);
        Button buttonReset = view.findViewById(R.id.buttonReset);

        studyStatsManager = new StudyStatsManager(requireContext());
        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        buttonStart.setOnClickListener(v -> startTimer());
        buttonPause.setOnClickListener(v -> pauseTimer());
        buttonReset.setOnClickListener(v -> resetTimer());

        updateTimerText();
        updateButtons();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
    }

    private void startTimer() {
        if (timerRunning) {
            return;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timeLeftInMillis = 0;
                studyStatsManager.addCompletedSession(SESSION_MINUTES);
                textViewStatus.setText(COMPLETED_TEXT);
                if (toneGenerator != null) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, COMPLETION_TONE_DURATION_MS);
                }
                if (isAdded()) {
                    Toast.makeText(requireContext(), COMPLETED_TEXT, Toast.LENGTH_SHORT).show();
                }
                updateTimerText();
                updateButtons();
            }
        }.start();

        timerRunning = true;
        textViewStatus.setText("Timer is running...");
        updateButtons();
    }

    private void pauseTimer() {
        if (!timerRunning) {
            return;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timerRunning = false;
        textViewStatus.setText("Timer paused.");
        updateButtons();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        timerRunning = false;
        timeLeftInMillis = getInitialDurationInMillis();
        textViewStatus.setText("Ready to start.");
        updateTimerText();
        updateButtons();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewTimer.setText(formatted);
    }

    private void updateButtons() {
        buttonStart.setEnabled(!timerRunning && timeLeftInMillis > 0);
        buttonPause.setEnabled(timerRunning);
    }

    private long getInitialDurationInMillis() {
        return TEST_MODE ? TEST_DURATION_IN_MILLIS : NORMAL_DURATION_IN_MILLIS;
    }
}

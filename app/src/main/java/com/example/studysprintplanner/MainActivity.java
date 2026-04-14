package com.example.studysprintplanner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeActions {
    private static final String KEY_CURRENT_TAB = "current_tab";

    private TextView textScreenTitle;
    private BottomNavigationView bottomNav;
    private int currentTab = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textScreenTitle = findViewById(R.id.textScreenTitle);
        bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(KEY_CURRENT_TAB, R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment existing = getSupportFragmentManager().findFragmentByTag(tagFor(itemId));
            if (itemId == currentTab && existing != null) {
                return true;
            }
            showTab(itemId);
            return true;
        });

        bottomNav.setSelectedItemId(currentTab);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_TAB, currentTab);
    }

    @Override
    public void onStartFocusRequested() {
        bottomNav.setSelectedItemId(R.id.nav_timer);
    }

    private void showTab(int tabId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setReorderingAllowed(true);

        int[] tabs = {R.id.nav_home, R.id.nav_tasks, R.id.nav_timer, R.id.nav_summary};
        for (int id : tabs) {
            Fragment fragment = fragmentManager.findFragmentByTag(tagFor(id));
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }

        Fragment target = fragmentManager.findFragmentByTag(tagFor(tabId));
        if (target == null) {
            target = createFragment(tabId);
            transaction.add(R.id.fragmentContainer, target, tagFor(tabId));
        } else {
            transaction.show(target);
        }

        transaction.commit();

        currentTab = tabId;
        updateTitle(tabId);
    }

    private Fragment createFragment(int tabId) {
        if (tabId == R.id.nav_tasks) {
            return new TasksFragment();
        }
        if (tabId == R.id.nav_timer) {
            return new TimerFragment();
        }
        if (tabId == R.id.nav_summary) {
            return new SummaryFragment();
        }
        return new HomeFragment();
    }

    private String tagFor(int tabId) {
        if (tabId == R.id.nav_tasks) {
            return "tab_tasks";
        }
        if (tabId == R.id.nav_timer) {
            return "tab_timer";
        }
        if (tabId == R.id.nav_summary) {
            return "tab_summary";
        }
        return "tab_home";
    }

    private void updateTitle(int tabId) {
        if (tabId == R.id.nav_tasks) {
            textScreenTitle.setText("Tasks");
            return;
        }
        if (tabId == R.id.nav_timer) {
            textScreenTitle.setText("Timer");
            return;
        }
        if (tabId == R.id.nav_summary) {
            textScreenTitle.setText("Summary");
            return;
        }
        textScreenTitle.setText("Home");
    }
}

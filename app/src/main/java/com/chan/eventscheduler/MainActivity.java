package com.chan.eventscheduler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chan.eventscheduler.eventscheduler.VTEventScheduler;
import com.chan.eventscheduler.eventscheduler.VTEventTypes;
import com.chan.eventscheduler.eventscheduler.VTMethodTypes;
import com.chan.eventscheduler.eventscheduler.VTSubscribe;

public class MainActivity extends AppCompatActivity {

    VTEventScheduler vtEventScheduler;
    View root;

    private View.OnClickListener fabClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Bundle request = new Bundle();
            request.putString("fromView", "Chandran");
            vtEventScheduler.postEvent(VTEventScheduler.TAG, VTEventTypes.PRESENTER, DummyPresenter.ACTION_GET_TEXT, request);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(fabClickListener);

        vtEventScheduler = VTEventScheduler.getInstance();
        vtEventScheduler.register(this, VTEventScheduler.TAG);
        new DummyPresenter();
    }

    @VTSubscribe(eventType = VTEventTypes.VIEW, methodType = VTMethodTypes.RECEIVE_RESULT)
    public void onReceiveResult(int requestAction, Bundle bundle)
    {
        switch(requestAction)
        {
            case DummyPresenter.ACTION_GET_TEXT:
            {
                processDummyText(bundle);
            }
            break;
        }
    }

    private void processDummyText(Bundle bundle)
    {
        String fromPresenter = bundle.getString("fromPresenter");
        Snackbar.make(root, fromPresenter, Snackbar.LENGTH_LONG).show();
    }

}

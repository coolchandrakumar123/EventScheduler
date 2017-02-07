package com.chan.eventscheduler;

import android.os.Bundle;

import com.chan.eventscheduler.eventscheduler.VTEventScheduler;
import com.chan.eventscheduler.eventscheduler.VTEventTypes;
import com.chan.eventscheduler.eventscheduler.VTMethodTypes;
import com.chan.eventscheduler.eventscheduler.VTSubscribe;

/**
 * Created by chandra-1765 on 2/7/17.
 */

public class DummyPresenter
{
    VTEventScheduler vtEventScheduler;
    public static final int ACTION_GET_TEXT = 0;

    public DummyPresenter()
    {
        vtEventScheduler = VTEventScheduler.getInstance();
        vtEventScheduler.register(this, VTEventScheduler.TAG);
    }

    @VTSubscribe(eventType = VTEventTypes.PRESENTER, methodType = VTMethodTypes.PROCESS_REQUEST)
    public void processRequest(int requestAction, Bundle bundle)
    {
        switch(requestAction)
        {
            case DummyPresenter.ACTION_GET_TEXT:
            {
                sendRequest(requestAction, processStringAndSendBack(bundle));
            }
                break;
        }

    }

    private Bundle processStringAndSendBack(Bundle bundle)
    {
        String fromView = bundle.getString("fromView");
        String resultStr = fromView + " Added at Presenter";
        Bundle result = new Bundle();
        result.putString("fromPresenter", resultStr);
        return result;
    }

    private void sendRequest(int requestAction, Bundle bundle)
    {
        vtEventScheduler.postEvent(VTEventScheduler.TAG, VTEventTypes.VIEW, requestAction, bundle);
    }
}

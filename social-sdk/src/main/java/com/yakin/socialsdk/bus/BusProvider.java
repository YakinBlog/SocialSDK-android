package com.yakin.socialsdk.bus;

import com.yakin.socialsdk.utils.ThreadMgr;

import java.util.ArrayList;
import java.util.List;

public class BusProvider {

    private BusProvider(){ }

    private static class InstanceHolder{
        private static BusProvider sInstance = new BusProvider();
    }

    public static BusProvider getInstance(){
        return InstanceHolder.sInstance;
    }

    private static List<IBusListener> mListeners = new ArrayList<>();

    public void registerSSOListener(final IBusListener listener) {
        ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mListeners.add(listener);
            }
        });
    }

    public void unregisterSSOListener(final IBusListener listener) {
        ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mListeners.remove(listener);
            }
        });
    }

    public void notify(final BusEvent event) {
        ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                List<IBusListener> list = new ArrayList<>(mListeners);
                for (IBusListener listener: list) {
                    listener.onBusEvent(event);
                }
            }
        });
    }
}

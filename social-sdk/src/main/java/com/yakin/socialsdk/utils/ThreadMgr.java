package com.yakin.socialsdk.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseArray;

public class ThreadMgr {

    public static final int TYPE_UI = 0;
    public static final int TYPE_WORKER = 1;

    private static HandlerThread sWorkerThread;
    private static SparseArray<Handler> sMaps = new SparseArray();

    private synchronized static void ensureThreadCreated(int type){
        Handler handler = sMaps.get(type);
        if(handler == null){
            if(type == TYPE_UI) {
                sMaps.put(TYPE_UI, new Handler(Looper.getMainLooper()));
            } else if(type == TYPE_WORKER) {
                sWorkerThread = new HandlerThread("social-worker");
                sWorkerThread.start();
                sMaps.put(TYPE_WORKER, new Handler(sWorkerThread.getLooper()));
            }
        }
    }

    public synchronized static void destory(){
        if(sWorkerThread != null){
            sWorkerThread.quit();
            try {
                sWorkerThread.interrupt();
            }catch (Throwable throwable){
            }
            sWorkerThread = null;
        }
        sMaps.clear();
    }

    public synchronized static void postTask(int type, Runnable runnable){
        ensureThreadCreated(type);
        Handler handler = sMaps.get(type);
        if(handler != null){
            if(handler.getLooper() == Looper.myLooper()){
                runnable.run();
            }else {
                handler.post(runnable);
            }
        }
    }

    public synchronized static void postTask(int type, Runnable runnable, long delayed){
        ensureThreadCreated(type);
        Handler handler = sMaps.get(type);
        if(handler != null){
            handler.postDelayed(runnable, delayed);
        }
    }

    public synchronized static void dropTask(int type, Runnable runnable){
        ensureThreadCreated(type);
        Handler handler = sMaps.get(type);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}

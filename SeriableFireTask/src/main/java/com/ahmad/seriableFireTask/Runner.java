package com.ahmad.seriableFireTask;


import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


final public class Runner {

    private boolean All_Completed_Flag = false;
    private boolean Pause_All_Flag = false;
    private boolean Cancel_All_Flag = false;
    private boolean Resume_All_Flag = false;
    //private int Last_Runned_Seriable_index;


    ArrayList<Seriable> SeriableList;
    ArrayList<Seriable> PausedList = new ArrayList<Seriable>();
    ArrayList<Seriable> RunningList = new ArrayList<Seriable>();
    ArrayList<Seriable> SuccesList = new ArrayList<Seriable>();
    ArrayList<Seriable> FailedList = new ArrayList<Seriable>();
    ArrayList<Seriable> CancelledList = new ArrayList<Seriable>();
    ArrayList<Seriable> CompletedList = new ArrayList<Seriable>();
    private int concurrent = 1;
    public interface OnAllCompleted {
        public void onAllCompleted(ArrayList<Seriable> success,
                                   ArrayList<Seriable> failed,
                                   ArrayList<Seriable> cancelled,
                                   ArrayList<Seriable> completed
        );
    }

    public interface OnAllCancelledListener {
        public void onAllCancelledListener();
    }

    public interface OnAllRunningPausedListener {
        public void onAllRunningPaused();
    }

    public interface OnAllPauseResumed {
        public void onAllPausedResume();
    }

    OnAllRunningPausedListener onAllRunningPausedListener;
    OnAllCompleted onAllCompleted;
    OnAllCancelledListener onAllCancelledListener;
    OnAllPauseResumed onAllPauseResumed;

    public Runner(ArrayList<Seriable> seriableList) {
        SeriableList = seriableList;
    }

    // set Last reached callback, if last reached already >> run immediately
    public void setOnAllCompleted(OnAllCompleted onAllCompleted) {
        this.onAllCompleted = onAllCompleted;
        if (All_Completed_Flag) {
            onAllCompleted.onAllCompleted(SuccesList, FailedList, CancelledList, CompletedList);
        }
    }

    public void setOnAllRunningPausedListener(OnAllRunningPausedListener onAllRunningPausedListener) {
        this.onAllRunningPausedListener = onAllRunningPausedListener;
    }

    public void setOnAllCancelledListener(OnAllCancelledListener onAllCancelledListener) {
        this.onAllCancelledListener = onAllCancelledListener;
    }

    public void setOnAllPauseResumed(OnAllPauseResumed onAllPauseResumed) {
        this.onAllPauseResumed = onAllPauseResumed;
    }

    public void setConcurrent(int concurrent)  {
        if (concurrent <= 0) {
            concurrent = 1;
            return;
        }
        this.concurrent = concurrent;
    }

    private Seriable attachExtraListeners(final Seriable seriable) {
        final StorageTask task = (StorageTask) seriable.getTask();


        // If one Paused >> all will wait for it
        task.addOnPausedListener(new OnPausedListener<StorageTask.SnapshotBase>() {
            @Override
            public void onPaused(StorageTask.SnapshotBase taskSnapshot) {
                if (!PausedList.contains(seriable)) {
                    PausedList.add(seriable);
                }
                if (RunningList.contains(seriable)) {
                    RunningList.remove(seriable);
                }
                /*if (pausedListener != null) {
                    pausedListener.onPaused(taskSnapshot);
                }*/
                tryOnAllRunningPaused();
                if (!Pause_All_Flag) {
                    next();
                }
            }
        });

        task.addOnProgressListener(new OnProgressListener<StorageTask.SnapshotBase>() {
            @Override
            public void onProgress(StorageTask.SnapshotBase taskSnapshot) {
                if (PausedList.contains(seriable)) {   // if present in Paused List remove it
                    PausedList.remove(seriable);
                }

                if (!RunningList.contains(seriable)) {
                    RunningList.add(seriable);
                }
                /*if (progressListener != null) {
                    progressListener.onProgress(taskSnapshot);
                }*/
                if (  ! seriable.uploadSessionUriSentFlag  && seriable.onUploadSessionUriRecieved != null   ) {
                    Uri uri = ((UploadTask.TaskSnapshot) taskSnapshot).getUploadSessionUri();
                    if (uri != null) {
                        seriable.onUploadSessionUriRecieved.onUploadSessionUriRecieved(uri);
                        seriable.uploadSessionUriSentFlag = true;
                    }
                }

                /*
                UploadTask.TaskSnapshot  taskSnapshot1;
                taskSnapshot1.getUploadSessionUri()*/

                tryOnAllPausedResumed();
                next();
            }
        });

        // if Success >> move to next
        task.addOnSuccessListener(new OnSuccessListener<StorageTask.SnapshotBase>() {
            @Override
            public void onSuccess(StorageTask.SnapshotBase taskSnapshot) {
                if (PausedList.contains(seriable)) {   // if present in Paused List remove it
                    PausedList.remove(seriable);
                }

                if (RunningList.contains(seriable)) {
                    RunningList.remove(seriable);
                }

                if (!SuccesList.contains(seriable)) {
                    SuccesList.add(seriable);
                }

                /*if (successListener != null) {
                    successListener.onSuccess(taskSnapshot);
                }*/
                next();
            }
        });

        task.addOnCompleteListener(new OnCompleteListener<StorageTask.SnapshotBase>() {
            @Override
            public void onComplete(@NonNull Task<StorageTask.SnapshotBase> task) {
                if (PausedList.contains(seriable)) {   // if present in Paused List remove it
                    PausedList.remove(seriable);
                }

                if (RunningList.contains(seriable)) {
                    RunningList.remove(seriable);
                }

                if (!CompletedList.contains(seriable)) {
                    CompletedList.add(seriable);
                }
                /*if (completeListener != null) {
                    completeListener.onComplete(task);
                }*/
                tryOnAllCompleted();
                next();
            }
        });

        // one cancelled all will wait
        task.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                if (PausedList.contains(seriable)) {   // if present in Paused List remove it
                    PausedList.remove(seriable);
                }

                if (RunningList.contains(seriable)) {
                    RunningList.remove(seriable);
                }

                if (!CancelledList.contains(seriable)) {
                    CancelledList.add(seriable);
                }
                if (!CompletedList.contains(seriable)) {
                    CompletedList.add(seriable);
                }
                /*if (canceledListener != null) {
                    canceledListener.onCanceled();
                }*/

                if (!Cancel_All_Flag) {
                    next();
                }
                tryOnAllCancelled();
                //tryOnAllCompleted();
                //run( SeriableList.indexOf( seriable ) + 1 );
            }
        });

        // one Failed >> move to next
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (PausedList.contains(seriable)) {   // if present in Paused List remove it
                    PausedList.remove(seriable);
                }

                if (RunningList.contains(seriable)) {
                    RunningList.remove(seriable);
                }

                if (!FailedList.contains(seriable)) {
                    FailedList.add(seriable);
                }

                tryOnAllCompleted();
                /*if (failureListener != null) {
                    failureListener.onFailure(e);
                }*/
                next();
            }
        });
        return seriable;
    }

    public Runner pauseAllRunning() {
        Pause_All_Flag = true;
        // try to pause all ,
        // pause will check for null task
        // try to pause all ,  ot in running list only

        for (Seriable seriable : SeriableList) {
            seriable.pause();
        }

        return this;
    }

    private void tryOnAllRunningPaused() {
        if (!Pause_All_Flag) {
            return;
        }

        if (RunningList.size() > 0) {
            return;
        }

        Pause_All_Flag = false;

        if (this.onAllRunningPausedListener != null) {
            onAllRunningPausedListener.onAllRunningPaused();
        }
    }

    public Runner resumeAllPaused() {
        //Pause_All_Flag = false;
        Resume_All_Flag = true;
        // try to resume all in paused list,
        for (Seriable seriable : PausedList) {
            seriable.resume();
        }
        //tryOnAllPausedResumed(cachedList);
        return this;
    }

    private void tryOnAllPausedResumed() {
        if (!Resume_All_Flag) {
            return;
        }
        if (PausedList.size() > 0) {
            return;
        }
        Resume_All_Flag = false;
        if (onAllPauseResumed != null) {
            onAllPauseResumed.onAllPausedResume();
        }
    }


    public Runner cancelAll() {
        Cancel_All_Flag = true;
        for (Seriable seriable : SeriableList){
            if (seriable.getTask() == null) {
                RunningList.add(seriable);
                seriable.run();
                attachExtraListeners(seriable);
                seriable.cancel();
            }
            if (seriable.getTask() != null){
                // cancel task >> task Oncancelled Listener will work
                seriable.cancel();
            }
        }
        return this;
    }

    private void tryOnAllCancelled() {
        if (!Cancel_All_Flag) {
            return;
        }
        for (Seriable seriable : SeriableList) {
            if (seriable.getTask() == null) { // not started
                continue;
            } else if (!seriable.getTask().isCanceled()) {
                return;
            }
        }
        if (this.onAllCancelledListener != null) {
            onAllCancelledListener.onAllCancelledListener();
        }
        Cancel_All_Flag = false;
    }

    private void tryOnAllCompleted() {
        if (All_Completed_Flag ){  // invoked before
            return;
        }

        for (Seriable seriable : SeriableList) {
            if (seriable.getTask() == null) { // not started
                return;
            } else if (!seriable.getTask().isComplete()) {
                return;
            }
        }
        All_Completed_Flag = true;
        if (onAllCompleted != null) {
            this.onAllCompleted.onAllCompleted(SuccesList, FailedList, CancelledList, CompletedList);
        }
    }

    private void run(int index) {
        final Seriable seriable = SeriableList.get(index);
        seriable.run();
        attachExtraListeners(seriable);
    }

    public Runner start() {
        //Log.i("", SeriableList.toString() + SeriableList.size());
        if (SeriableList.size() == 0) {
            tryOnAllCompleted();
            return this;
        }
        run(0);
        return this;
    }

    private Runner next() {
        if (Pause_All_Flag || Cancel_All_Flag) {
            return this;
        }
        int running = RunningList.size();
        if (  running >= concurrent) {
            return this;
        }
        int count = SeriableList.size();
        for (int x = 0; x < count; x++) {
            Seriable seriable = SeriableList.get(x);
            if (seriable.getTask() != null){
                continue;
            }
            if ( RunningList.contains(seriable)) {
                continue;
            } else {
                RunningList.add(seriable);
                run(x);
                return this;
            }
        }
        return this;
    }
}

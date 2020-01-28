package com.ahmad.seriableFireTask;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;

import java.util.concurrent.Executor;

public class Listeners {

    OnStartListener startListener;
    //private Executor startListenerExecutor;

    private OnPausedListener pausedListener;
    private Activity pausedListenerActivity;
    private Executor pausedListenerExecutor;

    OnProgressListener progressListener;
    private Activity progressListenerActivity;
    private Executor progressListenerExecutor;

    OnSuccessListener successListener;
    private Activity successListenerActivity;
    private Executor successListenerExecutor;

    OnCompleteListener completeListener;
    private Activity completeListenerActivity;
    private Executor completeListenerExceutor;

    OnCanceledListener canceledListener;
    private Activity canceledListenerActivity;
    private Executor canceledListenerExecutor;

    OnFailureListener failureListener;
    private Activity failureListenerActivity;
    private Executor failureListenerExecitor;

    SeriableUpload.OnUploadSessionUriRecieved onUploadSessionUriRecieved ;


    public interface OnStartListener {
        public void onStartListener(int index);
    }


    public OnStartListener getStartListener() {
        return startListener;
    }

    public OnPausedListener getPausedListener() {
        return pausedListener;
    }

    public OnProgressListener getProgressListener() {
        return progressListener;
    }

    public OnSuccessListener getSuccessListener() {
        return successListener;
    }

    public OnCompleteListener getCompleteListener() {
        return completeListener;
    }

    public OnCanceledListener getCanceledListener() {
        return canceledListener;
    }

    public OnFailureListener getFailureListener() {
        return failureListener;
    }


    public void setStartListener(OnStartListener startListener) {
        this.startListener = startListener;
    }

    public void setStartListener( Executor executor, OnStartListener startListener) {
        this.startListener = startListener;
        //this.startListenerExecutor = executor;
    }

    public void setProgressListener(OnProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setProgressListener(Activity activity, OnProgressListener progressListener) {
        this.progressListenerActivity = activity;
        this.progressListener = progressListener;
        this.progressListenerExecutor = null;
    }

    public void setProgressListener(Executor executor, OnProgressListener progressListener) {
        this.progressListenerActivity = null;
        this.progressListener = progressListener;
        this.progressListenerExecutor = executor;
    }


    public void setCanceledListener(OnCanceledListener canceledListener) {
        this.canceledListener = canceledListener;
    }

    public void setCanceledListener( Activity activity, OnCanceledListener canceledListener) {
        this.canceledListener = canceledListener;
        this.canceledListenerActivity = activity;
        this.canceledListenerExecutor = null;
    }

    public void setCanceledListener(Executor executor, OnCanceledListener canceledListener) {
        this.canceledListener = canceledListener;
        this.canceledListenerActivity = null;
        this.canceledListenerExecutor = executor;
    }

    public void setCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void setCompleteListener( Activity activity, OnCompleteListener completeListener) {
        this.completeListener = completeListener;
        this.completeListenerActivity = activity;
        this.completeListenerExceutor = null;
    }
    public void setCompleteListener( Executor executor, OnCompleteListener completeListener) {
        this.completeListener = completeListener;
        this.completeListenerActivity = null;
        this.completeListenerExceutor = executor;
    }

    public void setSuccessListener(OnSuccessListener successListener) {
        this.successListener = successListener;
    }
    public void setSuccessListener( Activity activity,  OnSuccessListener successListener) {
        this.successListener = successListener;
        this.successListenerActivity = activity;
        this.successListenerExecutor  = null;
    }
    public void setSuccessListener( Executor executor, OnSuccessListener successListener) {
        this.successListener = successListener;
        this.successListenerActivity = null;
        this.successListenerExecutor  = executor;
    }

    public void setFailureListener(OnFailureListener failureListener) {
        this.failureListener = failureListener;
    }


    public void setFailureListener( Activity activity, OnFailureListener failureListener) {
        this.failureListener = failureListener;
        this.failureListenerActivity = activity;
        this.failureListenerExecitor = null;
    }


    public void setFailureListener( Executor executor, OnFailureListener failureListener) {
        this.failureListener = failureListener;
        this.failureListenerActivity = null;
        this.failureListenerExecitor = executor;
    }

    public void setPausedListener(OnPausedListener pausedListener) {
        this.pausedListener = pausedListener;
    }

    public void setPausedListener(Activity activity, OnPausedListener pausedListener) {
        this.pausedListener = pausedListener;
        this.pausedListenerActivity = activity;
        this.pausedListenerExecutor = null;
    }

    public void setPausedListener( Executor executor , OnPausedListener pausedListener) {
        this.pausedListener = pausedListener;
        this.pausedListenerActivity = null;
        this.pausedListenerExecutor = executor;

    }

    protected void attachListeners(StorageTask Task) {
        // TODO if attempt to add listener after start, throw exception;

        /*if (startListener != null ){
            startListener.onStartListener(index);
        }*/

        if (pausedListener != null) {
            /*t.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {

                }
            })*/
            if ( pausedListenerActivity != null ){
                Task.addOnPausedListener( pausedListenerActivity,  new OnPausedListener() {
                    @Override
                    public void onPaused(Object o) {
                        pausedListener.onPaused(o);
                    }
                });

            }else if (pausedListenerExecutor != null){
                Task.addOnPausedListener( pausedListenerExecutor, new OnPausedListener() {
                    @Override
                    public void onPaused(Object o) {
                        pausedListener.onPaused(o);
                    }
                });

            }else {

                Task.addOnPausedListener(new OnPausedListener() {
                    @Override
                    public void onPaused(Object o) {
                        pausedListener.onPaused(o);
                    }
                });
            }

            /*Task.addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    pausedListener.onPaused(taskSnapshot);

                }
            });*/
        }
        if (progressListener != null) {

            if (progressListenerActivity != null){
                Task.addOnProgressListener( progressListenerActivity, new OnProgressListener() {
                    @Override
                    public void onProgress(Object o) {
                        progressListener.onProgress(o);
                    }
                });

            }else if (progressListenerExecutor != null){
                Task.addOnProgressListener(progressListenerExecutor, new OnProgressListener() {
                    @Override
                    public void onProgress(Object o) {
                        progressListener.onProgress(o);
                    }
                });

            }else {

                Task.addOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Object o) {
                        progressListener.onProgress(o);
                    }
                });
            }
            /*Task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressListener.onProgress(taskSnapshot);
                }
            });*/
        }
        if (successListener != null) {

            if (successListenerActivity != null){
                Task.addOnSuccessListener( successListenerActivity, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(o);
                    }
                });

            }else if (successListenerExecutor != null){
                Task.addOnSuccessListener( successListenerExecutor, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(o);
                    }
                });
            }else {

                Task.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(o);
                    }
                });
            }
            /*Task.addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    successListener.onSuccess( taskSnapshot);
                }
            });*/
        }
        if (completeListener != null) {

            if (completeListenerActivity != null){

                Task.addOnCompleteListener( completeListenerActivity, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        completeListener.onComplete(task);
                    }
                });

            }else if(  completeListenerExceutor != null){

                Task.addOnCompleteListener(completeListenerExceutor, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        completeListener.onComplete(task);
                    }
                });

            }else {

                Task.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        completeListener.onComplete(task);
                    }
                });
            }/*
            Task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    completeListener.onComplete(task);
                }
            });*/
        }
        if (failureListener != null) {

            if (failureListenerActivity != null ){
                Task.addOnFailureListener(  failureListenerActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
            }else if ( failureListenerExecitor != null ){
                Task.addOnFailureListener( failureListenerExecitor, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
            }else{
                Task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
            }


        }
        if (canceledListener != null) {

            if (canceledListenerActivity != null){
                Task.addOnCanceledListener( canceledListenerActivity, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        canceledListener.onCanceled();
                    }
                });

            }else if (canceledListenerExecutor != null){
                Task.addOnCanceledListener( canceledListenerExecutor, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        canceledListener.onCanceled();
                    }
                });

            }else {

                Task.addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        canceledListener.onCanceled();
                    }
                });
            }
        }
    }
}

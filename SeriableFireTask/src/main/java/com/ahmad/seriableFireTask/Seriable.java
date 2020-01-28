package com.ahmad.seriableFireTask;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


public class Seriable extends Listeners implements Runnable {
    int index;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();
    StorageTask Task;
    String cloudPath;
    boolean uploadSessionUriSentFlag;


    public enum TYPES {
        UPLOAD("UPLOAD"),
        DOWNLOAD("DOWNLOAD"),
        DBWRITE("DBWRITE");
        private String type;
        TYPES(String type) {
            this.type = type;
        }
    }
    TYPES type;

    public TYPES getType() {
        return type;
    }

    public void setType(TYPES type) {
        this.type = type;
    }

    public StorageTask getTask() {
        return Task;
    }

    public int getIndex() {
        return index;
    }

    public String getCloudPath() {
        return cloudPath;
    }

    public boolean pause() {
        if (Task != null) {
            return Task.pause();
        }
        return false;
    }

    public boolean resume() {
        if (Task != null) {
            return Task.resume();
        }
        return false;
    }
    public boolean cancel() {
        if (Task != null) {
            return Task.cancel();
        }
        return false;
    }

    @Override
    public void run(){
        if (startListener != null ){
            startListener.onStartListener(index);
        }
    }


}

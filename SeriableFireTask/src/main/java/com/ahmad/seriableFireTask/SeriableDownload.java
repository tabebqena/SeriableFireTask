package com.ahmad.seriableFireTask;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class SeriableDownload extends Seriable {
    private File localFile;

    public SeriableDownload() {
        setType(TYPES.DOWNLOAD);
    }

    public SeriableDownload(String cloudPath , File localFile) {
        this();
        this.localFile = localFile;
        this.cloudPath = cloudPath;
    }

    @Override
    public void run(){
        StorageReference ref = storageRef.child(cloudPath);

        if (localFile != null){
            Task = ref.getFile(localFile);

        }
        this.attachListeners(Task);
        super.run();


        /*islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        });*/


    }

}

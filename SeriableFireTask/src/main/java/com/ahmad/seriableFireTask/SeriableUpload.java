package com.ahmad.seriableFireTask;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

final class SeriableWrite extends Seriable {

}

final public class SeriableUpload extends Seriable   {

    public interface OnUploadSessionUriRecieved {
        public void onUploadSessionUriRecieved(Uri uri);
    }

    byte[] bytes;
    InputStream stream;
    Uri uri;
    StorageMetadata metadata;
    private Uri existingUploadUri;


    private SeriableUpload(){
        setType(TYPES.UPLOAD);
    }

    public SeriableUpload( @NonNull int index, @NonNull  byte[] bytes, @NonNull  String cloudPath) {
        this();
        this.index = index;
        this.bytes = bytes;
        this.cloudPath = cloudPath;
    }

    public SeriableUpload( @NonNull int index, @NonNull  byte[] bytes, @NonNull  String cloudPath, @NonNull  StorageMetadata metadata) {
        this();
        this.index = index;
        this.bytes = bytes;
        this.cloudPath = cloudPath;
        this.metadata = metadata;
    }

    public SeriableUpload( @NonNull int index,  @NonNull InputStream stream, @NonNull  String cloudPath) {
        this();
        this.index = index;
        this.stream = stream;
        this.cloudPath = cloudPath;
    }

    public SeriableUpload( @NonNull int index, @NonNull  InputStream stream,  @NonNull String cloudPath,  @NonNull StorageMetadata metadata) {
        this();
        this.index = index;
        this.stream = stream;
        this.cloudPath = cloudPath;
        this.metadata = metadata;

    }

    public SeriableUpload( @NonNull  int index,
                           @NonNull  Uri uri,
                           @NonNull  String cloudPath) {
        this();
        this.index = index;
        this.uri = uri;
        this.cloudPath = cloudPath;
    }

    public SeriableUpload( @NonNull int index,
                           @NonNull Uri uri,
                           @NonNull String cloudPath,
                           @NonNull StorageMetadata metadata ) {
        this();
        this.index = index;
        this.uri = uri;
        this.cloudPath = cloudPath;
        this.metadata = metadata;
        //Log.i("","in sertiabkle"+ index +" "+ uri+ cloudPath);
    }

    public SeriableUpload(@NonNull int index,
                          @NonNull Uri uri,
                          @NonNull String cloudPath,
                          @NonNull StorageMetadata metadata,
                          @NonNull Uri existingUploadUri ) {
        this();
        this.index = index;
        this.uri = uri;
        this.cloudPath = cloudPath;
        this.metadata = metadata;
        this.existingUploadUri = existingUploadUri;
    }

    public void setOnUploadSessionUriRecieved(
            OnUploadSessionUriRecieved onUploadSessionUriRecieved) {
        this.onUploadSessionUriRecieved = onUploadSessionUriRecieved;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public InputStream getStream() {
        return stream;
    }

    public Uri getUri() {
        return uri;
    }

    public StorageMetadata getMetadata() {
        return metadata;
    }

    public void getUploadSessionUri(){
        // UploadTask x ;
        // Uri sessionUri = taskSnapshot.getUploadSessionUri();

        //return ((UploadTask) Task)
    }

    public Uri getExistingUploadUri() {
        return existingUploadUri;
    }

    public void setExistingUploadUri(Uri existingUploadUri) {
        this.existingUploadUri = existingUploadUri;
    }

    @Override
    public void run(){
        StorageReference ref = storageRef.child(cloudPath);
        if (this.bytes != null){
            if (this.metadata != null){
                //UploadTask x = ref.putBytes(this.bytes);
                Task = ref.putBytes(this.bytes, this.metadata);
            }else {
                Task = ref.putBytes(this.bytes);
            }

        }else if(this.stream != null){
            if (this.metadata != null){
                //UploadTask x = ref.putStream(this.stream);
                Task = ref.putStream(this.stream, this.metadata);
            }else {
                Task = ref.putStream(this.stream);
            }

        }else if (this.uri != null){
            if (this.metadata != null){
                if (this.existingUploadUri != null ){
                    //UploadTask x = ref.putFile(this.uri);
                    Task = ref.putFile(this.uri, this.metadata, this.existingUploadUri);
                }else {
                    Task = ref.putFile(this.uri,
                            new StorageMetadata.Builder().build(),
                            this.existingUploadUri);
                }

            }else {
                Task = ref.putFile(this.uri);
            }
        }
        this.attachListeners(Task);
        super.run();
    }
}

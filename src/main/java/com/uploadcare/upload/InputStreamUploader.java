package com.uploadcare.upload;

import com.uploadcare.api.Client;
import com.uploadcare.api.File;
import com.uploadcare.data.UploadBaseData;
import com.uploadcare.urls.Urls;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by gautam on 19/9/14.
 */
public class InputStreamUploader implements Uploader {
    private final Client client;
    private final InputStream inputStream;
    private final String filename;

    public InputStreamUploader(Client client, InputStream inputStream, String filename) {
        this.client = client;
        this.inputStream = inputStream;
        this.filename = filename;
    }

    public File upload() throws UploadFailureException {
        URI uploadUrl = Urls.uploadBase();
        HttpPost request = new HttpPost(uploadUrl);
        MultipartEntity entity = new MultipartEntity();
        StringBody pubKeyBody = StringBody.create(client.getPublicKey(), "text/plain", null);
        entity.addPart("UPLOADCARE_PUB_KEY", pubKeyBody);
        entity.addPart("file", new InputStreamBody(inputStream, filename));
        request.setEntity(entity);
        String fileId = client.getRequestHelper().executeQuery(request, false, UploadBaseData.class).file;
        return client.getFile(fileId);
    }
}

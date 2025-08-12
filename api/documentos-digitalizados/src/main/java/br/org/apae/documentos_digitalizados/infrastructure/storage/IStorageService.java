package br.org.apae.documentos_digitalizados.infrastructure.storage;

import java.util.List;

public interface IStorageService {
  void createBucket(String bucketName);
  boolean isBucketExist(String bucketName);
  void deleteBucket(String bucketName);
  void uploadFile(String bucket, String path, byte[] file);
  List<String> listObject(String bucket, String prefix);
  byte[] getDocumentByFileName(String bucket, String objectName);
  List<String> generatePresignedUrls(String bucket, List<String> objectNames, int expirationTimeInHours);
  void deleteFile(String bucket, String objectName);
}

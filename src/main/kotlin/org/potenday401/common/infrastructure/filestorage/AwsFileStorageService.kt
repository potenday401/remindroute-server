package org.potenday401.common.infrastructure.filestorage

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectMetadata
import org.potenday401.common.domain.model.File
import org.potenday401.common.domain.model.FileStorageService
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.util.*

class AwsS3FileStorageService(
    region: String?,
    bucketName: String,
    accessKey: String?,
    secretKey: String?
) :
    FileStorageService {
    private val bucketName: String
    private val s3Client: AmazonS3

    init {
        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(credentials)).build()
        this.bucketName = bucketName
    }

    @Throws(IOException::class)
    override fun storeFile(directory: String, file: File): URL {
        val fileName = UUID.randomUUID().toString().replace("-".toRegex(), "") + "." + file.ext
        val metadata = ObjectMetadata()
        metadata.setContentLength(file.payload.size.toLong())
        val objectKey = "$directory/$fileName"
        s3Client.putObject(bucketName, objectKey, ByteArrayInputStream(file.payload), metadata)
        return s3Client.getUrl(bucketName, objectKey)
    }

    override fun deleteFileByPath(path: String?) {
        s3Client.deleteObject(bucketName, path)
    }

}

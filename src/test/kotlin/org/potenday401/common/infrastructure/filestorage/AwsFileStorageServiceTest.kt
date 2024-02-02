package org.potenday401.common.infrastructure.filestorage

import com.typesafe.config.ConfigFactory
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*


class AwsS3FileStorageServiceTest {

    private val testImagePath = "src/main/resources/test-image.jpg"
    private val awsS3Config = loadAwsS3Config()

    private val storageService = AwsS3FileStorageService(
        awsS3Config.region,
        awsS3Config.bucketName,
        awsS3Config.userAccessKey,
        awsS3Config.userAccessSecret
    )


    @Ignore("이 테스트는 실제로 s3에 파일을 생성합니다. 테스트가 필요할 때만 이 어노테이션을 제거하고 테스트를 실행하세요.")
    @Test
    @Throws(IOException::class)
    fun storeFile() {
        //given
        val directory = "1/1"
        val file = File(testImagePath)
        val imageFile =
            org.potenday401.common.domain.model.File(Files.readAllBytes(file.toPath()), "jpg")

        //when
        val url = storageService.storeFile("1/1", imageFile)

        //then
        println(url.toExternalForm())
        assertEquals(true, url.toExternalForm().contains(directory))
    }
}


fun loadAwsS3Config(): AwsS3Config {
    val config = ConfigFactory.parseFile(File("src/main/resources-local/application.conf")).resolve()
    val awsConfig = config.getConfig("ktor.aws.s3")

    return AwsS3Config(
        region = awsConfig.getString("region"),
        bucketName = awsConfig.getString("bucket_name"),
        userAccessKey = awsConfig.getString("user_access_key"),
        userAccessSecret = awsConfig.getString("user_access_secret")
    )
}

data class AwsS3Config(
    val region: String,
    val bucketName: String,
    val userAccessKey: String,
    val userAccessSecret: String
)
package org.potenday401

import ExposedPhotoPinRepository
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.potenday401.authentication.application.service.AuthenticationApplicationService
import org.potenday401.authentication.infrastructure.persistence.ExposedRefreshTokenRepository
import org.potenday401.common.infrastructure.filestorage.AwsS3FileStorageService
import org.potenday401.member.application.service.MemberApplicationService
import org.potenday401.member.infrastructure.persistence.ExposedEmailAuthenticationRepository
import org.potenday401.member.infrastructure.persistence.ExposedMemberRepository
import org.potenday401.photopin.application.service.PhotoPinApplicationService
import org.potenday401.photopin.infrastructure.persistence.PhotoPinQueries
import org.potenday401.plugins.*
import org.potenday401.tag.application.service.TagApplicationService
import org.potenday401.tag.infrastructure.persistence.ExposedTagRepository


fun main() {
    val applicationConfig = ConfigFactory.load()
    val port = applicationConfig.getInt("ktor.deployment.port")

    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val memberRepository = ExposedMemberRepository()
    val emailAuthenticationRepository = ExposedEmailAuthenticationRepository()
    val refreshTokenRepository = ExposedRefreshTokenRepository()
    val memberAppService = MemberApplicationService(memberRepository, emailAuthenticationRepository)
    val authAppService = AuthenticationApplicationService(refreshTokenRepository)

    val tagRepository = ExposedTagRepository()
    val tagAppService = TagApplicationService(tagRepository)

    val config = ConfigFactory.load()
    val s3Region = config.getString("ktor.aws.s3.region")
    val s3BucketName = config.getString("ktor.aws.s3.bucket_name")
    val s3UserAccessKey= config.getString("ktor.aws.s3.user_access_key")
    val s3UserAccessSecret = config.getString("ktor.aws.s3.user_access_secret")

    val fileStorageService = AwsS3FileStorageService(
        s3Region,
        s3BucketName,
        s3UserAccessKey,
        s3UserAccessSecret)
    val photoPinRepository = ExposedPhotoPinRepository()
    val photoPinAppService = PhotoPinApplicationService(photoPinRepository, fileStorageService)
    val photoPinQueries = PhotoPinQueries(tagRepository)

    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureRouting(tagAppService, memberAppService, photoPinAppService, photoPinQueries, authAppService)
    configureSwagger()
}
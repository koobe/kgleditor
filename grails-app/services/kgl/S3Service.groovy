package kgl

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import grails.transaction.Transactional
import grails.util.Environment

import javax.annotation.PostConstruct

@Transactional
class S3Service {

    def grailsApplication

    String accessKey
    String secretKey
    String bucket
    BasicAWSCredentials credentials

    @PostConstruct
    void initialize() {
        accessKey = grailsApplication.config.aws.credentials.accessKey
        secretKey = grailsApplication.config.aws.credentials.secretKey
        bucket = grailsApplication.config.aws.s3.bucket
        credentials = new BasicAWSCredentials(accessKey, secretKey)
    }

    boolean doesBucketExist() {
        doesBucketExist(bucket)
    }

    boolean doesBucketExist(bucket) {
        getClient().doesBucketExist(bucket)
    }

    void createBucket() {
        createBucket(bucket)
    }

    void createBucket(bucket) {
        log.info "Create S3 bucket: ${bucket}"
        getClient().createBucket(bucket)
    }

    def upload(S3File s3file, InputStream inputStream) {

        log.info "Upload S3File ${s3file.originalFilename} (Content-Type: ${s3file.contentType}, Content-Length: ${s3file.contentLength} bytes)"

        if (!s3file.bucket) {
            s3file.bucket = bucket
        }

        log.info "Save to bucket: ${s3file.bucket}"

        if (!s3file.objectKey) {
            s3file.objectKey = "${Environment.current.name}/${s3file.ownerId}/${UUID.randomUUID()}/${s3file.originalFilename}"
            log.info "Auto-create s3 object objectKey: ${s3file.objectKey}"
        }

        def metadata = new ObjectMetadata()
        metadata.contentLength = s3file.contentLength
        metadata.contentType = s3file.contentType

        def request = new PutObjectRequest(s3file.bucket, s3file.objectKey, inputStream, metadata)

        if (s3file.isPublic) {
            log.info "Make Public"
            // TODO: Set ACL to public

            request.setCannedAcl(CannedAccessControlList.PublicRead)
        }

        def result = getClient().putObject(request)

        s3file.url = getClient().getUrl(s3file.bucket, s3file.objectKey)
        s3file.resourceUrl = getClient().getResourceUrl(s3file.bucket, s3file.objectKey)

        s3file.hasBeenUploaded = true
    }

    def getClient() {
        new AmazonS3Client(credentials)
    }
}

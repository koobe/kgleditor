package kgl

import com.amazonaws.services.s3.model.AmazonS3Exception
import grails.converters.JSON

class BookController {

    private final static String UTF8_BOM = "\uFEFF"

    def s3Service

    def index() {}


    def legacy() {
        [legacies: Legacy.list()]
    }

    /**
     * Import KGL B2B EPUB
     */
    def legacyImport() {
        def result = []
        def files = []

        //def hex = (0..15).collect { Integer.toHexString(it) }

        def imported = []
        Legacy.list().each {
            imported << it.s3key
        }

        s3Service.listObjects('koobecloudepub', 'books-2x/epub/unzip', '.epub/')?.each {
            if (!imported.contains(it)) {
                files << it
            }
            else {
                log.info "Skip: ${it}"
            }
        }

        s3Service.listObjects('koobecloudepub', 'books-4x/epub', '.epub/')?.each {
            if (!imported.contains(it)) {
                files << it
            }
            else {
                log.info "Skip: ${it}"
            }
        }

        [
                files: files
        ]
    }

    def legacyFetchXML() {
        def result = [:]
        def file = params.file
        def message = ''
        def successful = false

        result << [file: file]

        result << [path: "${file}OEBPS/contents.xml"]

        String[] tokens = file.split('/')
        def key = tokens[tokens.size()-1].replace('.epub', '')

        def legacy = Legacy.findByKey(key)

        if (!legacy) {

            try {
                def opf = s3Service.getObject('koobecloudepub', "${file}OEBPS/content.opf").text

                // remove BOM
                if (opf.startsWith(UTF8_BOM)) {
                    opf = opf.substring(1)
                }

                def xml = new XmlSlurper().parseText(opf)

                xml.declareNamespace(dc: "http://purl.org/dc/elements/1.1/")

                legacy = new Legacy()
                legacy.key = key
                legacy.s3key = file
                legacy.metadata = ''
                legacy.opf = opf
                legacy.title = xml.metadata.'dc:title'
                legacy.creator = xml.metadata.'dc:creator'
                legacy.publisher = xml.metadata.'dc:publisher'
                legacy.contributor = xml.metadata.'dc:contributor'
                legacy.date = xml.metadata.'dc:date'
                legacy.language = xml.metadata.'dc:language'
                legacy.subject = xml.metadata.'dc:subject'
                legacy.description = xml.metadata.'dc:description'

                legacy.save flush: true

                successful = true
                message = "「${legacy.title}」已經匯入！"
            }
            catch (AmazonS3Exception ex) {
                message = ex.message
            }
        }

        result << [legacy: legacy]

        result << [successful: successful]

        result << [message: message]

        render result as JSON
    }
}

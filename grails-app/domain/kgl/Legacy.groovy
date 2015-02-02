package kgl

class Legacy {

    String key

    String s3key

    String metadata

    String title

    String description

    String opf

    String creator

    String contributor

    String date

    String language

    String subject

    String publisher

    static mapping = {
        opf type: 'text'
    }

    static constraints = {
        description maxSize: 10 * 1024
    }
}
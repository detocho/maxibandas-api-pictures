package Pictures

import java.text.MessageFormat
import org.apache.ivy.plugins.conflict.ConflictManager
import grails.converters.*
import Pictures.exceptions.NotFoundException
import Pictures.exceptions.ConflictException
import Pictures.exceptions.BadRequestException

class PictureService {

    static transactional = 'mongo'

    def getPictures (def pictureId){

        Map jsonResult = [:]

        if (!pictureId){

            throw new NotFoundException("You must provider pictureId")
        }

        def picture = Picture.findById(pictureId)

        if (!picture){

            throw new NotFoundException("The Pciture with pictureId = "+pictureId+" not found")
        }

        jsonResult = buildSizes(picture)

        jsonResult
    }

    def buildSizes(def picture){

        Map jsonResult = [:]

        def results = []

        def BUILD_SIZE_MAP = [

                zoom:'origin',
                listados:'200X60',
                detalle:'600X400',
                gallery:'100X75',
                galleryAdmin:'60X60'

        ]

        BUILD_SIZE_MAP.each{ key, value ->

            def url = picture.url.replaceAll('origin',value)
            def size = value
            def secureUrl = ''

            results.add(

                    size        : size,
                    url         : url,
                    secure_url  : secureUrl
            )

        }

        jsonResult.id = picture.id
        jsonResult.pictures = results

        jsonResult

    }
}

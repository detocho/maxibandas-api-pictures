package Pictures

import java.text.MessageFormat
import org.apache.ivy.plugins.conflict.ConflictManager

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
//import org.apache.http.entity.mime.HttpMultipartMode
//import org.apache.http.entity.mime.MultipartEntity
//import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.DefaultHttpClient

import grails.converters.*
import Pictures.exceptions.NotFoundException
import Pictures.exceptions.ConflictException
import Pictures.exceptions.BadRequestException

class PictureService {


    static transactional = 'mongo'

    def burningImageService
    def domainMainPicture = 'http://localhost:8080/pictures/'
    def urlMainPicture

    def BUILD_SIZE_MAP = [

            zoom:'origin',
            listados:'200X160',
            detalle:'800X600',
            gallery:'100X75',
            galleryAdmin:'60X60'

    ]

    def getPictures (def pictureId){

        Map jsonResult = [:]

        if (!pictureId){

            throw new NotFoundException("You must provider pictureId")
        }

        def picture = Picture.findById(pictureId)

        if (!picture){

            throw new NotFoundException("The Picture with pictureId = "+pictureId+" not found")
        }

        jsonResult = buildSizes(picture)

        jsonResult
    }


    def postPictures(def webrootDir, def file){

        def resultsPictures = []

        if (!file){

            throw new NotFoundException("You must provider file picture")
        }


        def newPicture =  new Picture(

                url: 'http://dominio/uploads/fotoProcess.jpg',
                size:'origin',
                secureUrl: ''

        ).save()


        if (!newPicture){

            throw new BadRequestException("Picture not created")
        }


        def pictureId = newPicture.id

        def folderTarget = folderToSave(webrootDir)
        def fileName = "MB"+pictureId+"-origin." + file.getContentType().replaceAll("image/","")
        urlMainPicture = urlMainPicture+fileName


        File fileDestiny = new File(folderTarget + fileName)
        file.transferTo(fileDestiny)



        burningImageService.doWith(fileDestiny.toString(), folderTarget)
                .execute ("MB"+pictureId+'-800X600', {
            it.scaleApproximate(800,600)
            //it.watermark(pathToWatermark, ['right': 10, 'bottom': 10])
        })
                .execute ("MB"+pictureId+'-200X160',{
            it.scaleApproximate(200,160)
        })
                .execute ("MB"+pictureId+'-100X75',{
            it.scaleApproximate(100,75)
        })
                .execute ("MB"+pictureId+'-60X60',{
            it.scaleApproximate(60,60)
        })


        newPicture.url = urlMainPicture
        newPicture.save()


        resultsPictures= buildSizes(newPicture)

        resultsPictures
    }

    def deletePicture(def pictureId, def webrootDir){


        Map result = [:]

        if (!pictureId){

            throw new NotFoundException("You must provider pictureId")

        }

        def picture = Picture.findById(pictureId)

        if (!picture){

            throw new NotFoundException("The picture with pictureId="+pictureId+" not found")
        }

        def nameFilePicture = picture.url.split("uploads")[1]
        webrootDir = webrootDir+'uploads'

        BUILD_SIZE_MAP.each{ key, value ->

            if(new File(webrootDir+nameFilePicture.replaceAll('origin', value)).delete()){

            }
        }

        picture.delete()

        result.message =  "Deleted picture"

        result

    }

    def putPicture (def pictureId, def webrootDir, def file ){

        def resultPictures = []

        if (!file){

            throw new BadRequestException("You must provider file picture")
        }

        def result = deletePicture(pictureId, webrootDir)

        resultPictures = postPictures(webrootDir, file)

        resultPictures

    }


    def folderToSave(webrootDir){

        def cal = Calendar.instance
        def year = cal.get(Calendar.YEAR)
        def month = cal.get(Calendar.MONTH) + 1 // por que los meses van de 0  al 11

        def folderName = "uploads/" + year +"/"+ month + "/"


        File folderDate = new File(webrootDir,folderName);

        if (!folderDate.exists())
        {

            folderDate.mkdirs();

        }

        def forderTarget  = webrootDir + folderName

        urlMainPicture = domainMainPicture+folderName

        forderTarget
    }


    def buildSizes(def picture){

        Map jsonResult = [:]

        def results = []


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

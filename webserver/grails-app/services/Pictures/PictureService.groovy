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

    def getPicturesByBand(def bandId){

        def resultsPictures = []

        if (!bandId){

            throw new NotFoundException("You must provider bandId")
        }

        def pictures = Picture.findAllByBandId(bandId)

        if (!pictures){

            throw  new NotFoundException("The pictures with bandId = "+ bandId + " not found")
        }

        pictures.each{
            resultsPictures.add(

                    name: it.id+'-60X60.jpeg',
                    size: it.size,
                    url: it.url,
                    thumbnail_url: it.url.replaceAll('origin','60X60'),
                    delete_url:''+it.id,
                    delete_type:'GET'
            )
        }

        resultsPictures

    }


    def postPictures(def webrootDir, def bandId, def file){

        def resultsPictures = []


        if (!bandId){

            throw new NotFoundException("You must provider bandId")
        }

        if (!file){

            throw new NotFoundException("You must provider file picture")
        }


        def newPicture =  new Picture(
                bandId:bandId,
                url: 'http://dominio/uploads/fotoProcess.jpg',
                size:'origin',
                secureUrl: ''

        ).save()


        if (!newPicture){

            throw new BadRequestException("Picture not created")
        }


        def pictureId = newPicture.id

        def folderTarget = folderToSave(webrootDir,bandId)
        def fileName = pictureId+"-origin." + file.getContentType().replaceAll("image/","")
        urlMainPicture = urlMainPicture+fileName


        File fileDestiny = new File(folderTarget + "/" + fileName)
        file.transferTo(fileDestiny)



        burningImageService.doWith(fileDestiny.toString(), folderTarget)
                .execute (pictureId+'-800X600', {
                    it.scaleApproximate(800,600)
                    //it.watermark(pathToWatermark, ['right': 10, 'bottom': 10])
                })
                .execute (pictureId+'-200X160',{
                    it.scaleApproximate(200,160)
                })
                .execute (pictureId+'-100X75',{
                    it.scaleApproximate(100,75)
                })
                .execute (pictureId+'-60X60',{
                    it.scaleApproximate(60,60)
                })


        newPicture.url = urlMainPicture
        newPicture.save()


        resultsPictures= getPicturesByBand(bandId)

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


    def folderToSave(webrootDir,bandId){

        def cal = Calendar.instance
        def year = cal.get(Calendar.YEAR)
        def month = cal.get(Calendar.MONTH)

        def folderName = "uploads/" + year +"/"+ month + "/"


        File folderDate = new File(webrootDir,folderName);

        if (!folderDate.exists())
        {
            File folderUploads = new File(webrootDir,"uploads/");

            folderUploads.deleteDir() //Borramos las carpetas y archivos almacenados
            folderUploads.mkdir()
            folderDate.mkdirs();
            println "Termino de crear el folder..."
        }

        File folder = new File(webrootDir,folderName+bandId);

        if (!folder.exists())
        {
            folder.mkdir();
        }

        def forderTarget  = webrootDir + folderName + bandId

        urlMainPicture = domainMainPicture+folderName+bandId+"/"

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
                    band_id     : picture.bandId,
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

package Pictures

import javax.servlet.http.HttpServletResponse
import grails.converters.*
//import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import grails.plugin.gson.converters.GSON
import Pictures.exceptions.NotFoundException
import Pictures.exceptions.ConflictException
import Pictures.exceptions.BadRequestException

class PictureController {

    def pictureService


    def notAllowed(){

        def method = request.method

        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)

        setHeaders()

        def mapResult = [
                message: "Method $method not allowed",
                status: HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                error:"not_allowed"
        ]
        render mapResult as GSON
    }

    def getPicture (){

        def pictureId = params.pictureId
        def result

        setHeaders()

        try{
            result = pictureService.getPictures(pictureId)
            response.setStatus(HttpServletResponse.SC_OK)
            render result as GSON

        }catch (NotFoundException e){
            renderException(e)

        }catch (Exception e){
            renderException(e)
        }
    }

    def postPicture(){


        def webrootDir  = servletContext.getRealPath("/")
        def result

        setHeaders()

        try{

            def file        = request.getFile('file')
            result          = pictureService.postPictures(webrootDir, file)

            response.setStatus(HttpServletResponse.SC_CREATED)
            render result as GSON

        }catch (BadRequestException e){

            renderException(e)

        }catch (Exception e){

            renderException(e)

        }
    }

    def putPicture(){

        def pictureId   = params.pictureId
        def webrootDir  = servletContext.getRealPath("/")

        def result

        setHeaders()

        try{

            def file        = request.getFile('file')
            result          = pictureService.putPicture(pictureId, webrootDir, file)

            response.setStatus(HttpServletResponse.SC_CREATED)
            render result as GSON

        }catch(BadRequestException e){

            renderException(e)

        }catch(Exception e){

            renderException(e)

        }
    }

    def deletePicture(){

        def pictureId   = params.pictureId
        def webrootDir  = servletContext.getRealPath("/")

        def result

        setHeaders()

        result = pictureService.deletePicture(pictureId, webrootDir)
        response.setStatus(HttpServletResponse.SC_OK)
        render result as GSON
    }




    def setHeaders(){

        response.setContentType "application/json; charset=utf-8"
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "application/json;charset=UTF-8");
    }

    def renderException(def e){

        def statusCode
        def error

        try{
            statusCode  = e.status
            error       = e.error

        }catch(Exception ex){

            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            error = "internal_server_error"
        }

        response.setStatus(statusCode)

        def mapExcepction = [
                message: e.getMessage(),
                status: statusCode,
                error: error
        ]

        render mapExcepction as GSON

    }
}

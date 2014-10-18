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




    def setHeaders(){

        response.setContentType "application/json; charset=utf-8"
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "application/json;charset=UTF-8");
    }

    def renderException(def e){

        response.setStatus(e.status)

        def mapExcepction = [
                message: e.getMessage(),
                status: e.status,
                error: e.error
        ]
        render mapExcepction as GSON

    }
}

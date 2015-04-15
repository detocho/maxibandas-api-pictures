class UrlMappings {

	static mappings = {

        "/$pictureId?" {
            controller = "Picture"
            action = [GET: 'getPicture', POST:'postPicture',PUT:'putPicture' ,DELETE: 'deletePicture']
        }

        /*

        "/uploadPicture/$bandId?" {
            controller = "Picture"
            action = [GET: 'notAllowed', POST:'postPicture',PUT:'notAllowed' ,DELETE: 'notAllowed']
        }

        "/pictures/bands/$bandId?" {
            controller = 'Picture'
            action = [GET:'getPicturesByBand', POST:'notAllowed', PUT:'notAllowed', DELETE:'notAllowed']
        }

        "/pictures/del/$pictureId?" {
            controller = 'Picture'
            action = [GET:'deletePicture', POST:'notAllowed', PUT:'notAllowed', DELETE:'notAllowed']
        }
        */

        //TODO
        /*
        1.- vamos dejar el modo rest
        2.- para el post, revisar si podemos enviar en el body desde un form html la bandid
         */

	}
}

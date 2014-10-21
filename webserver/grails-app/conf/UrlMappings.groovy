class UrlMappings {

	static mappings = {

        "/$pictureId?" {
            controller = "Picture"
            action = [GET: 'getPicture', POST:'notAllowed',PUT:'notAllowed' ,DELETE: 'notAllowed']
        }

        "/uploadPicture/$bandId?" {
            controller = "Picture"
            action = [GET: 'notAllowed', POST:'postPicture',PUT:'notAllowed' ,DELETE: 'notAllowed']
        }

        "/pictures/bands/$bandId?"{
            controller = 'Picture'
            action = [GET:'getPicturesByBand', POST:'notAllowed', PUT:'notAllowed', DELETE:'notAllowed']
        }

	}
}

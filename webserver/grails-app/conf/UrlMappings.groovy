class UrlMappings {

	static mappings = {

        "/$pictureId?" {
            controller = "Picture"
            action = [GET: 'getPicture', POST:'notAllowed',PUT:'notAllowed' ,DELETE: 'notAllowed']
        }
	}
}

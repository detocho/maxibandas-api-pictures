import Pictures.Picture

class BootStrap {

    def init = { servletContext ->

        test{

        }
        development {

            if (Picture.count() == 0) {

                def picture01 = new Picture(
                        size: 'origin',
                        url: 'http://pictures.maxibanda.com.mx/2014/09/10/0001-origin.jpg',
                        secureUrl: ''
                )

                picture01.save()

            }
        }
        production{

        }
    }
    def destroy = {
    }
}

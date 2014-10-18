package Pictures



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Picture)
class PictureTests {

    def registeredPicture
    def samplePicture

    @Before
    void setUp(){

        registeredPicture = new Picture(

                size: '400X300',
                url: 'http://s0.uvnimg.com/musica/bandamax/fotos/photo/2013-01-02/premios-bandamax-2012-alfombra-8_409x611.jpg',
                secureUrl: ''
        )

        mockForConstraintsTests(Picture, [registeredPicture])

        samplePicture = new Picture(
                size: '100X200',
                url:'http://s0.uvnimg.com/musica/bandamax/fotos/photo/2013-01-02/premios-bandamax-2012-alfombra-8_409x611.jpg',
                secureUrl: 'http://s0.uvnimg.com/musica/bandamax/fotos/photo/2013-01-02/premios-bandamax-2012-alfombra-8_409x611.jpg'
        )
    }

    void test_PictureIsValid(){

        assertTrue(samplePicture.validate())
        samplePicture.save()
        assertEquals(samplePicture.size, '100X200')
    }

    void test_PictureIsNotValidWhitSizeNullOrBlank(){

        samplePicture.size = ''
        assertFalse(samplePicture.validate())
        assertEquals('blank', samplePicture.errors['size'])

        samplePicture.size = null
        assertFalse(samplePicture.validate())
        assertEquals('nullable', samplePicture.errors['size'])
    }

    void test_PictureIsNotValidWhitUrlNullOrBlank(){

        samplePicture.url = ''
        assertFalse(samplePicture.validate())
        assertEquals('blank', samplePicture.errors['url'])

        samplePicture.url = null
        assertFalse(samplePicture.validate())
        assertEquals('nullable', samplePicture.errors['url'])
    }


}

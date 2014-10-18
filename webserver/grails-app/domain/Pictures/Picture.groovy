package Pictures

class Picture {

    static constraints = {

        size        nullable:false, blank:false
        url         nullable: false , blank:false
        secureUrl   nullable: true, blank:true
    }

    String size
    String url
    String secureUrl
}

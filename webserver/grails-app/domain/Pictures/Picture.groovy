package Pictures

class Picture {

    static constraints = {

        size        nullable:false, blank:false
        url         nullable: false , blank:false
        secureUrl   nullable: true, blank:true
        bandId      nullable: false, blank:false
    }

    String bandId
    String size
    String url
    String secureUrl

}

environments {
    development {
        grails {
            mongo {
                host = "localhost"
                databaseName = "mb_pictures"
            }
        }
    }
    test {
        grails {
            mongo {
                host = "localhost"
                databaseName = "mb_pictures"
            }
        }
    }
    production {
        grails {
            mongo {

                // replicaSet = []
                host = "localhost"
                username = ""
                password = ""
                databaseName = "mb_pictures"
            }
        }
    }
}
class UrlMappings {
    static mappings = {
      "/squid"(controller:"squid", action:"newGame")
        "/"(controller:"squid", action:"newGame")

        "500"(view:'/error')
	}
}

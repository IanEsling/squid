class UrlMappings {
    static mappings = {
      "/$controller/$action?/$player?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "500"(view:'/error')
        "/squid/move/$player"(controller:"squid", action:"move")
    }
}

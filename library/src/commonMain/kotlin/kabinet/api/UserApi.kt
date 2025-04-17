package kabinet.api

import kabinet.model.Auth
import kabinet.model.PrivateInfo
import kabinet.model.User

object UserApi : ParentEndpoint(null, "/api/v1") {
    object Login : GetEndpoint<Auth>(this, "/login")

    object Users : ParentEndpoint(this, "/users") {
        object GetUser : GetEndpoint<User>(this)
        object GetPrivateInfo : GetEndpoint<PrivateInfo>(this, "/private")
    }
}
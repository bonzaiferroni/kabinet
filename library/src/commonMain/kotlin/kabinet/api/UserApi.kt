package kabinet.api

import kabinet.model.Auth
import kabinet.model.LoginRequest
import kabinet.model.SignUpRequest
import kabinet.model.SignUpResult
import kabinet.model.User

object UserApi : ApiNode(null, "api/v1/user") {
    object Login : PostEndpoint<LoginRequest, Auth>(this, "login")
    object Create : PostEndpoint<SignUpRequest, SignUpResult>(this, "create")
    object PrivateInfo : GetEndpoint<kabinet.model.PrivateInfo>(this, "private")
    object ReadInfo : GetEndpoint<User>(this)
    // object Update : PutEndpoint(this)
}
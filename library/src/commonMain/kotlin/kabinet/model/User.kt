package kabinet.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UserId,
    val username: String,
    val roles: RoleSet,
    val avatarUrl: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

val User.isAdmin: Boolean
    get() = UserRole.ADMIN in roles

val User.isUser: Boolean
    get() = UserRole.USER in roles

typealias UserId = String
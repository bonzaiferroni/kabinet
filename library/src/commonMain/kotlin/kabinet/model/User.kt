package kabinet.model

import kabinet.db.TableId
import kabinet.utils.randomUuidString
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class User(
    val userId: UserId,
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

@JvmInline @Serializable
value class UserId(override val value: String): TableId<String> {
    companion object { fun random() = UserId(randomUuidString()) }
}
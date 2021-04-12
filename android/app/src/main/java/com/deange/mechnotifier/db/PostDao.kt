package com.deange.mechnotifier.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.deange.mechnotifier.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
  @Query("SELECT * FROM Post ORDER BY createdSeconds DESC")
  suspend fun posts(): List<Post>

  @Query("SELECT * FROM Post ORDER BY createdSeconds DESC")
  fun postsFlow(): Flow<List<Post>>

  @Query("SELECT * FROM Post WHERE id = :postId")
  suspend fun post(postId: String): Post?

  @Insert
  suspend fun insertPost(post: Post)

  @Update(onConflict = REPLACE)
  suspend fun updatePost(post: Post)

  @Update(onConflict = REPLACE)
  suspend fun updatePosts(posts: List<Post>)
}

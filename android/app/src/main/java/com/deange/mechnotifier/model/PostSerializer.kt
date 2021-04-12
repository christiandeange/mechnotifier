package com.deange.mechnotifier.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import javax.inject.Inject

class PostSerializer
@Inject constructor(
  private val moshi: Moshi
) {
  fun posts(): ModelSerializer<Post> = ModelSerializer(moshi)

  class ModelSerializer<T>(val moshi: Moshi) {
    inline fun <reified T : Any> jsonToModel(dataMap: Map<String, *>): T {
      return moshi.adapter<T>().fromJsonValue(dataMap)!!
    }

    inline fun <reified T : Any> jsonToModel(json: String): T {
      return moshi.adapter<T>().fromJson(json)!!
    }

    inline fun <reified T : Any> modelToJson(model: T): String {
      return moshi.adapter<T>().toJson(model)
    }
  }
}

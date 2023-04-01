package com.example.searchuser.domain

import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.ErrorData
import com.example.searchuser.data.response.SearchUserResponse
import com.example.searchuser.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
): RepositoryInterface {
    override suspend fun searchUsers(query: String, per_page: Int?): Resource<SearchUserResponse> {
        return try {
            val response = searchApi.searchUser(query, 1, 10)
            when(response.code()){
                in 200..299 -> {
                    Resource.Success(response.body()!!)
                }
                in 400..499 -> {
                    val gson = Gson()
                    val type = object: TypeToken<ErrorData>() {}.type
                    val errorResponse: ErrorData = gson.fromJson(response.errorBody()?.charStream(), type)
                    Resource.Error(errorResponse.errors[0])
                }
                else -> {
                    Resource.Error(response.message())
                }
            }
        }catch (e: Exception){
            Resource.Error("An error occurred")
        }
    }
}
package io.dhruv1019.mangashelfnew.data.remote

import io.dhruv1019.mangashelfnew.modal.Manga
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/b/KEJO")
    suspend fun getMangaList() : Response<List<Manga>>

}
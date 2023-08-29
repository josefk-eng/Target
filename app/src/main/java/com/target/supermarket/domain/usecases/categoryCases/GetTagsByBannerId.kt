package com.target.supermarket.domain.usecases.categoryCases

import com.target.supermarket.domain.models.Tag
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetTagsByBannerId(private val repo: MainRepo) {
    operator fun invoke(id:Int):Flow<List<Tag>>{
        return repo.getTagsByBannerId(id)
    }
}
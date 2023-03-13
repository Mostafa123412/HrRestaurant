package com.example.hrrestaurant.data.dataSources

import android.content.Context

interface Mapper<Input, Output> {
    suspend fun map(input:Input?, context: Context):Output?
    suspend fun mapList(input:List<Input?> , context: Context) :List<Output?>
}
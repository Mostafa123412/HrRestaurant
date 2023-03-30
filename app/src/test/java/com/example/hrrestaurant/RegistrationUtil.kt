package com.example.hrrestaurant

import com.google.common.truth.Truth.assertThat
import org.junit.Test

object RegistrationUtil {

    @Test
    fun `empty email return false`() {
        val result = checkLogin("" , "1234")
        assertThat(result).isFalse()

    }

    private fun checkLogin (email: String, password: String):Boolean {

    }
}
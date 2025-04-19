package com.maspam.etrain.training.core.presentation.utils

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.UserModel
import kotlinx.serialization.json.Json

object CustomNavType {

    val userModelType = object :  NavType<UserModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): UserModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): UserModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: UserModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: UserModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val enrollModel = object :  NavType<EnrollModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): EnrollModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): EnrollModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: EnrollModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: EnrollModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

}
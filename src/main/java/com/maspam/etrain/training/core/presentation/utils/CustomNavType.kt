package com.maspam.etrain.training.core.presentation.utils

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.domain.model.TrainingModel
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

    val newsModel = object :  NavType<NewsModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): NewsModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): NewsModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: NewsModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: NewsModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val trainingModel = object :  NavType<TrainingModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): TrainingModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): TrainingModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: TrainingModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: TrainingModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val listSectionModel = object :  NavType<List<SectionModel>>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): List<SectionModel>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<SectionModel> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<SectionModel>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<SectionModel>) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val listPostTestModel = object :  NavType<List<PostTestModel>>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): List<PostTestModel>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<PostTestModel> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<PostTestModel>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<PostTestModel>) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val sectionModel = object :  NavType<SectionModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): SectionModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): SectionModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: SectionModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: SectionModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val postTestModel = object :  NavType<PostTestModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): PostTestModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): PostTestModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: PostTestModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: PostTestModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val listTopicModel = object :  NavType<List<TopicModel>>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): List<TopicModel>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<TopicModel> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<TopicModel>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<TopicModel>) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

    val topicModel = object :  NavType<TopicModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): TopicModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): TopicModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: TopicModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: TopicModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }

}
package com.prathamesh.shoppingsearchmock.data.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockItemApiService(
    private val items: List<String> = listOf<String>(
        "Abyssinian",
        "Aegean",
        "American Bobtail",
        "American Curl",
        "American Shorthair",
        "American Wirehair",
        "Arabian Mau",
        "Australian Mist",
        "Balinese",
        "Bambino",
        "Bengal",
        "Birman",
        "Bombay",
        "British Longhair",
        "British Shorthair",
        "Burmese",
        "Burmilla",
        "California Spangled",
        "Chantilly-Tiffany",
        "Chartreux",
        "Chausie",
        "Cheetoh",
        "Colorpoint Shorthair",
        "Cornish Rex",
        "Cymric",
        "Cyprus",
        "Devon Rex",
        "Donskoy",
        "Dragon Li",
        "Egyptian Mau",
        "European Burmese",
        "Exotic Shorthair",
        "Havana Brown",
        "Himalayan",
        "Japanese Bobtail",
        "Javanese",
        "Khao Manee",
        "Korat",
        "Kurilian",
        "LaPerm",
        "Maine Coon",
        "Malayan",
        "Manx",
        "Munchkin",
        "Nebelung",
        "Norwegian Forest Cat",
        "Ocicat",
        "Oriental",
        "Persian",
        "Pixie-bob",
        "Ragamuffin",
        "Ragdoll",
        "Russian Blue",
        "Savannah",
        "Scottish Fold",
        "Selkirk Rex",
        "Siamese",
        "Siberian",
        "Singapura",
        "Snowshoe",
    )
) : IItemApiService {

    override fun fetchAll(): Flow<List<String>> = flow {
        delay(100)
        emit(items)
    }

    override fun fetchByNameEqual(name: String): Flow<String?> = flow {
        delay(200)
        items
            .firstOrNull { it == name }
            .let { emit(it) }
    }

    override fun fetchByNameSimilar(name: String): Flow<List<String>> = flow {
        delay(300)
        items
            .filter { it.lowercase().contains(name) }
            .let { emit(it) }
    }
}
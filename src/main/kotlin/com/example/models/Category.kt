package com.example.models

enum class Category(val id: Int) {
    Sports(0),
    Health(1),
    Politics(2),
    Economics(3),
    Technology(4);

    companion object {
        fun fromId(id: Int): Category? {
            val categoryMap = mapOf(
                0 to Sports,
                1 to Health,
                2 to Politics,
                3 to Economics,
                4 to Technology,
            )
            return categoryMap[id]
        }
    }
}
package io.dhruv1019.mangashelfnew.modal

enum class SortBy( val sortname : String, val sortdsc : String) {
    NONE("Year","Year (Low to High)"),
    SCORE_LOW_TO_HIGH("Score","Score (Low to High)"),
    SCORE_HIGH_TO_LOW("Score","Score (High to Low)"),
    POPULARITY_LOW_TO_HIGH("Popularity","Popularity (Low to High)"),
    POPULARITY_HIGH_TO_LOW("Popularity","Popularity (High to Low)"),
}
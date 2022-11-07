package spaceEngineers.util.generator.map.labrecruits

fun String.numericPart(): Int {
    return replace("[a-z]*".toRegex(), "").toInt()
}

fun compare(o1: String, o2: String): Int {
    return o1.numericPart().compareTo(o2.numericPart())
}

val labRecruitsIdComparator = Comparator<String> { o1, o2 ->
    compare(o1, o2)
}

val labRecruitsDoorComparator = Comparator<Door> { o1, o2 ->
    compare(o1.id, o2.id)
}

val labRecruitsButtonComparator = Comparator<Button> { o1, o2 ->
    compare(o1.id, o2.id)
}

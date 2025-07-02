package org.efradev.todolist.utils

/**
 * Utility object for formatting dates in a user-friendly way
 */
object DateFormatter {

    /**
     * Formats an ISO date string to a user-friendly format
     * Example: "2023-06-01T10:00:00Z" -> "1 de junio, 2023"
     *
     * @param isoDateString The ISO date string to format
     * @return Formatted date string or original string if parsing fails
     */
    fun formatToUserFriendly(isoDateString: String): String {
        return try {
            // Extract date parts from ISO string (YYYY-MM-DDTHH:mm:ss or YYYY-MM-DDTHH:mm:ssZ)
            val datePart = isoDateString.split("T")[0]
            val parts = datePart.split("-")

            if (parts.size >= 3) {
                val year = parts[0]
                val month = parts[1].toInt()
                val day = parts[2].toInt()

                val monthNames = arrayOf(
                    "enero", "febrero", "marzo", "abril", "mayo", "junio",
                    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
                )

                val monthName = if (month in 1..12) monthNames[month - 1] else "mes desconocido"

                "$day de $monthName, $year"
            } else {
                // If format is unexpected, return a simple formatted version
                formatToShort(isoDateString)
            }
        } catch (e: Exception) {
            // If parsing fails, return the original string
            isoDateString
        }
    }

    /**
     * Formats an ISO date string to a short format
     * Example: "2023-06-01T10:00:00Z" -> "01/06/2023"
     *
     * @param isoDateString The ISO date string to format
     * @return Formatted date string or original string if parsing fails
     */
    fun formatToShort(isoDateString: String): String {
        return try {
            // Extract date parts from ISO string (YYYY-MM-DDTHH:mm:ss or YYYY-MM-DDTHH:mm:ssZ)
            val datePart = isoDateString.split("T")[0]
            val parts = datePart.split("-")

            if (parts.size >= 3) {
                val year = parts[0]
                val month = parts[1].padStart(2, '0')
                val day = parts[2].padStart(2, '0')

                "$day/$month/$year"
            } else {
                isoDateString
            }
        } catch (e: Exception) {
            // If parsing fails, return the original string
            isoDateString
        }
    }
}

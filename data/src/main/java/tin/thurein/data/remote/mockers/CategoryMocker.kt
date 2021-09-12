package tin.thurein.data.remote.mockers

object CategoryMocker {
    fun getCategory() : String {
        return """
            {
                "status": {
                    "statusCode": 200,
                    "message": "success"
                },
                "data": [
                    {
                        "id": 1,
                        "name": "Bento"
                    },
                    {
                        "id": 2,
                        "name": "Main"
                    },
                    {
                        "id": 3,
                        "name": "Appetizer"
                    }
                ]
            }
        """.trimIndent()
    }
}
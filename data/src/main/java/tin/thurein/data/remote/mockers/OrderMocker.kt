package tin.thurein.data.remote.mockers

object OrderMocker {
    fun getOrders() : String {
        return """
            {
                "status": {
                    "statusCode": 200,
                    "message": "success"
                },
                "data": [
                    {
                        "id": 10,
                        "title": "Special extra large fried rice",
                        "addon": [
                            {
                                "id": 21,
                                "title": "Fried Egg",
                                "protein": "# Protein (7)",
                                "almond": "1 Almond",
                                "moreEggs": "More Egg",
                                "quantity": 3
                            }
                        ],
                        "quantity": 1,
                        "created_at": "2021-09-12T19:14+00:00Z",
                        "alerted_at": "2021-09-12T19:16+00:00Z",
                        "expired_at": "2021-09-12T19:17+00:00Z"
                    }, 
                    {
                        "id": 11,
                        "title": "Chicken Noodle",
                        "addon": [
                            {
                                "id": 26,
                                "title": "Extra chicken",
                                "protein": "# Protein (7)",
                                "quantity": 2
                            }, 
                            {
                                "id": 27,
                                "title": "Sambal",
                                "protein": "# Protein (7)",
                                "almond": "1 Almond",
                                "quantity": 1
                            }
                        ],
                        "quantity": 1,
                        "created_at": "2021-06-10T15:10+00:00Z",
                        "alerted_at": "2021-06-10T15:13+00:00Z",
                        "expired_at": "2021-06-10T15:15+00:00Z"
                    },
                    {
                        "id": 12,
                        "title": "Chicken Noodle",
                        "addon": [
                            {
                                "id": 26,
                                "title": "Extra chicken",
                                "quantity": 2
                            }, 
                            {
                                "id": 27,
                                "title": "Sambal",
                                "quantity": 1
                            }
                        ],
                        "quantity": 1,
                        "created_at": "2021-06-10T15:10+00:00Z",
                        "alerted_at": "2021-06-10T15:13+00:00Z",
                        "expired_at": "2021-06-10T15:15+00:00Z"
                    },
                    {
                        "id": 13,
                        "title": "Chicken Noodle",
                        "addon": [
                            {
                                "id": 26,
                                "title": "Extra chicken",
                                "quantity": 2
                            }, 
                            {
                                "id": 27,
                                "title": "Sambal",
                                "quantity": 1
                            }
                        ],
                        "quantity": 1,
                        "created_at": "2021-06-10T15:10+00:00Z",
                        "alerted_at": "2021-06-10T15:13+00:00Z",
                        "expired_at": "2021-06-10T15:15+00:00Z"
                    }
                ]
            }
        """.trimIndent()
    }
}